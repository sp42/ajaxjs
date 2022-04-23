package com.ajaxjs.framework.log;

import java.util.Date;

import com.ajaxjs.framework.IBaseModel;
import com.ajaxjs.sql.annotation.IgnoreDB;

/**
 * @description 操作日志
 * @author Frank Cheung<sp42@qq.com>
 * @date 2021-11-07
 */
public class ServiceLog implements IBaseModel {
	/**
	 * 主键 id，自增
	 */
	private Long id;

	/**
	 * 简介、描述 collate utf8mb4_bin
	 */
	private String name;

	/**
	 * 操作者 id
	 */
	private Long userid;

	/**
	 * 租户 id。0 = 不设租户
	 */
	private Long tenantid;

	/**
	 * 操作者 ip collate utf8mb4_bin
	 */
	private String ip;

	/**
	 * 相关执行的 sql collate utf8mb4_bin
	 */
	private String sql;

	/**
	 * 其他操作内容 collate utf8mb4_bin
	 */
	private String content;

	/**
	 * 操作异常，如果为空表示操作成功 collate utf8mb4_bin
	 */
	private String error;

	/**
	 * 是否操作成功
	 */
	private Boolean done;

	/**
	 * 创建时间
	 */
	private Date createdate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Long getTenantid() {
		return tenantid;
	}

	public void setTenantid(Long tenantid) {
		this.tenantid = tenantid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Boolean isDone() {
		done = error == null || "".equals(error);
		return done;
	}
	
	@IgnoreDB
	public Boolean getDone() {
		done = error == null || "".equals(error);
		return done;
	}

	public void setDone(Boolean done) {
		this.done = done;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}