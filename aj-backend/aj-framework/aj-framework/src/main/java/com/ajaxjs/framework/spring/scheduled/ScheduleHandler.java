package com.ajaxjs.framework.spring.scheduled;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.util.logger.LogHelper;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.*;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@Data
public class ScheduleHandler implements InitializingBean, BeanFactoryAware {
    private static final LogHelper LOGGER = LogHelper.getLog(ScheduleHandler.class);

    //-----------------Spring 内部处理-----------------------------

    private BeanFactory beanFactory;

    private Environment environment;

    /**
     * ThreadPoolTaskExecutor是 Spring框架提供的一个线程池执行器，用于管理和执行异步任务
     */
    private ThreadPoolTaskExecutor executor;

    /**
     * 带有 @Scheduled 注解的处理器
     */
    private ScheduledAnnotationBeanPostProcessor scheduledProcessor;

    /**
     * ScheduledTaskRegistrar 是 Spring 框架中用于注册和管理定时任务的类。它是 Spring 内部的一个调度器，负责管理和执行定时任务。
     */
    private ScheduledTaskRegistrar scheduledTaskRegistrar;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() {
        environment = beanFactory.getBean(Environment.class);
        executor = beanFactory.getBean(ThreadPoolTaskExecutor.class);
        scheduledProcessor = beanFactory.getBean(ScheduledAnnotationBeanPostProcessor.class);

        try {
            Field registrar = scheduledProcessor.getClass().getDeclaredField("registrar");
            registrar.setAccessible(true);
            scheduledTaskRegistrar = (ScheduledTaskRegistrar) registrar.get(scheduledProcessor);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.warning(e);
        }
    }

    //-----------------Spring 内部处理-----------------------------

    private static final String APP_NAME = "spring.application.name";
    private static final String TASK_NAME = "_JOB_TASK";
    private static final AtomicLong ATOMIC_LONG = new AtomicLong(0L);

    /**
     * 所有被 @Scheduled 注解修饰的任务
     */
    private Set<ScheduledTask> scheduledTasks;

    /**
     * 初始化
     */
    public void init() {
        LOGGER.info("初始化定时任务管理器");
        // 获取了所有被 @Scheduled 注解修饰的任务列表
        scheduledTasks = scheduledProcessor.getScheduledTasks();

        if (CollectionUtils.isEmpty(scheduledTasks))
            return;

        String appName = environment.getProperty(APP_NAME);

        for (ScheduledTask s : scheduledTasks) {
            Task task = s.getTask();

            if (task instanceof CronTask) {
                CronTask cronTask = (CronTask) s.getTask();
                ScheduledMethodRunnable scheduledMethodRunnable = (ScheduledMethodRunnable) cronTask.getRunnable();

                String sql = "SELECT id FROM schedule_job_info WHERE job_express = ? AND job_app_name = ?";
                Integer id = CRUD.queryOne(Integer.class, sql, cronTask.getExpression(), appName);

                if (id == null) { // 持久化
                    JobInfo jobInfoVO = new JobInfo();
                    jobInfoVO.setAppName(appName);
                    jobInfoVO.setExpress(cronTask.getExpression());
                    jobInfoVO.setClassName(scheduledMethodRunnable.getMethod().getDeclaringClass().getName());// 类名
                    jobInfoVO.setName(jobInfoVO.getAppName().toUpperCase() + TASK_NAME + ATOMIC_LONG.getAndIncrement());
                    jobInfoVO.setMethodName(scheduledMethodRunnable.getMethod().getName());
                    LOGGER.info("持久化");
                    // scheduledMapper.insertJob(jobInfoVO);
                }

                String _sql = ScheduledController.SQL + " FROM schedule_job_info WHERE `job_status` IN (1, 2)";
                List<JobInfo> list = CRUD.list(JobInfo.class, _sql);

                if (!CollectionUtils.isEmpty(list)) {
                    for (JobInfo job : list)
                        cancel(job.getExpress(), job.getClassName(), job.getId(), false);
                }
            } else if (task instanceof FixedRateTask) {
                // 无法动态修改静态配置任务的状态、暂停/恢复任务，以及终止运行中任务
            }
        }
    }

    /**
     * 取消指定表达式和类的定时任务，并根据需要更新任务状态
     * 主要操作两个列表：scheduledTaskRegistrar.getCronTaskList() 和 scheduledTasks
     *
     * @param express  表达式
     * @param clz      类
     * @param id       任务 id
     * @param isUpdate 是否更新任务状态
     */
    public void cancel(String express, String clz, Integer id, boolean isUpdate) {
        Set<ScheduledTask> scheduledTasks0 = new LinkedHashSet<>(); // 需要取消的任务
        List<CronTask> cronTaskList = new ArrayList<>(scheduledTaskRegistrar.getCronTaskList()); // 获取了所有定时任务的列表

        for (ScheduledTask scheduledTask : scheduledTasks) {
            CronTask cronTask = (CronTask) scheduledTask.getTask();
            ScheduledMethodRunnable runnable = (ScheduledMethodRunnable) cronTask.getRunnable();

            if (express.equals(cronTask.getExpression()) && clz.equals(runnable.getMethod().getDeclaringClass().getName())) {
                scheduledTask.cancel();
                cronTaskList.remove(cronTask);
                scheduledTasks0.add(scheduledTask);

                if (isUpdate)
                    CRUD.jdbcWriterFactory().write(ScheduledController.updateStatus, JobInfo.ScheduledConstant.CANCEL_STATUS, id);
            }
        }

        scheduledTaskRegistrar.setCronTasksList(cronTaskList); // 设置新的

        if (!CollectionUtils.isEmpty(scheduledTasks0))
            scheduledTasks.removeAll(scheduledTasks0);
    }
}
