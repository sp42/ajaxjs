package com.ajaxjs.tomcat;

import java.io.IOException;
import java.lang.management.MemoryUsage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

/**
 * 原来大厂都是这样监控Tomcat性能的，废话不多说，直接开干
 * 
 * https://blog.csdn.net/l1028386804/article/details/128036310
 * 
 * @author liuyazhuang
 * @date 2016-05-31
 */
public class Jmx {

	/**
	 * main方法
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String jmxURL = "service:jmx:rmi:///jndi/rmi://127.0.0.1:8999/jmxrmi";

		try {
			JMXServiceURL serviceURL = new JMXServiceURL(jmxURL);

			Map<String, String[]> map = new HashMap<>();
			String[] credentials = new String[] { "monitorRole", "tomcat" };
			map.put("jmx.remote.credentials", credentials);

			JMXConnector connector = JMXConnectorFactory.connect(serviceURL, map);
			MBeanServerConnection mbsc = connector.getMBeanServerConnection();

			// 端口最好是动态取得
			ObjectName threadObjName = new ObjectName("Catalina:type=ThreadPool,name=http-8080");
			MBeanInfo mbInfo = mbsc.getMBeanInfo(threadObjName);

			String attrName = "currentThreadCount";// tomcat的线程数对应的属性值
			MBeanAttributeInfo[] mbAttributes = mbInfo.getAttributes();
			System.out.println("currentThreadCount:" + mbsc.getAttribute(threadObjName, attrName));

			// heap
			for (int j = 0; j < mbsc.getDomains().length; j++)
				System.out.println("###########" + mbsc.getDomains()[j]);

			Set<ObjectInstance> MBeanset = mbsc.queryMBeans(null, null);
			System.out.println("MBeanset.size() : " + MBeanset.size());
			Iterator<ObjectInstance> MBeansetIterator = MBeanset.iterator();

			while (MBeansetIterator.hasNext()) {
				ObjectInstance objectInstance = (ObjectInstance) MBeansetIterator.next();
				ObjectName objectName = objectInstance.getObjectName();
				String canonicalName = objectName.getCanonicalName();
				System.out.println("canonicalName : " + canonicalName);

				if (canonicalName.equals("Catalina:host=localhost,type=Cluster")) {
					// Get details of cluster MBeans
					System.out.println("Cluster MBeans Details:");
					System.out.println("=========================================");
					// getMBeansDetails(canonicalName);
					String canonicalKeyPropList = objectName.getCanonicalKeyPropertyListString();
				}
			}

			// ------------------------- system ----------------------
			ObjectName runtimeObjName = new ObjectName("java.lang:type=Runtime");
			System.out.println("厂商:" + (String) mbsc.getAttribute(runtimeObjName, "VmVendor"));
			System.out.println("程序:" + (String) mbsc.getAttribute(runtimeObjName, "VmName"));
			System.out.println("版本:" + (String) mbsc.getAttribute(runtimeObjName, "VmVersion"));
			Date starttime = new Date((Long) mbsc.getAttribute(runtimeObjName, "StartTime"));
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			System.out.println("启动时间:" + df.format(starttime));

			Long timespan = (Long) mbsc.getAttribute(runtimeObjName, "Uptime");
			System.out.println("连续工作时间:" + formatTimeSpan(timespan));
			// ------------------------ JVM -------------------------
			// 堆使用率
			ObjectName heapObjName = new ObjectName("java.lang:type=Memory");
			MemoryUsage heapMemoryUsage = MemoryUsage.from((CompositeDataSupport) mbsc.getAttribute(heapObjName, "HeapMemoryUsage"));
			long maxMemory = heapMemoryUsage.getMax();// 堆最大
			long commitMemory = heapMemoryUsage.getCommitted();// 堆当前分配
			long usedMemory = heapMemoryUsage.getUsed();
			System.out.println("heap:" + (double) usedMemory * 100 / commitMemory + "%");// 堆使用率

			MemoryUsage nonheapMemoryUsage = MemoryUsage.from((CompositeDataSupport) mbsc.getAttribute(heapObjName, "NonHeapMemoryUsage"));
			long noncommitMemory = nonheapMemoryUsage.getCommitted();
			long nonusedMemory = heapMemoryUsage.getUsed();
			System.out.println("nonheap:" + (double) nonusedMemory * 100 / noncommitMemory + "%");

			ObjectName permObjName = new ObjectName("java.lang:type=MemoryPool,name=Perm Gen");
			MemoryUsage permGenUsage = MemoryUsage.from((CompositeDataSupport) mbsc.getAttribute(permObjName, "Usage"));
			long committed = permGenUsage.getCommitted();// 持久堆大小
			long used = heapMemoryUsage.getUsed();//
			System.out.println("perm gen:" + (double) used * 100 / committed + "%");// 持久堆使用率

			Info info = new Info();
			info.session = getSession(mbsc);
			info.threadPool = getThreadList(mbsc);

			// ----------------- Thread Pool ----------------

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static List<Session> getSession(MBeanServerConnection mbsc) {
		List<Session> list = new ArrayList<>();

		try {
			ObjectName managerObjName = new ObjectName("Catalina:type=Manager,*");
			Set<ObjectName> s = mbsc.queryNames(managerObjName, null);

			for (ObjectName obj : s) {
				System.out.println("应用名:" + obj.getKeyProperty("path"));
				ObjectName objname = new ObjectName(obj.getCanonicalName());

				Session session = new Session();
				session.maxActiveSessions = (int) mbsc.getAttribute(objname, "maxActiveSessions");
				session.activeSessions = (int) mbsc.getAttribute(objname, "activeSessions");
				session.sessionCounter = (int) mbsc.getAttribute(objname, "sessionCounter");

				list.add(session);
			}
		} catch (MalformedObjectNameException | IOException | AttributeNotFoundException | InstanceNotFoundException | MBeanException | ReflectionException e) {
			e.printStackTrace();
		}

		return list;
	}

	private static List<ThreadPool> getThreadList(MBeanServerConnection mbsc) {
		List<ThreadPool> list = new ArrayList<>();
		try {
			ObjectName threadpoolObjName = new ObjectName("Catalina:type=ThreadPool,*");
			Set<ObjectName> s2 = mbsc.queryNames(threadpoolObjName, null);

			for (ObjectName obj : s2) {
				System.out.println("端口名:" + obj.getKeyProperty("name"));
				ObjectName objname = new ObjectName(obj.getCanonicalName());

				ThreadPool pool = new ThreadPool();
				pool.maxThreads = (int) mbsc.getAttribute(objname, "maxThreads");
				pool.currentThreadCount = (int) mbsc.getAttribute(objname, "currentThreadCount");
				pool.currentThreadsBusy = (int) mbsc.getAttribute(objname, "currentThreadsBusy");

			}
		} catch (MalformedObjectNameException | IOException | AttributeNotFoundException | InstanceNotFoundException | MBeanException | ReflectionException e) {
			e.printStackTrace();
		}

		return list;
	}

	public static class Info {
		public List<Session> session;

		public List<ThreadPool> threadPool;
	}

	public static class Session {
		/**
		 * 活动会话数
		 */
		public int sessionCounter;

		/**
		 * 会话数
		 */
		public int activeSessions;

		/**
		 * 最大会话数
		 */
		public int maxActiveSessions;
	}

	public static class ThreadPool {
		/**
		 * 最大线程数
		 */
		public int maxThreads;

		/**
		 * 当前线程数
		 */
		public int currentThreadCount;

		/**
		 * 繁忙线程数
		 */
		public int currentThreadsBusy;

	}

	static String formatTimeSpan(long span) {
		long minseconds = span % 1000;

		span = span / 1000;
		long seconds = span % 60;

		span = span / 60;
		long mins = span % 60;

		span = span / 60;
		long hours = span % 24;

		span = span / 24;
		long days = span;

		try (Formatter formatter = new Formatter();) {
			return formatter.format("%1$d天 %2$02d:%3$02d:%4$02d.%5$03d", days, hours, mins, seconds, minseconds).toString();
		}
	}
}
