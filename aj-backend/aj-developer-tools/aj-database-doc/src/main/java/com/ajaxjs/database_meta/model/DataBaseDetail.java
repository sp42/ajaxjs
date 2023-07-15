package com.ajaxjs.database_meta.model;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

import java.util.Map;

@Data
public class DataBaseDetail implements IBaseModel {
    /**
     * 数据库基本信息
     */
    private Map<String, String> basicInfo;

    /**
     * 版本
     */
    private String version;

    /**
     * MYSQL 安装目录
     */
    private String mySqlHome;

    /**
     * 实际位置
     */
    private String basedir;

    /**
     * MySQL 数据存放的位置
     */
    private String dataDir;

    /**
     * 数据库 大小简介
     */
    private Map<String, String> dbSize;

    /**
     * 数据库编码信息
     */
    private Map<String, String> charMap;

    /**
     * 错误日志
     */
    private Map<String, String> logError;

    /**
     * 二进制日志
     */
    private Map<String, String> logBin;

    /**
     * 通用日志
     */
    private Map<String, String> generalLog;

    /**
     * 慢查询日志
     */
    private Map<String, String> slowQueryLog;

    /**
     * 最大连接数
     */
    private Map<String, String> maxConnection;

    /**
     * 线程数
     */
    private Map<String, String> threads;

    /**
     * table lock
     */
    private Map<String, String> tableLock;

    private Map<String, String> variables;
}
