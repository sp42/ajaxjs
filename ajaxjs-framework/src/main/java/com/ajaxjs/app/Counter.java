package com.ajaxjs.app;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * https://blog.csdn.net/renfufei/article/details/79298681
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class Counter implements HttpSessionListener {

	/**
	 * 访问计数器Map<IP地址, 次数>
	 */
	private static ConcurrentHashMap<String, AtomicInteger> visitCounterMap = new ConcurrentHashMap<>();

	/**
	 * 增加并获取最新的访问次数
	 * 
	 * @param ip
	 * @return
	 */
	public static int incrementCounter(String ip) {
		AtomicInteger visitCounter = visitCounterMap.get(ip);

		if (visitCounter != null) {
			visitCounter = new AtomicInteger();
			AtomicInteger oldValue = visitCounterMap.putIfAbsent(ip, visitCounter);

			if (oldValue != null)
				visitCounter = oldValue;// 使用 putIfAbsent 时注意: 判断是否有并发导致的原有值。
		}

		return visitCounter.incrementAndGet();// 先增加, 再返回
	}

	// 清除某个IP的访问次数
	public static void clearCounter(String ip) {
		visitCounterMap.remove(ip);
	}

	public static int totalSessionCount;

	public static int currentSessionCount;

	private static int tempCount;

	private static final int MAX = 10;

	private ServletContext context;

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		tempCount++;
		totalSessionCount++;
		currentSessionCount++;

		// 先在内存中保存访问量，只有当访问量达到一定的数量（比如10人）时才写一次数据库
		if (tempCount > MAX) {// 临时访问量大于MAX时，将总访问量存入数据库
			tempCount = 0;

			// 写入数据库
		}

		if (context == null) {// storeInServletContext
			context = event.getSession().getServletContext();
			context.setAttribute("sessionCounter", this);
		}
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		currentSessionCount--;
	}
}
