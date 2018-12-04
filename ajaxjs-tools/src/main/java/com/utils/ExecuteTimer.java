package com.utils;

import java.util.*;
import java.util.Map.Entry;

/**
 * 代码工具类
 * 
 * @author https://my.oschina.net/drinkjava2/blog/1622179
 *
 */
public class ExecuteTimer {
	private static String lastMark = "start";
	private static long lastTime = System.nanoTime();
	private static final Map<String, Long> timeMap = new LinkedHashMap<>();
	private static final Map<String, Long> timeHappenCount = new LinkedHashMap<>();

	public static void set(int mark) {
		set("" + mark);
	}

	public static void set(String mark) {
		long thisTime = System.nanoTime();
		String key = lastMark + "->" + mark;
		Long lastSummary = timeMap.get(key);
		if (lastSummary == null)
			lastSummary = 0l;

		timeMap.put(key, System.nanoTime() - lastTime + lastSummary);
		Long lastCount = timeHappenCount.get(key);
		if (lastCount == null)
			lastCount = 0L;

		timeHappenCount.put(key, ++lastCount);
		lastTime = thisTime;
		lastMark = mark;
	}

	public static void print() {
		for (Entry<String, Long> entry : timeMap.entrySet()) {
			System.out.println(// NOSONAR
					String.format("%20s, Total times:%20s,  Repeat times:%20s, Avg times:%20s ", entry.getKey(), entry.getValue(), timeHappenCount.get(entry.getKey()),
							entry.getValue() / timeHappenCount.get(entry.getKey())));
		}
	}
}
