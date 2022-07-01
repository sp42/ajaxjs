package com.ajaxjs.tag;

import java.util.Date;

public class TagInfo {
    /**
     *  主键 id，自增
     */
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTagIndex() {
        return tagIndex;
    }

    public void setTagIndex(Integer tagIndex) {
        this.tagIndex = tagIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    /**
     *  标签索引
     */
    private Integer tagIndex;

    /**
     *  名称、自定义编码、相当于 key。可选的
     */
    private String name;

    /**
     *  简介、描述
     */
    private String desc;

    /**
     *  数据字典：状态
     */
    private Integer stat;

    /**
     *  唯一 id，通过 uuid 生成不重复 id
     */
    private Long uid;

    /**
     *  租户 id。0 = 不设租户
     */
    private Long tenantId;

    /**
     *  创建者 id
     */
    private Long createByUser;

    /**
     *  创建时间
     */
    private Date createDate;

    /**
     *  修改时间
     */
    private Date updateDate;
}
