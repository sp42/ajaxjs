package com.ajaxjs.developertools;

import com.ajaxjs.data.SmallMyBatis;
import com.ajaxjs.data.jdbc_helper.JdbcConn;
import com.ajaxjs.data.jdbc_helper.JdbcWriter;
import com.ajaxjs.framework.spring.validator.ValidatorContextAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.ajaxjs.framework.spring.scheduled.ScheduledController;
import com.ajaxjs.framework.spring.scheduled.ScheduleHandler;

import javax.sql.DataSource;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 程序配置
 */
@Configuration
public class DevToolsConfiguration implements WebMvcConfigurer {
    @Value("${db.url}")
    private String url;

    @Value("${db.user}")
    private String user;

    @Value("${db.psw}")
    private String psw;

    @Bean(value = "dataSource", destroyMethod = "close")
    DataSource getDs() {
        return JdbcConn.setupJdbcPool("com.mysql.cj.jdbc.Driver", url, user, psw);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // 允许所有来源
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 允许的请求方法
                .allowedHeaders("*"); // 允许所有请求头
    }

    @Bean
    SmallMyBatis smallMyBatis() {
        SmallMyBatis s = new SmallMyBatis();
        s.loadXML("sql.xml");

        return s;
    }

    @Bean
    @Scope("prototype")
    public JdbcWriter jdbcWriter() {
        return new JdbcWriter();
    }

    // 初始化 Spring 任务调度器
    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(5); // 指定线程数
        pool.setMaxPoolSize(10);
        pool.setWaitForTasksToCompleteOnShutdown(true);

        return pool;
    }

    // 初始化任务调度管理
    @Bean(initMethod = "init")
    public ScheduleHandler scheduleHandler() {
        return new ScheduleHandler();
    }

    // 注入任务调度的控制器
    @Bean
    public ScheduledController scheduledController() {
        return new ScheduledController();
    }

    private static final AtomicLong ATOMIC_LONG = new AtomicLong(0L);

    // 添加定时任务
    @Scheduled(cron = "0/2 * * * * *") // cron 表达式，每5秒执行
    public void doTask() {
        System.out.println("我是定时任务~" + ATOMIC_LONG.getAndIncrement());
    }

    @Bean
    public ValidatorContextAware ValidatorContextAware() {
        return new ValidatorContextAware();
    }
}
