/**
 * Copyright Sp42 frank@ajaxjs.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 处理对象一些相关函数
 */
public class ObjectHelper {
    /**
     * 创建一个新的 HashMap
     *
     * @param k1 键1
     * @param v1 值1
     * @return 新创建的HashMap
     */
    public static <K, V> Map<K, V> hashMap(K k1, V v1) {
        Map<K, V> map = new HashMap<>();

        map.put(k1, v1);

        return map;
    }

    /**
     * 创建一个新的 HashMap
     *
     * @param k1 键1
     * @param v1 值1
     * @param k2 键2
     * @param v2 值2
     * @return 新创建的HashMap
     */
    public static <K, V> Map<K, V> hashMap(K k1, V v1, K k2, V v2) {
        Map<K, V> map = new HashMap<>();

        map.put(k1, v1);
        map.put(k2, v2);

        return map;
    }

    /**
     * 创建一个新的 HashMap
     *
     * @param k1 键1
     * @param v1 值1
     * @param k2 键2
     * @param v2 值2
     * @param k3 键3
     * @param v3 值3
     * @return 新创建的 HashMap
     */
    public static <K, V> Map<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3) {
        Map<K, V> map = new HashMap<>();

        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);

        return map;
    }

    /**
     * 挂起当前线程
     *
     * @param timeout  挂起的时长
     * @param timeUnit 时长单位
     */
    public static void sleep(Number timeout, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(timeout.longValue());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 基于一个数值和时间单位来使当前线程休眠指定的时间。
     *
     * @param timeout 要休眠的数值，以秒为单位
     */
    public static void sleep(Number timeout) {
        sleep(timeout, TimeUnit.SECONDS);
    }


    private static ScheduledExecutorService scheduledThreadPool;

    /**
     * 设置一个定时任务
     *
     * @param command 待执行任务
     * @param delay   延迟时间，单位秒
     * @return 任务结果
     */
    public static ScheduledFuture<?> setTimeout(Runnable command, long delay) {
        if (scheduledThreadPool == null)
            scheduledThreadPool = Executors.newScheduledThreadPool(5);

        return scheduledThreadPool.schedule(command, delay, TimeUnit.SECONDS);
    }

    /**
     * 开启一个轮询任务
     *
     * @param job          待执行任务
     * @param initialDelay 初始延迟时间，单位分钟
     * @param period       轮询间隔，单位分钟
     */
    public static void timeout(Runnable job, int initialDelay, int period) {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(job, 1, 1, TimeUnit.MINUTES);
    }
}
