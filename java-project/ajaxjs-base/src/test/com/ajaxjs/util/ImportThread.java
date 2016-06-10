package test.com.ajaxjs.util;

import java.util.concurrent.CountDownLatch;

/**
 * 子线程
 * @author http://blog.csdn.net/5iasp/article/details/7308584
 *
 */
public class ImportThread extends Thread {
	private int index;

	private CountDownLatch threadsSignal;

	public ImportThread(int index, CountDownLatch threadsSignal) {
		this.threadsSignal = threadsSignal;
		this.index = index;
	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + "开始...index:" + index);
		// Do somethings
		System.out.println(Thread.currentThread().getName() + "do execute ...");
		threadsSignal.countDown();// 线程结束时计数器减1
		System.out.println(Thread.currentThread().getName() + "结束. 还有" + threadsSignal.getCount() + " 个线程");
	}
}