package com.ajaxjs.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 简单统计Java方法中每行代码的执行时间
 * 
 * 在需要调试的方法中隔几行插入Timer.set(数字或字符)方法，程序结束前调用Timer.print()方法就行了。
 * 因为目的只是排查方法执行时间，调试完成后可以将set()方法注释掉，然后利用Eclipse的查错功能将所有Timer.set()方法找出来删除。
 * 
 * @author https://my.oschina.net/drinkjava2/blog/1622179
 *
 */
public class Timer {
	private static String lastMark = "start";
	private static String TPL = "%20s, Total times:%20s,  Repeat times:%20s, Avg times:%20s ";

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
		for (String key : timeMap.keySet()) {
			Long value = timeMap.get(key), _value = timeHappenCount.get(key);
			String.format(TPL, key, value, _value, value / _value);
		}
	}
}
