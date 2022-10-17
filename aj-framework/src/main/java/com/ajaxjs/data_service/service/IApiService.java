package com.ajaxjs.data_service.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestParam;

import com.ajaxjs.data_service.DataServiceDAO;
import com.ajaxjs.data_service.model.DataServiceDTO;

/**
 * API 业务，最重要的
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public interface IApiService extends DataServiceDAO, DataServiceDTO {
	String get(HttpServletRequest req);

	String post(Map<String, Object> formPostMap, HttpServletRequest req);
}
