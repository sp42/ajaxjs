package com.algorithm;

import java.math.BigDecimal;
import java.util.Random;

/**
 * 带权重的随机选择
 * 
 * @author https://www.zifangsky.cn/1545.html
 *
 */
public class WeightRandom {
	/**
	 * 选项数组
	 */
	private Item[] options;

	/**
	 * 权重的临界值
	 */
	private BigDecimal[] criticalWeight;

	private Random rnd;

	public WeightRandom(Item[] options) {
		if (options == null || options.length < 1)
			throw new IllegalArgumentException("选项数组存在异常！");

		this.options = options;
		rnd = new Random();
		init();
	}

	/**
	 * 随机函数
	 */
	public String nextItem() {
		double randomValue = rnd.nextDouble();
		// 查找随机值所在区间
		int index = this.searchIndex(randomValue);

		return this.options[index].getName();
	}

	/**
	 * 查找随机值所在区间
	 * 
	 * @param randomValue
	 * @return
	 */
	private int searchIndex(double randomValue) {
		BigDecimal rndValue = new BigDecimal(randomValue);
		int high = criticalWeight.length - 1;
		int low = 0;
		int median = (high + low) / 2;

		BigDecimal medianValue = null;

		while (median != low && median != high) {
			medianValue = this.criticalWeight[median];

			if (rndValue.compareTo(medianValue) == 0)
				return median;
			else if (rndValue.compareTo(medianValue) > 0) {
				low = median;
				median = (high + low) / 2;
			} else {
				high = median;
				median = (high + low) / 2;
			}
		}

		return median;
	}

	/**
	 * 初始化
	 */
	private void init() {
		// 总权重
		BigDecimal sumWeights = BigDecimal.ZERO;
		// 权重的临界值
		this.criticalWeight = new BigDecimal[options.length + 1];

		// 1. 计算总权重
		for (Item item : this.options)
			sumWeights = sumWeights.add(new BigDecimal(item.getWeight()));

		// 2. 计算每个选项的临界值
		BigDecimal tmpSum = BigDecimal.ZERO;
		this.criticalWeight[0] = tmpSum;

		for (int i = 0; i < this.options.length; i++) {
			tmpSum = tmpSum.add(new BigDecimal(options[i].getWeight()));
			criticalWeight[i + 1] = tmpSum.divide(sumWeights, 2, BigDecimal.ROUND_HALF_UP);
		}
	}

	/**
	 * 需要随机的 item
	 */
	public static class Item {
		/**
		 * 名称
		 */
		private String name;

		/**
		 * 权重
		 */
		private double weight;

		public Item(String name, double weight) {
			this.name = name;
			this.weight = weight;
		}

		public String getName() {
			return name;
		}

		public double getWeight() {
			return weight;
		}
	}
}
