/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.ajaxjs.developertools.monitor.oshi.model;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

/**
 * 操作系统信息
 */
@Data
public class SysInfo implements IBaseModel {

    /**
     * 系统名称
     */
    private String name;

    /**
     * 系统 ip
     */
    private String ip;

    /**
     * 操作系统
     */
    private String osName;

    /**
     * 系统架构
     */
    private String osArch;

    /**
     * 项目路径
     */
    private String userDir;

}
