package com.ajaxjs.developertools.monitor.model.tomcat;

import com.ajaxjs.framework.IBaseModel;

public class ThreadPool implements IBaseModel {
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
