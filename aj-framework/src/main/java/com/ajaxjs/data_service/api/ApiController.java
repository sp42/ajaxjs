package com.ajaxjs.data_service.api;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.data_service.model.DataServiceDml;
import com.ajaxjs.data_service.model.ServiceContext;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.util.WebHelper;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 数据服务 API 基类 数据服务向外提供的基础API
 *
 */
@RestController
@RequestMapping("${DataService.api_root:/api}/**")
public class ApiController extends RuntimeData {
	private static final LogHelper LOGGER = LogHelper.getLog(ApiController.class);

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
	@ResponseBody
	public String get(HttpServletRequest req) {
		initCache();
		String uri = getUri(req);
		Map<String, Object> params = WebHelper.getQueryParameters(req);
		DataServiceDml node = exec(uri, ApiController.GET);
		ServiceContext ctx = ServiceContext.factory(uri, req, node, params);

		Object result = get(ctx, plugins);

		if (result != null) {
			if (result instanceof PageResult)
				return BaseController.toJson((PageResult<?>) result);
			else
				return BaseController.toJson(result);
		} else
			return BaseController.jsonNoOk("查询失败");
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

	private static Map<String, Object> getParamMap(HttpServletRequest req, Map<String, Object> formPostMap) {
		Map<String, Object> map;

		if (formPostMap.size() == 0 && req.getContentType().contains("application/json")) {
			map = WebHelper.getRawBodyAsJson(req);// 不是标准的 表单格式，而是 RawBody Payload
		} else
			map = formPostMap;

		if (map.size() == 0)
			throw new IllegalArgumentException("没有提交任何数据");

		return map;
	}

	/**
	 * 创建实体
	 * 
	 * https://blog.csdn.net/Dr_Guo/article/details/79057153
	 * https://blog.csdn.net/qq_40580023/article/details/84992429
	 * 
	 * @param formPostMap 请求参数
	 * @param req         请求对象
	 * @return
	 */
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
	public String post(@RequestParam Map<String, Object> formPostMap, HttpServletRequest req) {
		initCache();
		Map<String, Object> map = getParamMap(req, formPostMap);
		String uri = getUri(req);
		LOGGER.info("API POST: " + uri + " map:" + map);

		DataServiceDml node = exec(uri, ApiController.POST);
		ServiceContext ctx = ServiceContext.factory(uri, req, node, map);

		Serializable newlyId = create(ctx, plugins);

		if (newlyId != null) {
			if (NOT_AUTOCREMENT_ID.equals(newlyId))
				return BaseController.jsonOk("创建成功");
			else {
				if (newlyId instanceof String)
					return BaseController.jsonOk_Extension("创建成功", "\"newlyId\":\"" + newlyId + "\"");
				else
					return BaseController.jsonOk_Extension("创建成功", "\"newlyId\":" + newlyId + "");
			}
		} else
			return BaseController.jsonNoOk("创建失败");
	}

	/**
	 * 修改实体
	 * 
	 * @param req 请求对象
	 * @return
	 */
	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
	public String put(HttpServletRequest req) {
		return put(req, null);
	}

	/**
	 * For TEST purpose
	 * 
	 * @param req 请求对象
	 * @param map 请求参数
	 * @return
	 */
	public String put(HttpServletRequest req, Map<String, Object> map) {
		initCache();

		if (map == null) {
			if (req.getContentType() != null && req.getContentType().contains("application/json"))
				map = WebHelper.getRawBodyAsJson(req);// 不是标准的 表单格式，而是 RawBody Payload
			else {
				map = WebHelper.getParameterMap(req);
			}
		}

		if (map.size() == 0)
			throw new IllegalArgumentException("没有提交任何数据");

		String uri = getUri(req);
		LOGGER.info("API PUT: " + uri);

		DataServiceDml node = exec(uri, ApiController.PUT);
		ServiceContext ctx = ServiceContext.factory(uri, req, node, map);

		if (update(ctx, plugins))
			return BaseController.jsonOk("修改成功");
		else
			return BaseController.jsonNoOk("修改失败");
	}

	/**
	 * 删除实体/批量删除
	 * 
	 * @param req 请求对象
	 * @return
	 */
	@DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
	public String delete(HttpServletRequest req) {
		initCache();
		String uri = getUri(req);
		LOGGER.info("API Delete " + uri);

		Map<String, Object> params = WebHelper.getQueryParameters(req);
		DataServiceDml node = exec(uri, ApiController.DELETE);
		ServiceContext ctx = ServiceContext.factory(uri, req, node, params);

		if (delete(ctx, plugins))
			return BaseController.jsonOk("删除成功");
		else
			return BaseController.jsonNoOk("删除失败");
	}
}
