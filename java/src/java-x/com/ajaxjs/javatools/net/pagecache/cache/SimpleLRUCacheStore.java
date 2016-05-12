package com.ajaxjs.javatools.net.pagecache.cache;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.ajaxjs.util.pagecache.config.PageCacheGlobalConfig;
import com.ajaxjs.util.pagecache.config.PageContentModel;

public class SimpleLRUCacheStore implements CacheStore {
	private int cacheSize = 10000;

	private Map<String, PageContentModel> urlKeyPageCache;

	public void init(int cacheSize) {
		this.cacheSize = cacheSize;
		urlKeyPageCache = new LruCache<>(this.cacheSize);
		// Thread thread = new Thread(new ExpirePageCheckTread());
		// thread.setName("ExpirePageCheckTread");
		// thread.start();
	}

	@Override
	public void init(Map<String, String> initParams) {
		String cacheSize = initParams.get("cachesize");
		init(Integer.parseInt(cacheSize));
	}

	@Override
	public void put(String key, String value, String urlPattern) {
		PageContentModel model = new PageContentModel();
		model.setPageContent(value);
		model.setLastModified(System.currentTimeMillis());
		model.setCacheTime(PageCacheGlobalConfig.getCacheExpiredTime(urlPattern));
		urlKeyPageCache.put(key, model);
	}

	@Override
	public String get(String key) {
		PageContentModel model = urlKeyPageCache.get(key);
		if (model != null) {
			if (System.currentTimeMillis() - model.getLastModified() > model.getCacheTime() * 1000) {
				urlKeyPageCache.remove(key);
				return null;
			}
			return model.getPageContent();
		}
		return null;
	}

	public class ExpirePageCheckTread implements Runnable {
		@Override
		public void run() {
			while (true) {
				Map<String, PageContentModel> tmpUrlPageMap = SimpleLRUCacheStore.this.urlKeyPageCache;
				Iterator<Entry<String, PageContentModel>> iter = tmpUrlPageMap.entrySet().iterator();
				
				while (iter.hasNext()) {
					Entry<String, PageContentModel> entry = iter.next();
					if (System.currentTimeMillis() - entry.getValue().getLastModified() > entry.getValue().getCacheTime() * 1000) {
						tmpUrlPageMap.remove(entry.getKey());
					}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
			}
		}

	}

	public class LruCache<K, V> extends LinkedHashMap<K, V> {
		private static final long serialVersionUID = -3923317621052085848L;
		private int maxCapacity;
		private Lock lock = new ReentrantLock();

		public LruCache(int maxCapacity) {
			super(maxCapacity + 1, 1f, true);// make it do not rehash ever,
			this.maxCapacity = maxCapacity;
		}

		@Override
		protected boolean removeEldestEntry(Entry<K, V> eldest) {
			return size() > this.maxCapacity;
		}

		@Override
		public V put(K key, V value) {
			try {
				lock.lock();
				return super.put(key, value);
			} finally {
				lock.unlock();
			}
		}

		@Override
		public V get(Object key) {
			try {
				lock.lock();
				return super.get(key);
			} finally {
				lock.unlock();
			}
		}

		@Override
		public V remove(Object key) {
			try {
				lock.lock();
				return super.remove(key);
			} finally {
				lock.unlock();
			}
		}
	}
}
