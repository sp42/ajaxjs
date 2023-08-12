package com.ajaxjs.developertools.monitor.model.tomcat;

import com.ajaxjs.framework.IBaseModel;

import java.util.List;

public class TomcatInfo implements IBaseModel {
    public List<Session> session;

//    public List<ThreadPool> threadPool;

    public ThreadPool threadPool;
    public SystemInfo systemInfo;

    public JvmInfo jvmInfo;
}
