package com.ajaxjs.data_service.model;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.ajaxjs.data_service.DataServiceConstant;

/**
 * 数据服务上下文对象
 *
 * @author Frank Cheung
 */
public class ServiceContext {
	private Map<String, Object> requestParams;

	private DataServiceConstant.CRUD crudType;

	private String uri;

	/**
	 * 操作者 id
	 */
	private Long userId;

	private DataSource datasource;

	private String sql;

	/**
	 * 
	 */
	private Map<String, Object> sqlParam;

	private String[] sqls;

	/**
	 * 
	 */
	private Map<String, Object>[] sqlParams;

	/**
	 * 自定义错误信息，返回给前端
	 */
	private String errMsg;

	/**
	 * 创建数据服务上下文对象
	 * 
	 * @param uri
	 * @param req
	 */
	public ServiceContext(String uri, HttpServletRequest req) {
		setUri(uri);
		setRequest(req);
	}

	/**
	 * 创建数据服务上下文对象的工厂函数
	 * 
	 * @param uri
	 * @param req
	 * @param node
	 * @param params
	 * @return
	 */
	public static ServiceContext factory(String uri, HttpServletRequest req, DataServiceDml node, Map<String, Object> params) {
		ServiceContext ctx = new ServiceContext(uri, req);
		ctx.setNode(node);
		ctx.setDatasource(node.getDataSource());
		ctx.setRequestParams(params);

		return ctx;
	}

	/**
	 * 设置常规参数
	 *
	 * @param node
	 * @param map
	 * @param ds
	 */
	public void set(DataServiceDml node, Map<String, Object> map, DataSource ds) {
		setNode(node);
		setRequestParams(map);
		setDatasource(ds);
	}

	private HttpServletRequest request;

	private DataServiceDml node;

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public Map<String, Object> getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(Map<String, Object> requestParams) {
		this.requestParams = requestParams;
	}

	public DataServiceConstant.CRUD getCrudType() {
		return crudType;
	}

	public void setCrudType(DataServiceConstant.CRUD crudType) {
		this.crudType = crudType;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Map<String, Object> getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(Map<String, Object> sqlParam) {
		this.sqlParam = sqlParam;
	}

	public String[] getSqls() {
		return sqls;
	}

	public void setSqls(String[] sqls) {
		this.sqls = sqls;
	}

	public Map<String, Object>[] getSqlParams() {
		return sqlParams;
	}

	public void setSqlParams(Map<String, Object>[] sqlParams) {
		this.sqlParams = sqlParams;
	}

	public DataServiceDml getNode() {
		return node;
	}

	public void setNode(DataServiceDml node) {
		this.node = node;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
}
