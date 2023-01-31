package com.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * https://www.cnblogs.com/xrq730/p/5154340.html
 * 几种简单的负载均衡算法及其Java代码实现
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public class LoadBalance {

	private static Integer pos = 0;

	/**
	 * 
	 * 轮询法即Round Robin法
	 * 
	 * 由于serverWeightMap中的地址列表是动态的，随时可能有机器上线、下线或者宕机，因此为了避免可能出现的并发问题，方法内部要新建局部变量serverMap，现将serverMap中的内容复制到线程本地，以避免被多个线程修改。这样可能会引入新的问题，复制以后serverWeightMap的修改无法反映给serverMap，也就是说这一轮选择服务器的过程中，新增服务器或者下线服务器，负载均衡算法将无法获知。新增无所谓，如果有服务器下线或者宕机，那么可能会访问到不存在的地址。因此，服务调用端需要有相应的容错处理，比如重新发起一次server选择并调用。
	 * 
	 * 对于当前轮询的位置变量pos，为了保证服务器选择的顺序性，需要在操作时对其加锁，使得同一时刻只能有一个线程可以修改pos的值，否则当pos变量被并发修改，则无法保证服务器选择的顺序性，甚至有可能导致keyList数组越界。
	 * 
	 * 轮询法的优点在于：试图做到请求转移的绝对均衡。
	 * 
	 * 轮询法的缺点在于：为了做到请求转移的绝对均衡，必须付出相当大的代价，因为为了保证pos变量修改的互斥性，需要引入重量级的悲观锁synchronized，这将会导致该段轮询代码的并发吞吐量发生明显的下降。
	 * 
	 * @return
	 */
	public static String RoundRobin() {
		// 重建一个Map，避免服务器的上下线导致的并发问题
		Map<String, Integer> serverMap = new HashMap<>();
		serverMap.putAll(serverWeightMap);

		// 取得Ip地址List
		Set<String> keySet = serverMap.keySet();
		ArrayList<String> keyList = new ArrayList<>();
		keyList.addAll(keySet);

		String server = null;

		synchronized (pos) {
			if (pos > keySet.size())
				pos = 0;

			server = keyList.get(pos);
			pos++;
		}

		return server;
	}

	/**
	 * 通过系统随机函数，根据后端服务器列表的大小值来随机选择其中一台进行访问。由概率统计理论可以得知，随着调用量的增大，其实际效果越来越接近于平均分配流量到每一台后端服务器，也就是轮询的效果。
	 * 
	 * 整体代码思路和轮询法一致，先重建serverMap，再获取到server列表。在选取server的时候，通过Random的nextInt方法取0~keyList.size()区间的一个随机值，从而从服务器列表中随机获取到一台服务器地址进行返回。基于概率统计的理论，吞吐量越大，随机算法的效果越接近于轮询算法的效果。
	 * 
	 * @return
	 */
	public static String Random() {
		// 重建一个Map，避免服务器的上下线导致的并发问题
		Map<String, Integer> serverMap = new HashMap<>();
		serverMap.putAll(serverWeightMap);

		// 取得Ip地址List
		Set<String> keySet = serverMap.keySet();
		ArrayList<String> keyList = new ArrayList<>();
		keyList.addAll(keySet);

		java.util.Random random = new java.util.Random();
		int randomPos = random.nextInt(keyList.size());

		return keyList.get(randomPos);
	}

	// 待路由的Ip列表，Key代表 Ip，Value 代表该 Ip 的权重
	public static HashMap<String, Integer> serverWeightMap = new HashMap<>();

	static {
		serverWeightMap.put("192.168.1.100", 1);
		serverWeightMap.put("192.168.1.101", 1);
		// 权重为4
		serverWeightMap.put("192.168.1.102", 4);
		serverWeightMap.put("192.168.1.103", 1);
		serverWeightMap.put("192.168.1.104", 1);
		// 权重为3
		serverWeightMap.put("192.168.1.105", 3);
		serverWeightMap.put("192.168.1.106", 1);
		// 权重为2
		serverWeightMap.put("192.168.1.107", 2);
		serverWeightMap.put("192.168.1.108", 1);
		serverWeightMap.put("192.168.1.109", 1);
		serverWeightMap.put("192.168.1.110", 1);
	}

}
