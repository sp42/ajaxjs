package com.ajaxjs.javatools.task.timeout;

import java.util.Iterator;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 超时任务
 * 该工具类适用于以下场合：

	a) 关闭空闲连接。服务器中，有很多客户端的连接，空闲一段时间之后需要关闭之。
	b) 缓存。缓存中的对象，超过了空闲时间，需要从缓存中移出。
	c) 任务超时处理。在网络协议滑动窗口请求应答式交互时，处理超时未响应的请求。
	d)心跳任务
 * 用法：

		TimeoutTask<String> timeoutTask = new TimeoutTask<String>("test task") {
            protected void process(String submit) {
                System.out.println("process : " + submit);
               
            }
        };
        timeoutTask.put("test", 1, TimeUnit.MINUTES);
        timeoutTask.start();
 * @author http://yuancihang.iteye.com/blog/1098182
 *
 * @param <T>
 */
public abstract class TimeoutTask<T> implements Runnable {
	private DelayQueue<DelayItem<T>> timeoutQueue = new DelayQueue<DelayItem<T>>(); // 超时队列
	private Thread daemonThread;
	private AtomicBoolean running = new AtomicBoolean(true);
	private String name;
	private Object lock = new Object();

	public TimeoutTask(String name) {
		this.name = name;
	}

	public void start() {
		if (daemonThread == null) {
			daemonThread = new Thread(this);
			daemonThread.setDaemon(true);
			daemonThread.setName(name);
			daemonThread.start();
		}
	}

	public void stop() {
		if (daemonThread != null) {
			running.compareAndSet(true, false);
			daemonThread.interrupt();
		}
	}

	public void join() throws InterruptedException {
		if (daemonThread != null) daemonThread.join();
	}

	@Override
	public void run() {
		while (running.get()) {
			try {
				if (timeoutQueue.isEmpty())// 如果超时队列是空的就阻塞守护线程
					lock();
				
				DelayItem<T> delayItem = timeoutQueue.take();
				if (delayItem == null)  continue;

				process(delayItem.getItem());
				// 重新开始延迟
				delayItem.resetTime();
				timeoutQueue.put(delayItem);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	protected void lock() throws InterruptedException {
		synchronized (lock) {
			lock.wait();
		}
	}

	protected void unlock() {
		synchronized (lock) {
			lock.notifyAll();
		}
	}

	protected abstract void process(T submit);

	public void put(T submit, long timeout, TimeUnit unit) {
		int minutes = (int) TimeUnit.MINUTES.convert(timeout, unit);
		timeoutQueue.put(new DelayItem<T>(submit, minutes));
		unlock();
	}

	public void remove(T submit) {
		DelayItem<T> delayItem = getDelayItem(submit);
		if (delayItem != null) timeoutQueue.remove(delayItem);
	}

	public void resetTime(T submit) {
		DelayItem<T> delayItem = getDelayItem(submit);
		if (delayItem != null) delayItem.resetTime();
	}

	protected DelayItem<T> getDelayItem(T submit) {
		Iterator<DelayItem<T>> it = timeoutQueue.iterator();
		while (it.hasNext()) {
			DelayItem<T> delayItem = it.next();
			if (delayItem.getItem() == submit) return delayItem;
		}

		return null;
	}
}