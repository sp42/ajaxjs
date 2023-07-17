package com.ajaxjs.utils;

import com.ajaxjs.util.logger.LogHelper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 轮询定时器
 *
 * @author Frank Cheung sp42@qq.com
 */

/*
 * Timer计时器可以定时（指定时间执行任务）、延迟（延迟5秒执行任务）、周期性地执行任务（每隔个1秒执行任务）。
 * 但是，Timer存在一些缺陷。首先Timer对调度的支持是基于绝对时间的，而不是相对时间，所以它对系统时间的改变非常敏感。
 *
 * 其次Timer线程是不会捕获异常的，如果TimerTask抛出的了未检查异常则会导致Timer线程终止，同时Timer也不会重新恢复线程的执行，
 * 它会错误的认为整个Timer线程都会取消。
 *
 * 同时，已经被安排单尚未执行的TimerTask也不会再执行了，新的任务也不能被调度。故如果TimerTask抛出未检查的异常，Timer
 * 将会产生无法预料的行为。
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SetTimeout extends Timer {
    private static final LogHelper LOGGER = LogHelper.getLog(SetTimeout.class);

    /**
     * 开始时间用于计算是否超时
     */
    private final long startTime = new Date().getTime();

    public SetTimeout() {
    }

    /**
     * 构造函数
     *
     * @param handler 超时事件回调函数
     */
    public SetTimeout(Function<SetTimeout, Boolean> handler) {
        this.handler = handler;
    }

    /**
     * 超时的秒数
     */
    private int timeout = 60;

    /**
     * 轮询间隔的秒数
     */
    private long period = 3;

    /**
     * 超时的秒数
     */
    private long delay = 2;

    private Function<SetTimeout, Boolean> handler;

    /**
     * 开始轮询
     */
    public void schedule() {
        SetTimeout setTimeout = this;

        schedule(new TimerTask() {
            @Override
            public void run() {
                LOGGER.info("开始轮询");

                if ((new Date().getTime() - startTime) > timeout * 1000L) {
                    LOGGER.warning("超时中止");
                    setTimeout.cancelTask();
                }

                if (!handler.apply(setTimeout))
                    setTimeout.cancelTask();
            }

        }, delay * 1000, period * 1000);
    }

    /**
     * 取消轮询
     */
    public void cancelTask() {
        LOGGER.info("取消轮询");
        cancel();
    }

    /**
     * 开始定时器
     */
    public void startTimeout() {
        SetTimeout setTimeout = this;

        schedule(new TimerTask() {
            @Override
            public void run() {
                LOGGER.info("开始执行定时器");
                if (!handler.apply(setTimeout))
                    cancelTask();
            }
        }, delay * 1000);
    }

    /**
     * 简单的定时器
     *
     * @param handler 回调函数
     * @param delay   延迟时间，单位秒
     */
    public static void simpleTimeout(Runnable handler, int delay) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                LOGGER.info("开始执行定时器");
                handler.run();
            }
        }, delay * 1000L);
    }

    private static ScheduledExecutorService scheduledThreadPool;

    /**
     * 设置一个定时任务
     *
     * @param command 待执行任务
     * @param delay   延迟时间，单位秒
     * @return 任务结果
     */
    public static ScheduledFuture<?> setTimeout(Runnable command, long delay) {
        if (scheduledThreadPool == null)
            scheduledThreadPool = Executors.newScheduledThreadPool(5);

        return scheduledThreadPool.schedule(command, delay, TimeUnit.SECONDS);
    }

    /**
     * 开启一个轮询任务
     *
     * @param job          待执行任务
     * @param initialDelay 初始延迟时间，单位分钟
     * @param period       轮询间隔，单位分钟
     */
    public static void timeout(Runnable job, int initialDelay, int period) {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(job, 1, 1, TimeUnit.MINUTES);
    }
}