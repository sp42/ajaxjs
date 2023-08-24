package com.ajaxjs.developertools.monitor.oshi.model;

/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

/**
 * 网络带宽信息
 */
@Data
public class NetIoInfo  implements IBaseModel {
    /**
     * 每秒钟接收的数据包,rxpck/s
     */
    private String rxpck;

    /**
     * 每秒钟发送的数据包,txpck/s
     */
    private String txpck;

    /**
     * 每秒钟接收的KB数,rxkB/s
     */
    private String rxbyt;

    /**
     * 每秒钟发送的KB数,txkB/s
     */
    private String txbyt;


}
