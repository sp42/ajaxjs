package com.ajaxjs.database_meta.model;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 表的详情信息
 *
 * @author Frank Cheung sp42@qq.com
 */
@Data
@Accessors(chain = true)
public class TableDesc implements IBaseModel {
    /**
     * 表名
     */
    private String name;

    /**
     * 存储引擎
     */
    private String engine;

    /**
     * 版本
     */
    private String version;

    /**
     * 行格式
     */
    private String rowFormat;

    /**
     * 行数
     */
    private String rows;

    /**
     * 平均行长度
     */
    private String avgRowLength;

    /**
     * 数据长度
     */
    private String dataLength;

    /**
     * 最大数据长度
     */
    private String maxDataLength;

    /**
     * 索引长度
     */
    private String indexLength;

    /**
     * 空闲空间
     */
    private String dataFree;

    /**
     * 自增值
     */
    private String autoIncrement;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 检查时间
     */
    private String checkTime;

    /**
     * 字符集排序规则
     */
    private String collation;

    /**
     * 校验和
     */
    private String checksum;

    /**
     * 创建选项
     */
    private String createOptions;

    /**
     * 注释
     */
    private String comment;

    /**
     * 数据占用空间（MB）
     */
    private String dataMb;

    /**
     * 索引占用空间（MB）
     */
    private String indexMb;

    /**
     * 总空间占用（MB）
     */
    private String allMb;

    /**
     * 计数
     */
    private String count;
}
