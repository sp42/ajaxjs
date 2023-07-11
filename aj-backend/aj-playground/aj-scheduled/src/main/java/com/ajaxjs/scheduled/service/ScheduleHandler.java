package com.ajaxjs.scheduled.service;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.scheduled.model.JobInfoVO;
import com.ajaxjs.scheduled.model.ScheduledConstant;
import com.ajaxjs.util.logger.LogHelper;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class ScheduleHandler implements InitializingBean, BeanFactoryAware {
    private static final LogHelper LOGGER = LogHelper.getLog(ScheduleHandler.class);

    private static final String APP_NAME = "spring.application.name";
    private static final String TASK_NAME = "_JOB_TASK";
    private static final AtomicLong ATOMIC_LONG = new AtomicLong(0L);

    private BeanFactory beanFactory;

    /**
     * ThreadPoolTaskExecutor是 Spring框架提供的一个线程池执行器，用于管理和执行异步任务
     */
    private ThreadPoolTaskExecutor executor;

    private Set<ScheduledTask> scheduledTasks;

    /**
     *
     */
    private ScheduledAnnotationBeanPostProcessor scheduledAnnotationBeanPostProcessor;

    /**
     * ScheduledTaskRegistrar是Spring框架中用于注册和管理定时任务的类。它是Spring内部的一个调度器，负责管理和执行定时任务。
     */
    private ScheduledTaskRegistrar scheduledTaskRegistrar;

    private Environment environment;

    public void init() {
        // 获取了所有被 @Scheduled 注解修饰的任务列表
        scheduledTasks = scheduledAnnotationBeanPostProcessor.getScheduledTasks();

        if (!CollectionUtils.isEmpty(scheduledTasks)) {
            String appName = environment.getProperty(APP_NAME);
            scheduledTasks.forEach(s -> {
                CronTask cronTask = (CronTask) s.getTask();
                ScheduledMethodRunnable scheduledMethodRunnable = (ScheduledMethodRunnable) cronTask.getRunnable();
                // 提取了任务的类名和方法名
                Class<?> clazz = scheduledMethodRunnable.getMethod().getDeclaringClass();// 类名
                String name = scheduledMethodRunnable.getMethod().getName();//  方法名

                String sql = "SELECT id FROM schedule_job_info  WHERE job_express = ? AND job_app_name = ?";
                Integer id = CRUD.queryOne(Integer.class, sql, cronTask.getExpression(), appName);

                if (id == null) {
                    JobInfoVO jobInfoVO = new JobInfoVO();
                    jobInfoVO.setJobAppName(appName);
                    jobInfoVO.setJobExpress(cronTask.getExpression());
                    jobInfoVO.setJobClassName(clazz.getName());
                    jobInfoVO.setJobName(jobInfoVO.getJobAppName().toUpperCase(Locale.ENGLISH) + TASK_NAME + ATOMIC_LONG.getAndIncrement());
                    jobInfoVO.setJobMethodName(name);
//                    scheduledMapper.insertJob(jobInfoVO);
                }
            });

            String sql = ScheduledController.SQL + " FROM schedule_job_info WHERE `job_status` IN (1, 2)";
            List<JobInfoVO> list = CRUD.list(JobInfoVO.class, sql);

            if (list != null) {
                for (JobInfoVO job : list)
                    cancel(job.getJobExpress(), job.getJobClassName(), job.getId(), false);
            }
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() {
        environment = beanFactory.getBean(Environment.class);
        executor = beanFactory.getBean(ThreadPoolTaskExecutor.class);
        scheduledAnnotationBeanPostProcessor = beanFactory.getBean(ScheduledAnnotationBeanPostProcessor.class);

        try {
            Field registrar = scheduledAnnotationBeanPostProcessor.getClass().getDeclaredField("registrar");
            registrar.setAccessible(true);
            scheduledTaskRegistrar = (ScheduledTaskRegistrar) registrar.get(scheduledAnnotationBeanPostProcessor);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 恢复指定类中指定方法的定时任务
     */
    public void resume(String clazz, String methodName, String express, Long id) {
        try {
            Class<?> aClass = Class.forName(clazz);
            Object bean = beanFactory.getBean(aClass);
            CronTask cronTask = new CronTask(createRunnable(bean, aClass.getDeclaredMethod(methodName)), new CronTrigger(express, TimeZone.getDefault()));
            scheduledTasks.add(scheduledTaskRegistrar.scheduleCronTask(cronTask));
            scheduledTaskRegistrar.addCronTask(cronTask);
            CRUD.jdbcWriterFactory().write(updateStatus, ScheduledConstant.NORMAL_STATUS, id);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    protected Runnable createRunnable(Object target, Method method) {
        Assert.isTrue(method.getParameterCount() == 0, "Only no-arg methods may be annotated with @Scheduled");
        Method invocableMethod = AopUtils.selectInvocableMethod(method, target.getClass());

        return new ScheduledMethodRunnable(target, invocableMethod);
    }

    /**
     * 触发指定类中的指定方法
     */
    public void trigger(String clazz, String methodName) {
        try {
            Class<?> aClass = Class.forName(clazz);
            Method declaredMethod = aClass.getDeclaredMethod(methodName);

            executor.execute(() -> {
                try {
                    declaredMethod.invoke(beanFactory.getBean(aClass));
                } catch (Exception ex) {
                    LOGGER.warning("trigger error", ex);
                }
            });
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void remove(String express, String clazz, Long id) {
        cancel(express, clazz, id, Boolean.FALSE);
        CRUD.jdbcWriterFactory().write(updateStatus, ScheduledConstant.DELETE_STATUS, id);
    }

    final static String updateStatus = "UPDATE schedule_job_info SET `job_status` = ? WHERE id = ?";

    /**
     * 取消指定表达式和类的定时任务，并根据需要更新任务状态
     */
    public void cancel(String express, String clazz, Long id, boolean isUpdate) {
        Set<ScheduledTask> scheduledTasks0 = new LinkedHashSet<>(); // 需要取消的任务
        List<CronTask> cronTaskList = new ArrayList<>(scheduledTaskRegistrar.getCronTaskList()); // 获取了所有定时任务的列表

        for (ScheduledTask scheduledTask : scheduledTasks) {
            CronTask cronTask = (CronTask) scheduledTask.getTask();
            ScheduledMethodRunnable runnable = (ScheduledMethodRunnable) cronTask.getRunnable();

            if (express.equals(cronTask.getExpression()) && clazz.equals(runnable.getMethod().getDeclaringClass().getName())) {
                scheduledTask.cancel();
                cronTaskList.remove(cronTask);
                scheduledTasks0.add(scheduledTask);

                if (isUpdate)
                    CRUD.jdbcWriterFactory().write(updateStatus, ScheduledConstant.CANCEL_STATUS, id);
            }
        }

        scheduledTaskRegistrar.setCronTasksList(cronTaskList);

        if (!CollectionUtils.isEmpty(scheduledTasks0))
            scheduledTasks.removeAll(scheduledTasks0);
    }
}
