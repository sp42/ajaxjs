package com.ajaxjs.cache.expiry_map;

import lombok.Data;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Data
public class DelayData<T> implements Delayed {
    private T data;

    //到期时间
    private long expire;

    public DelayData(T data, long expire) {
        this.data = data;
        this.expire = expire;
    }

    //如果返回小于0就代表过期了
    @Override
    public long getDelay(TimeUnit unit) {
        //expire是过期时的时间
        long diffTime = expire - System.currentTimeMillis();
        return unit.convert(diffTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return (int) (this.expire - ((DelayData<T>) o).getExpire());
    }
}
