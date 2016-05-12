package com.ajaxjs.javatools.task;

import java.util.Date;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * 实现定时任务
 * @author http://blog.csdn.net/5iasp/article/details/10950529
 */
public class TaskEngine {
	static Logger logger = Logger.getLogger(TaskEngine.class.getName());

	private static class PriorityQueue {

		public void enqueue(int priority, Object object) {
			if (priority > HIGH_PRIORITY)
				priority = HIGH_PRIORITY;
			else if (priority < LOW_PRIORITY)
				priority = LOW_PRIORITY;
			switch (priority) {
			case HIGH_PRIORITY: // '\002'
				high.addFirst(object);
				break;

			case MEDIUM_PRIORITY: // '\001'
				medium.addFirst(object);
				break;

			case LOW_PRIORITY: // '\0'
				low.addFirst(object);
				break;
			}
		}

		public boolean isEmpty() {
			return high.isEmpty() && medium.isEmpty() && low.isEmpty();
		}

		public int size() {
			return high.size() + medium.size() + low.size();
		}

		public Object dequeue() {
			Object object;
			if (!high.isEmpty())
				object = high.removeLast();
			else if (!medium.isEmpty())
				object = medium.removeLast();
			else if (!low.isEmpty())
				object = low.removeLast();
			else
				throw new NoSuchElementException("Queue is empty.");
			if (!low.isEmpty()) medium.addFirst(low.removeLast());
			if (!medium.isEmpty()) high.addFirst(medium.removeLast());
			return object;
		}

		private LinkedList<Object> high;
		private LinkedList<Object> medium;
		private LinkedList<Object> low;

		private PriorityQueue() {
			high = new LinkedList<>();
			medium = new LinkedList<>();
			low = new LinkedList<>();
		}
	}

	private static class ScheduledTask extends TimerTask {
		public void run() {
			TaskEngine.addTask(priority, task);
		}

		private int priority;
		private Runnable task;

		ScheduledTask(int priority, Runnable task) {
			this.priority = priority;
			this.task = task;
		}
	}

	private static class TaskEngineWorker extends Thread {
		public void stopWorker() {
			done = true;
		}

		public void run() {
			do {
				if (done) break;
				int currentThreadPriority = getPriority();
				int newThreadPriority = currentThreadPriority;
				
				try {
					TaskWrapper wrapper = TaskEngine.nextTask();
					int desiredTaskPriority = wrapper.getPriority();
					newThreadPriority = desiredTaskPriority != 2 ? ((int) (desiredTaskPriority != 1 ? 2 : 5)) : 9;
					
					if (newThreadPriority != currentThreadPriority)
						try {
							logger.info("Running task engine worker ("
									+ wrapper.getTask().getClass()
									+ ") at thread priority "
									+ newThreadPriority);
							setPriority(newThreadPriority);
						} catch (Exception e) {
							logger.warning(e.toString());
						}
					logger.info("Executing task (" + wrapper.getTask().getClass() + ")");
					wrapper.getTask().run();
					logger.info("Completed execution task (" + wrapper.getTask().getClass() + ")");
					if (newThreadPriority != currentThreadPriority)
						try {
							logger.info("Restoring task engine worker thread to thread priority - " + currentThreadPriority);
							setPriority(currentThreadPriority);
						} catch (Exception e) {
							logger.warning(e.toString());
						}
				} catch (Exception e) {
					logger.warning(e.toString());
					if (newThreadPriority != currentThreadPriority)
						try {
							logger.info("Restoring task engine worker thread to thread priority - " + currentThreadPriority);
							setPriority(currentThreadPriority);
						} catch (Exception e2) {
							e2.printStackTrace();
						}
				}
			} while (true);
		}

		private boolean done;

		TaskEngineWorker(String name) {
			super(TaskEngine.threadGroup, name);
			done = false;
		}
	}

	private TaskEngine() {
	}

	public static void start() {
		synchronized (lock) {
			started = true;
			lock.notifyAll();
		}
	}

	private static void initialize() {
		taskTimer = new Timer(true);
		taskQueue = new PriorityQueue();
		threadGroup = new ThreadGroup("Task Engine Workers");
		workers = new TaskEngineWorker[5];
		for (int i = 0; i < workers.length; i++) {
			workers[i] = new TaskEngineWorker("Task Engine Worker " + i);
			workers[i].setDaemon(true);
			workers[i].start();
		}

	}

	public static int size() {
		synchronized (lock) {
			return taskQueue.size();
		}

	}

	public static int getNumWorkers() {
		return workers.length;
	}

	public static void addTask(Runnable task) {
		addTask(1, task);
	}

	public static void addTask(int priority, Runnable task) {
		synchronized (lock) {
			if ((double) taskQueue.size() > Math.ceil(workers.length / 2)) {
				busyTimestamp = System.currentTimeMillis();
				addWorker();
			} else if (workers.length > 3) removeWorker();
			
			TaskWrapper wrapper = new TaskWrapper(priority, task);
			taskQueue.enqueue(priority, wrapper);
			lock.notify();
		}
	}

	public static TimerTask scheduleTask(Runnable task, Date date) {
		return scheduleTask(1, task, date);
	}

	public static TimerTask scheduleTask(int priority, Runnable task, Date date) {
		TimerTask timerTask = new ScheduledTask(priority, task);
		taskTimer.schedule(timerTask, date);
		return timerTask;
	}

	// 在1delay秒后执行此任务,每次间隔2秒period
	public static TimerTask scheduleTask(Runnable task, long delay, long period) {
		return scheduleTask(1, task, delay, period);
	}

	public static TimerTask scheduleTask(int priority, Runnable task, long delay, long period) {
		TimerTask timerTask = new ScheduledTask(priority, task);
		taskTimer.scheduleAtFixedRate(timerTask, delay, period);
		return timerTask;
	}

	public static void shutdown() {
		taskTimer.cancel();
	}

	public static void restart() {
		taskTimer.cancel();
		initialize();
	}

	private static TaskWrapper nextTask() {
		synchronized (lock) {
			while (taskQueue.isEmpty() || !started)
				try {
					lock.wait();
				} catch (InterruptedException e) {}
			return (TaskWrapper) taskQueue.dequeue();
		}
	}

	private static void addWorker() {
		if (workers.length < 30 && System.currentTimeMillis() > newWorkerTimestamp + 2000L) {
			int newSize = workers.length + 1;
			int lastIndex = newSize - 1;
			
			TaskEngineWorker newWorkers[] = new TaskEngineWorker[newSize];
			System.arraycopy(workers, 0, newWorkers, 0, workers.length);
			newWorkers[lastIndex] = new TaskEngineWorker("Task Engine Worker " + lastIndex);
			newWorkers[lastIndex].setDaemon(true);
			newWorkers[lastIndex].start();
			workers = newWorkers;
			
			newWorkerTimestamp = System.currentTimeMillis();
		}
	}

	private static void removeWorker() {
		if (workers.length > 3 && System.currentTimeMillis() > busyTimestamp + 5000L) {
			workers[workers.length - 1].stopWorker();
			int newSize = workers.length - 1;
			TaskEngineWorker newWorkers[] = new TaskEngineWorker[newSize];
			System.arraycopy(workers, 0, newWorkers, 0, newSize);
			workers = newWorkers;
			busyTimestamp = System.currentTimeMillis();
		}
	}

	public static final int HIGH_PRIORITY = 2;
	public static final int MEDIUM_PRIORITY = 1;
	public static final int LOW_PRIORITY = 0;
	private static PriorityQueue taskQueue = null;
	private static ThreadGroup threadGroup;
	private static TaskEngineWorker workers[] = null;
	private static Timer taskTimer = null;
	private static Object lock = new Object();
	private static long newWorkerTimestamp = System.currentTimeMillis();
	private static long busyTimestamp = System.currentTimeMillis();
	private static boolean started = false;

	static {
		initialize();
	}
}