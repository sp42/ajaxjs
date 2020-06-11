package com.ajaxjs.object_storage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多线程并发快速处理数据
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class LargeListIntegerSum {
	private long sum;// 存放整数的和
	private CyclicBarrier barrier;// 障栅集合点(同步器)
	private List<Integer> list;// 整数集合List
	private int threadCounts;// 使用的线程数

	public LargeListIntegerSum(List<Integer> list, int threadCounts) {
		this.list = list;
		this.threadCounts = threadCounts;
	}

	/**
	 * 获取List中所有整数的和
	 * 
	 * @return
	 */
	public long getIntegerSum() {
		ExecutorService exec = Executors.newFixedThreadPool(threadCounts);

		int len = list.size() / threadCounts;// 平均分割List
		// List中的数量没有线程数多（很少存在）
		if (len == 0) {
			threadCounts = list.size();// 采用一个线程处理List中的一个元素
			len = list.size() / threadCounts;// 重新平均分割List
		}

		barrier = new CyclicBarrier(threadCounts + 1);

		for (int i = 0; i < threadCounts; i++) {
			int max, length = list.size();

			if (i == threadCounts - 1) // 最后一个线程承担剩下的所有元素的计算
				max = length;
			else {
				max = len * (i + 1) > length ? length : len * (i + 1);
			}

			List<Integer> l = list.subList(i * len, max);
			// 创建线程任务
			exec.execute(new SubIntegerSumTask(l));
		}

		try {
			barrier.await();// 关键，使该线程在障栅处等待，直到所有的线程都到达障栅处
		} catch (InterruptedException e) {
			System.out.println(Thread.currentThread().getName() + ":Interrupted");
		} catch (BrokenBarrierException e) {
			System.out.println(Thread.currentThread().getName() + ":BrokenBarrier");
		}

		exec.shutdown();
		return sum;
	}

	/**
	 * 分割计算List整数和的线程任务
	 * 
	 * 
	 */
	public class SubIntegerSumTask implements Runnable {
		private List<Integer> subList;

		public SubIntegerSumTask(List<Integer> subList) {
			this.subList = subList;
		}

		@Override
		public void run() {
			long subSum = 0L;
			for (Integer i : subList) {
				subSum += i;
			}

			synchronized (LargeListIntegerSum.this) {// 在LargeListIntegerSum对象上同步
				sum += subSum;
			}

			try {
				barrier.await();// 关键，使该线程在障栅处等待，直到所有的线程都到达障栅处
			} catch (InterruptedException e) {
				System.out.println(Thread.currentThread().getName() + ":Interrupted");
			} catch (BrokenBarrierException e) {
				System.out.println(Thread.currentThread().getName() + ":BrokenBarrier");
			}

			System.out.println("分配给线程：" + Thread.currentThread().getName() + "那一部分List的整数和为：\tSubSum:" + subSum);
		}

	}

	public static void main(String[] args) {
		List<Integer> list = new ArrayList<>();
		for (int i = 1; i <= 1000000; i++)
			list.add(i);

		int threadCounts = 10;// 采用的线程数
		long start = System.currentTimeMillis();
		LargeListIntegerSum countListIntegerSum = new LargeListIntegerSum(list, threadCounts);
		long sum = countListIntegerSum.getIntegerSum();
		System.out.println("List中所有整数的和为:" + sum);

		System.out.println(System.currentTimeMillis() - start);
	}

}