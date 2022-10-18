package com.ajaxjs.data_service.service;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.data_service.DataServiceDAO;
import com.ajaxjs.data_service.model.DataServiceConstant;
import com.ajaxjs.data_service.model.DataServiceDTO;

/**
 * API 业务，最重要的
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public interface IApiService extends DataServiceDAO, DataServiceDTO, DataServiceConstant {
	/**
	 * 查询
	 * 
	 * @param req
	 * @return
	 */
	Object get(HttpServletRequest req);

	/**
	 * 创建实体
	 * 
	 * @param formPostMap
	 * @param req
	 * @return
	 */
	Serializable post(Map<String, Object> formPostMap, HttpServletRequest req);

	/**
	 * 修改实体
	 * 
	 * @param req
	 * @return
	 */
	Boolean put(Map<String, Object> formPostMap, HttpServletRequest req);

	/**
	 * 删除实体/批量删除
	 * 
	 * @param req 请求对象
	 * @return
	 */
	Boolean delete(HttpServletRequest req);
}
