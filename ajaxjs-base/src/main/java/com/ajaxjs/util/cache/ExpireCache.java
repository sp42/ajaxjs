package com.ajaxjs.util.cache;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * 带过期时间的内存缓存。 参考：https://blog.csdn.net/wab719591157/article/details/78029861
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class ExpireCache extends ConcurrentHashMap<String, ExpireCacheData<Object>> {
	private static final long serialVersionUID = 3850668473354271847L;

	/**
	 * 单例，外界一般调用该对象的方法
	 */
	public final static ExpireCache CACHE = new ExpireCache();

	static {
		// 缓存自动失效
		// 周期性执行任务，另外可以考虑 ScheduledExecutorService
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				for (String key : CACHE.keySet()) {
					Object obj = CACHE.get(key);

					/*
					 * 超时了，干掉！另外：有 load 的总是不会返回 null，所以这里不用考虑有 load 的 ExpireCacheData。 而且有 load
					 * 的也不应该被 kill
					 */
					if (obj == null)
						CACHE.remove(key);
				}
			}
		}, 0, 10 * 1000); // 10 秒钟清理一次
	}

	/**
	 * 获取缓存中的数据
	 * 
	 * @param key 缓存 KEY
	 * @return 缓存的数据，找不到返回或者过期则直接返回 null
	 */
	public Object get(String key) {
		ExpireCacheData<Object> data = super.get(key);
		long now = new Date().getTime();

		if (data != null && (data.expire <= 0 || data.saveTime >= now))
			return data.data;
		else if (data.load != null) {
			Object value = data.load.get();
			data.data = value;
			data.saveTime = now + data.expire; // 重新算过存活时间

			return value;
		}

		return null;
	}

	/**
	 * 获取缓存中的数据（避免强类型转换的麻烦）
	 * 
	 * @param <T> 缓存的类型
	 * @param key 缓存 KEY
	 * @param clz 缓存的类型
	 * @return 缓存的数据，找不到返回或者过期则直接返回 null
	 */
	@SuppressWarnings({ "unchecked" })
	public <T> T get(String key, Class<T> clz) {
		Object obj = get(key);
		return obj == null ? null : (T) obj;
	}

	/**
	 * 设置 key 与 cache
	 * 
	 * @param key    缓存 KEY
	 * @param data   要缓存的数据
	 * @param expire 过期时间，单位是秒
	 */
	public void put(String key, Object data, int expire) {
		put(key, new ExpireCacheData<>(data, expire));
	}

	/**
	 * 设置 key 与 cache
	 * 
	 * @param key    缓存 KEY
	 * @param data   要缓存的数据
	 * @param expire 过期时间，单位是秒
	 * @param load   数据装载器，如果缓存中没有数据或者已经过期，则调用数据装载器加载最新的数据并且加入缓存，并返回
	 */
	public void put(String key, Object data, int expire, Supplier<Object> load) {
		put(key, new ExpireCacheData<>(data, expire, load));
	}

}
