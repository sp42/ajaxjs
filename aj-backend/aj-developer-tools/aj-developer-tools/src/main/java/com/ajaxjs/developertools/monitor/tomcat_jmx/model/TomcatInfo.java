package com.ajaxjs.developertools.monitor.tomcat_jmx.model;

import com.ajaxjs.framework.IBaseModel;

import java.util.List;

public class TomcatInfo implements IBaseModel {
    public List<Session> session;

//    public List<ThreadPool> threadPool;

    public ThreadPool threadPool;
    public SystemInfo systemInfo;

    public JvmInfo jvmInfo;
}
