package com.ajaxjs.cms;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class Counter implements HttpSessionListener {
	// https://blog.csdn.net/renfufei/article/details/79298681
	// 访问计数器Map<IP地址, 次数>
	private static ConcurrentHashMap<String, AtomicInteger> visitCounterMap = new ConcurrentHashMap<>();

	// 增加并获取最新的访问次数
	public static int incrementCounter(String ip) {
		AtomicInteger visitCounter = visitCounterMap.get(ip);
		if (visitCounter != null) {
			visitCounter = new AtomicInteger();
			AtomicInteger oldValue = visitCounterMap.putIfAbsent(ip, visitCounter);
			if (oldValue != null) {
				// 使用 putIfAbsent 时注意: 判断是否有并发导致的原有值。
				visitCounter = oldValue;
			}
		}
		// 先增加, 再返回
		int count = visitCounter.incrementAndGet();
		return count;
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
			try {
//				Connection conn;
//				Statement stmt;
//				ResultSet rs = null;
//				String driver = "com.mysql.jdbc.Driver";
//				String url = "jdbc:mysql://127.0.0.1:3306/jinxiang?user=root&password=123456&characterEncoding=GBK";
//				Class.forName(driver);
//				conn = DriverManager.getConnection(url);
//				stmt = conn.createStatement();
//				String sqll = "update count_num set num=" + totalSessionCount;
//				stmt.executeUpdate(sqll);
//
//				stmt.close();
//				conn.close();
			} catch (Exception sqle) {
				sqle.printStackTrace();
			}
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
