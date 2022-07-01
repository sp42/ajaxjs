package com.ajaxjs.datadict;

import java.util.Date;

import com.ajaxjs.framework.IBaseModel;

/**
 * 数据字典 
 * 
 * @author Frank Cheung
 * @date 2021-11-07
 */
public class DataDict implements IBaseModel {
    /**
     * 主键 id，自增
     */
    private Long id;

    /**
     * 名称、自定义编码、相当于 key。可选的
     */
    private String name;

    /**
     * 值
     */
    private String value;

    /**
     * 简介、描述
     */
    private String desc;

    /**
     * 父 id
     */
    private Long parentId;

    /**
     * 类型 id
     */
    private Long type;

    /**
     * 顺序、序号
     */
    private Integer sortNo;

    /**
     * 数据字典：状态
     */
    private Integer stat;

    /**
     * 唯一 id，通过 uuid 生成不重复 id
     */
    private Long uid;

    /**
     * 租户 id。0 = 不设租户
     */
    private Long tenantId;

    /**
     * 创建者 id
     */
    private Long createByUser;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 修改时间
     */
    private Date updateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public Integer getStat() {
        return stat;
    }

    public void setStat(Integer stat) {
        this.stat = stat;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getCreateByUser() {
        return createByUser;
    }

    public void setCreateByUser(Long createByUser) {
        this.createByUser = createByUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}