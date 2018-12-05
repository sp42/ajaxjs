package com.ajaxjs.incubation.net.roundRobin;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 加权轮询 http://orchome.com/685
 * 
 * @author Frank Cheung
 *
 */
public class WeightRoundRobin {

	private List<Server> servers;

	private int currentIndex;
	private int totalServer;
	private int currentWeight;
	private int maxWeight;
	private int gcdWeight;

	public WeightRoundRobin() {
		servers = new ArrayList<>();
		servers.add(new Server("192.168.1.2", 5));
		servers.add(new Server("192.168.1.3", 10));
		servers.add(new Server("192.168.1.4", 15));
		servers.add(new Server("192.168.1.5", 100));
		servers.add(new Server("192.168.1.6", 5));
		servers.add(new Server("192.168.1.7", 20));
		servers.add(new Server("192.168.1.8", 30));
		totalServer = servers.size();
		currentIndex = totalServer - 1;
		maxWeight = maxWeight();
		gcdWeight = serverGcd();
	}

	public Server round() {
		while (true) {
			currentIndex = (currentIndex + 1) % totalServer;

			if (currentIndex == 0) {
				currentWeight = currentWeight - gcdWeight;

				if (currentWeight <= 0) {
					currentWeight = maxWeight;
					if (currentWeight == 0)
						return null;
				}
			}

			if (servers.get(currentIndex).getWeight() >= currentWeight)
				return servers.get(currentIndex);
		}
	}

	/**
	 * 返回所有服务器的权重的最大公约数
	 * 
	 * @return
	 */
	private int serverGcd() {
		int comDivisor = 0;

		for (int i = 0; i < totalServer - 1; i++) {
			if (comDivisor == 0) {
				comDivisor = gcd(servers.get(i).getWeight(), servers.get(i + 1).getWeight());
			} else {
				comDivisor = gcd(comDivisor, servers.get(i + 1).getWeight());
			}
		}

		return comDivisor;
	}

	/**
	 * 获得服务器中的最大权重
	 * 
	 * @return
	 */
	private int maxWeight() {
		int max = servers.get(0).getWeight();
		int tmp;

		for (int i = 1; i < totalServer; i++) {
			tmp = servers.get(i).getWeight();

			if (max < tmp)
				max = tmp;
		}

		return max;
	}

	/**
	 * 求两个数的最大公约数 4和6最大公约数是2
	 * 
	 * @param num1
	 * @param num2
	 * @return
	 */
	private int gcd(int num1, int num2) {
		BigInteger i1 = new BigInteger(String.valueOf(num1));
		BigInteger i2 = new BigInteger(String.valueOf(num2));

		return i1.gcd(i2).intValue();
	}

	public static void main(String[] args) {
		final WeightRoundRobin wr = new WeightRoundRobin();

		// 非并发情况
		for (int i = 0; i < 100; i++) {
			System.out.println(wr.round());
		}

		System.out.println();
		System.out.println("==========");
		System.out.println();

		final CyclicBarrier b = new CyclicBarrier(30);

		// 并发情况
		for (int i = 0; i < 30; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						b.await();
						System.out.println(Thread.currentThread().getName() + " " + wr.round());
					} catch (InterruptedException | BrokenBarrierException e) {
						e.printStackTrace();
					}
				}
			}, "thread" + i).start();
		}
	}
}
