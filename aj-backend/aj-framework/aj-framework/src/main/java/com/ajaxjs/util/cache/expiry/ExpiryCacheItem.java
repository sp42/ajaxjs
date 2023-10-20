package com.ajaxjs.util.cache.expiry;

import com.ajaxjs.util.cache.CacheItem;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class ExpiryCacheItem<V> extends CacheItem<V> implements Delayed {
    public ExpiryCacheItem(V data, long expire) {
        super(data, expire);
    }

    @Override
    public long getDelay(TimeUnit unit) {    // 如果返回小于0就代表过期了
        long diffTime = getExpire() - System.currentTimeMillis();
        return unit.convert(diffTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        ExpiryCacheItem<V> _o = (ExpiryCacheItem<V>) o;

        return (int) (getExpire() - _o.getExpire());
    }
}
