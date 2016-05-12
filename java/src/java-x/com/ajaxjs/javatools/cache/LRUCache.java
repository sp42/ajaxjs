package com.ajaxjs.javatools.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * 最近最少使用算法 Window - Preferences - Java - Code Style - Code Templates
 * 最近最少使用算法”（LRU算法），它是将最近一段时间内最少被访问过的行淘汰出局。
 * 因此需要为每行设置一个计数器，LRU算法是把命中行的计数器清零，其他各行计数器加1。
 * 当需要替换时淘汰行计数器计数值最大的数据行出局。
 * 这是一种高效、科学的算法，其计数器清零过程可以把一些频繁调用后再不需要的数据淘汰出Cache，提高Cache的利用率。
 * @author 刘跃清
 */
public class LRUCache {
	protected HashMap<Object, Object> lruCache = new HashMap<>(2);

	// 可操作的最大使用次数
	protected int MAX_INTEGER_NUMBER = 2147483647;

	// 缓存中保存的数大对象数目
	protected int max_object_num = 1000;

	public LRUCache(){}

	public LRUCache(int maxObjectNum) {
		max_object_num = maxObjectNum;
	}

	/**
	 * 增加对象到缓存中
	 * @param key
	 * @param value
	 */
	public Object put(Object key, Object value) {
		CacheObject newValue = new CacheObject(value);
		if (lruCache.size() >= max_object_num) removeLease();

		return lruCache.put(key, newValue);
	}

	/**
	 * 使用key来获取对象
	 * 
	 * @param key
	 * @return
	 */
	public Object get(Object key) {
		CacheObject object = (CacheObject) lruCache.get(key);
		if (object == null) return null;

		// 根据LRU算法原则，将命中的对象计算器0，将其他对象的计算值加1
		Set<Object> set = lruCache.keySet();
		Iterator<Object> iter = set.iterator();
		Object keyObject = null;
		CacheObject cacheObject = null;
		
		while (iter.hasNext()) {
			keyObject = iter.next();
			cacheObject = (CacheObject) lruCache.get(keyObject);
			cacheObject.setUsetimes(cacheObject.getUsetimes() + 1);
		}
		object.setUsetimes(0);

		return object != null ? object.getValue() : null;
	}

	public boolean containsKey(Object key) {
		return lruCache.containsKey(key);
	}

	public void clear() {
		lruCache.clear();
	}

	public int size() {
		return lruCache.size();
	}

	public boolean isEmpty() {
		return lruCache.isEmpty();
	}

	public boolean containsValue(Object value) {
		return lruCache.containsKey(value);
	}

	/**
	 * 移除使用最少的对象
	 */
	public void removeLease() {
		Object leaseUseObjectKey = null;
		int usetimes = 0;

		Set<Object> set = lruCache.keySet();
		Iterator<Object> iter = set.iterator();
		
		while (iter.hasNext()) {
			Object keyObject = iter.next();
			CacheObject object = (CacheObject) lruCache.get(keyObject);
			if (object.getUsetimes() > usetimes) {
				usetimes = object.getUsetimes();
				leaseUseObjectKey = keyObject;
			}
		}
		lruCache.remove(leaseUseObjectKey);
	}

	public Set<Object> keySet() {
		return lruCache.keySet();
	}

	/**
	 * 移除使用最频繁的对象
	 */
	public void removeMost() {
		Object leaseUseObjectKey = null;
		int usetimes = MAX_INTEGER_NUMBER;

		Set<Object> set = lruCache.keySet();
		Iterator<Object> iter = set.iterator();
		while (iter.hasNext()) {
			Object keyObject = iter.next();
			CacheObject object = (CacheObject) lruCache.get(keyObject);
			if (object.getUsetimes() < usetimes) {
				usetimes = object.getUsetimes();
				leaseUseObjectKey = keyObject;
			}
		}
		lruCache.remove(leaseUseObjectKey);
	}

	/**
	 * 移除最早置入缓存的对象
	 */
	public void removeEarly() {
		Object leaseUseObjectKey = null;
		long time = System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000;

		Set<Object> set = lruCache.keySet();
		Iterator<Object> iter = set.iterator();
		while (iter.hasNext()) {
			Object keyObject = iter.next();
			CacheObject object = (CacheObject) lruCache.get(keyObject);
			if (object.getPushtime() < time) {
				time = object.getPushtime();
				leaseUseObjectKey = keyObject;
			}
		}
		lruCache.remove(leaseUseObjectKey);
	}

	/**
	 * 移除最迟放入的对象
	 */
	public void removeLater() {
		Object leaseUseObjectKey = null;
		long time = -1;

		Set<Object> set = lruCache.keySet();
		Iterator<Object> iter = set.iterator();
		while (iter.hasNext()) {
			Object keyObject = iter.next();
			CacheObject object = (CacheObject) lruCache.get(keyObject);
			if (object.getPushtime() > time) {
				time = object.getPushtime();
				leaseUseObjectKey = keyObject;
			}
		}
		lruCache.remove(leaseUseObjectKey);
	}

	/**
	 * 删除某个键值及对应对象
	 * 
	 * @param key
	 */
	public void remove(Object key) {
		lruCache.remove(key);
	}

	public static void main(String[] args) {
		LRUCache lru = new LRUCache(4);
		lru.put("a", "The A String");
		lru.put("b", "The B String");
		lru.put("d", "The D String");
		lru.put("c", "The C String");

		System.out.println(lru.toString());

		lru.get("a");
		lru.get("b");
		lru.get("d");
		lru.get("a");
		lru.get("b");
		lru.get("d");
		lru.put("e", "The E String");
		lru.get("e");
		lru.get("e");
		lru.get("e");
		lru.get("e");
		System.out.println(lru.toString());
	}

	public String toString() {
		StringBuffer strBf = new StringBuffer(10);
		Set<Object> set1 = lruCache.keySet();
		Iterator<Object> iter1 = set1.iterator();
		while (iter1.hasNext()) {
			Object key = iter1.next();
			strBf.append(key + "=");
			strBf.append(lruCache.get(key));
			strBf.append("/n");
		}
		return strBf.toString();
	}

}
