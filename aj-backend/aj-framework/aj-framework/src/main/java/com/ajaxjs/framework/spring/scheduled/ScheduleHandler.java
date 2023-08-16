package com.ajaxjs.framework.spring.scheduled;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.data.jdbc_helper.JdbcConn;
import com.ajaxjs.framework.spring.filter.dbconnection.DataBaseConnection;
import com.ajaxjs.util.logger.LogHelper;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.*;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Data
public class ScheduleHandler implements InitializingBean, BeanFactoryAware {
    private static final LogHelper LOGGER = LogHelper.getLog(ScheduleHandler.class);

    //-----------------Spring 内部处理-----------------------------

    private BeanFactory beanFactory;

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

    //-----------------Spring 内部处理----------------------------
    private static final String TASK_NAME = "JOB_TASK_";

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

        for (ScheduledTask s : scheduledTasks) {
            Task task = s.getTask();

            if (task instanceof CronTask) {
                CronTask cronTask = (CronTask) s.getTask();
                ScheduledMethodRunnable scheduledMethodRunnable = (ScheduledMethodRunnable) cronTask.getRunnable();
                DataBaseConnection.initDb();
                String clzName = scheduledMethodRunnable.getMethod().getDeclaringClass().getName();// 类名

                try {
                    String sql = "SELECT id FROM schedule_job WHERE class_name = ? AND express = ?";
                    Integer id = CRUD.queryOne(Integer.class, sql, clzName, cronTask.getExpression());

                    if (id == null) { // 持久化
                        JobInfo info = new JobInfo();
                        info.setName(TASK_NAME + ATOMIC_LONG.getAndIncrement());
                        info.setExpress(cronTask.getExpression());
                        info.setClassName(clzName);
                        info.setMethod(scheduledMethodRunnable.getMethod().getName());
                        info.setStatus(JobInfo.ScheduledConstant.NORMAL_STATUS);

                        CRUD.create(info);
                    }

                    String _sql = ScheduledController.SQL + " WHERE `status` IN (1, 2)";
                    List<JobInfo> list = CRUD.list(JobInfo.class, _sql);

                    if (!CollectionUtils.isEmpty(list)) {
                        for (JobInfo job : list)
                            cancel(job.getExpress(), job.getClassName(), job.getId(), false);
                    }
                } finally {
                    JdbcConn.closeDb();
                }
            } else if (task instanceof FixedRateTask)
                LOGGER.info(task + "无法动态修改静态配置任务的状态、暂停/恢复任务，以及终止运行中任务");
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

    /**
     * 检验 Cron 表达式是否正确
     */
    public static boolean isValidExpression(String cron) {
        return CronExpression.isValidExpression(cron);
    }

//    计算下次运行时间点

    /**
     * 获取下次时间点列表
     *
     * @param cron  表达式
     * @param count 需要计算的数量
     * @return 返回日期集合
     */
    public static List<Date> calNextPoint(String cron, int count) {
        return calNextPoint(cron, new Date(), count);
    }

    /**
     * 获取下次时间点列表
     *
     * @param cron  表达式
     * @param date  当前日期
     * @param count 需要计算的数量
     * @return 返回日期集合
     */
    public static List<Date> calNextPoint(String cron, Date date, int count) {
        List<Date> points = new ArrayList<>();

        if (isValidExpression(cron)) {
            CronExpression csg = CronExpression.parse(cron);
            Date nextDate = date;

            for (int i = 0; i < count; i++) {
                nextDate = nextPoint(csg, nextDate);
                points.add(nextDate);
            }
        }

        return points;
    }

    /**
     * 计算下次时间点
     */
    public static Date nextPoint(CronExpression csg, Date date) {
        return Date.from(Objects.requireNonNull(csg.next(date.toInstant())));
    }
}
