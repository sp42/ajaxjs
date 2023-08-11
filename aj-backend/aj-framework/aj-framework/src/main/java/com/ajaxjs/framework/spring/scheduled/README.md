# 基于 Spring Scheduling 的定时任务

## 配置

```
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

参考：
- [从零搭建开发脚手架 基于Spring Task实现动态管理任务](https://juejin.cn/post/6955381726931583006)
- [Spring Boot Task 定时任务升级（启动、停止、变更执行周期）](https://my.oschina.net/sdlvzg/blog/1590946)
- [@Scheduled定时任务管理界面](https://blog.csdn.net/u012262450/article/details/120491382)

