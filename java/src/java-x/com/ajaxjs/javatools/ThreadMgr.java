package com.ajaxjs.javatools;

import java.util.concurrent.CountDownLatch;

import test.util.ImportThread;

public class ThreadMgr {
	
	/**
	 * 让主线程等待所有子线程执行完毕代码 
	 * @param threadNum
	 * @param clazz
	 */
	public static void waitAll(int threadNum, Class<? extends Thread> clazz) {
		CountDownLatch threadSignal = new CountDownLatch(threadNum);// 初始化countDown

		for (int i = 0; i < threadNum; i++) {// 开threadNum个线程
			Thread t = new ImportThread(i, threadSignal);
			t.start();
		}
		
		try {
			threadSignal.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 等待所有子线程执行完
		System.out.println(Thread.currentThread().getName() + "结束.");// 打印结束标记
	}
	
	public static class CountDownLatchDemo {
		public CountDownLatch latch;
		
		public int count = 5;

		class Worker2 implements Runnable {
			int index;

			Worker2(int index) {
				this.index = index;
			}

			public void run() {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("第" + index + "个线程结束休眠！");

				latch.countDown();
			}
		}

		CountDownLatchDemo() {
			latch = new CountDownLatch(count);

			for (int i = 1; i <= count; i++) new Thread(new Worker2(i)).start();
			
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("完成了。");
		}
	}
}
