# 基于 Spring Scheduling 的定时任务

## 配置

```java
@Bean
public ThreadPoolTaskExecutor taskExecutor() {
    ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
    pool.setCorePoolSize(5);
    pool.setMaxPoolSize(10);
    pool.setWaitForTasksToCompleteOnShutdown(true);

    return pool;
}

@Bean(initMethod = "init")
public ScheduleHandler scheduleHandler() {
    return new ScheduleHandler();
}
```

Spring Task的调度器默认是线程数为1的ThreadPoolTaskScheduler，自动装配类为TaskSchedulingAutoConfiguration，多任务之间的执行会相互影响，一定要修改默认值。



