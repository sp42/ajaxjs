package com.ajaxjs.data_service.controller;

import com.ajaxjs.data_service.DataServiceConstant;
import com.ajaxjs.data_service.model.DataServiceDml;
import com.ajaxjs.data_service.model.ServiceContext;
import com.ajaxjs.data_service.service.DataService;
import com.ajaxjs.framework.spring.easy_controller.anno.ControllerMethod;
import com.ajaxjs.web.WebHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Map;

/**
 * 数据服务 API 基类 数据服务向外提供的基础 API
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public abstract class BaseDataServiceApiController implements DataServiceConstant {
	@Autowired
	private DataService dataService;
	
	public DataService getDataService() {
		return dataService;
	}

	public void setDataService(DataService dataService) {
		this.dataService = dataService;
	}

	@GetMapping
	@ControllerMethod("数据服务 GET")
	public Object get(HttpServletRequest req) {
		return dataService.get(getServiceContext(req, DataService.GET, null));
	}

	@PostMapping
	@ControllerMethod("数据服务 创建实体")
	public Serializable post(Map<String, Object> formPostMap, HttpServletRequest req) {
		Serializable newlyId = dataService.create(getServiceContext(req, DataService.POST, formPostMap));

		if (newlyId != null) {
			if (DataService.NOT_AUTOCREMENT_ID.equals(newlyId))
				return DataService.NOT_AUTOCREMENT_ID; // 创建成功，但没返回 id
			else
				return newlyId;
		} else
			throw new NullPointerException("创建失败，未能返回新创建实体之 id");
	}

	@PutMapping
	@ControllerMethod("数据服务 修改实体")
	public Boolean put(Map<String, Object> formPostMap, HttpServletRequest req) {
		return dataService.update(getServiceContext(req, DataService.PUT, formPostMap));
	}

	@DeleteMapping
	@ControllerMethod("数据服务 删除实体/批量删除")
	public Boolean delete(HttpServletRequest req) {
		return dataService.delete(getServiceContext(req, DataService.DELETE, null));
	}

	/**
	 * 
	 * @param req
	 * @param dml
	 * @param formPostMap
	 * @return
	 */
	private ServiceContext getServiceContext(HttpServletRequest req, Map<String, DataServiceDml> dml, Map<String, Object> formPostMap) {
		dataService.init();
		String uri = getUri(req);
		DataServiceDml node = DataService.exec(uri, dml);

		if (dml == DataService.GET || dml == DataService.DELETE)
			return ServiceContext.factory(uri, req, node, WebHelper.getQueryParameters(req));
		if (dml == DataService.POST || dml == DataService.PUT)
			return ServiceContext.factory(uri, req, node, WebHelper.getParamMap(req, formPostMap));

		throw new IllegalArgumentException("不可能走到这步");
	}

	/**
	 * API 接口的总目录
	 */
	@Value("${DataService.api_root:/api}")
	public String API_URL_ROOT;

	/**
	 * 获取路径，转化为命令
	 *
	 * @param req 请求对象
	 * @return 命令
	 */
	private String getUri(HttpServletRequest req) {
		String uri = WebHelper.getRoute(req);
		uri = uri.replace(API_URL_ROOT, "");

		if (uri.startsWith("/"))
			uri = uri.substring(1, uri.length());

		return uri;
	}

}
