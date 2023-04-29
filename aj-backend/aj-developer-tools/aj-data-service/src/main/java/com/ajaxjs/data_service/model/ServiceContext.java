package com.ajaxjs.data_service.model;

import com.ajaxjs.data_service.DataServiceConstant;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.Map;

/**
 * 数据服务上下文对象
 *
 * @author Frank Cheung
 */
@Data
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

    private HttpServletRequest request;

    private DataServiceDml node;

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
}
