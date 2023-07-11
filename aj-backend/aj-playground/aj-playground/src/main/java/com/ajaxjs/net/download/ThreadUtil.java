package com.ajaxjs.net.download;

import java.util.concurrent.ArrayBlockingQueue;

import java.util.concurrent.ExecutorService;

import java.util.concurrent.ThreadFactory;

import java.util.concurrent.ThreadPoolExecutor;

import java.util.concurrent.TimeUnit;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadUtil {
    /**
     * 创建批量下载线程池
     *
     * @param threadSize 下载线程数
     * @return ExecutorService
     */
    public static ExecutorService buildDownloadBatchThreadPool(int threadSize) {
        int keepAlive = 0;
        String prefix = "download-batch";
        ThreadFactory factory = ThreadUtil.buildThreadFactory(prefix);

        return new ThreadPoolExecutor(threadSize, threadSize, keepAlive, TimeUnit.SECONDS, new ArrayBlockingQueue<>(threadSize), factory);
    }

    /**
     * 创建自定义线程工厂
     *
     * @param prefix 名称前缀
     * @return ThreadFactory
     */
    public static ThreadFactory buildThreadFactory(String prefix) {
        return new CustomThreadFactory(prefix);
    }

    /**
     * 自定义线程工厂
     */
    public static class CustomThreadFactory implements ThreadFactory {
        private String threadNamePrefix;

        private AtomicInteger counter = new AtomicInteger(1);

        /**
         * 自定义线程工厂
         *
         * @param threadNamePrefix 工厂名称前缀
         */
        CustomThreadFactory(String threadNamePrefix) {
            this.threadNamePrefix = threadNamePrefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            String threadName = threadNamePrefix + "-t" + counter.getAndIncrement();

            return new Thread(r, threadName);
        }
    }

}