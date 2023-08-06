package com.ajaxjs.developertools.monitor.model.system;

/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

/**
 * 系统内存信息
 */
@Data
public class MemoryInfo implements IBaseModel {
    /**
     * 总计
     */
    private String total;

    /**
     * 已使用
     */
    private String used;

    /**
     * 未使用
     */
    private String free;

    /**
     * 使用率
     */
    private double usePercent;
}
