package com.ajaxjs.util;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import com.ajaxjs.util.logger.LogHelper;

/**
 * 轮询定时器
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class SetTimeout extends Timer {
	private static final LogHelper LOGGER = LogHelper.getLog(SetTimeout.class);

	/**
	 * 开始时间用于计算是否超时
	 */
	private long startTime = new Date().getTime();

	public SetTimeout() {
	}

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

	public void schedule() {
		SetTimeout setTimeout = this;

		schedule(new TimerTask() {
			@Override
			public void run() {
				LOGGER.info("开始轮询");

				if ((new Date().getTime() - startTime) > timeout * 1000) {
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

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public Function<SetTimeout, Boolean> getHandler() {
		return handler;
	}

	public void setHandler(Function<SetTimeout, Boolean> handler) {
		this.handler = handler;
	}

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

	public static void simpleTimeout(Runnable handler, int delay) {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				LOGGER.info("开始执行定时器");
				handler.run();
			}
		}, delay * 1000);
	}

	private static ScheduledExecutorService scheduledThreadPool;

	public static ScheduledFuture<?> setTimeout(Runnable command, long delay) {
		if (scheduledThreadPool == null)
			scheduledThreadPool = Executors.newScheduledThreadPool(5);

		return scheduledThreadPool.schedule(command, delay, TimeUnit.SECONDS);
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

}
