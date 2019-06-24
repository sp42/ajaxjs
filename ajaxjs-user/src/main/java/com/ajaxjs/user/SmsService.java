package com.ajaxjs.user;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SmsService {
	private static ConcurrentMap<String, Long> map = new ConcurrentHashMap<>();

	static long cleanMapInterval = 6000;
	static long sendInterval = 1000 * 60;

	/**
	 * 是否可以发送
	 * 
	 * @param id
	 *            手机号或者 ip
	 * @return 如果成功将发送时间修改为当前时间, 则返回true. 否则返回false
	 */
	public static boolean canSend(String id) {
		long now = System.currentTimeMillis();
		Long sent = map.putIfAbsent(id, now);

		if (sent == null) // 第一次发送短信, 而且现在的时间也已经放到了map中, 可以发送
			return true;

		long nextCanSendTime = sent + sendInterval;
		if (now < nextCanSendTime)
			return false;

		return map.replace(id, sent, now);
	}

	static {
		// clear map
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				long expire = System.currentTimeMillis() - sendInterval;

				for (String key : map.keySet()) {
					Long sendTime = map.get(key);

					if (sendTime < expire)
						map.remove(key, sendTime);
				}
			}
		}, 3000, cleanMapInterval);
	}

}
