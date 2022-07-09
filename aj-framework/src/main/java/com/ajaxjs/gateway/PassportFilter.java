package com.ajaxjs.gateway;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ajaxjs.util.config.EasyConfig;
import com.ajaxjs.util.map.MapTool;

public class PassportFilter implements HandlerInterceptor {
	/**
	 * 认证 HTTP HEAD 中 字段
	 */
	public final static String CLIENT_ID = "clientId";

	/**
	 * 认证 HTTP HEAD 中 字段
	 */
	public final static String TOKEN = "token";

	@Autowired
	EasyConfig config;

	static Map<String, Passport> CLIENTS;

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) {
		String clientId = req.getHeader(CLIENT_ID);
		String token = req.getHeader(TOKEN);

		if (!StringUtils.hasText(clientId) || !StringUtils.hasText(token))
			throw new IllegalAccessError("缺少认证信息的参数！");

		List<Map<String, Object>> list = config.getListMap("auth");

		if (CollectionUtils.isEmpty(list))
			throw new NullPointerException("未配置任何认证信息");

		if (CLIENTS == null) { // 初始化
			CLIENTS = new HashMap<>();

			for (Map<String, Object> map : list) {
				Passport passport = MapTool.map2Bean(map, Passport.class);
				CLIENTS.put(clientId, passport);
			}
		}

//		Optional<Map<String, Object>> authO = list.stream().filter(map -> clientId.equals(map.get("clientId").toString())).findFirst();
//
//		if (!authO.isPresent())
//			throw new IllegalAccessError("非法客户端 id：" + clientId);

//		Map<String, Object> auth = authO.get();
//		Passport passport = MapTool.map2Bean(auth, Passport.class);

		Passport passport = getPassportByClientId(clientId);

		if (!token.equals(passport.getToken()))
			throw new IllegalAccessError("非法客户端 token：" + token);

		return false;
	}

	private Long getTenantIdByClientId(String clientId) {
		return getPassportByClientId(clientId).getTenantId();
	}

	private Long getPortalIdByClientId(String clientId) {
		return getPassportByClientId(clientId).getPortalId();
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public Passport getPassportByClientId(HttpServletRequest req) {
		String clientId = req.getHeader(CLIENT_ID);
		return getPassportByClientId(clientId);
	}
	
	/**
	 * 
	 * @param clientId
	 * @return
	 */
	public Passport getPassportByClientId(String clientId) {
		if (!CLIENTS.containsKey(clientId))
			throw new IllegalAccessError("非法客户端 id：" + clientId);
		
		return CLIENTS.get(clientId);
	}
	/**
	 * 检测是否有效
	 * 
	 * @param entity
	 * @param field
	 * @param value
	 * @return
	 */
	private boolean isDataValid(Object _entity, String field, Long value) {
		if (_entity instanceof TenantPortal) {
			TenantPortal entity = (TenantPortal) _entity;

			switch (field) {
			case "tenantId":
				if (value != entity.getTenantId())
					throw new IllegalAccessError("非法访问，字段：" + field + " value: " + value + " 错误");
				else
					return true;
			case "portalId":
				if (value != entity.getPortalId())
					throw new IllegalAccessError("非法访问，字段：" + field + " value: " + value + " 错误");
				else
					return true;
			}
		} else if (_entity instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> entity = (Map<String, Object>) _entity;
			if (!entity.containsKey(field))
				throw new NullPointerException("实体中找不到字段 ： " + field);

			if (value != (Long) entity.get(field))
				throw new IllegalAccessError("非法访问，字段：" + field + " value: " + value + " 错误");
			else
				return true;
		}

		throw new IllegalAccessError("非法类型参数");
	}

	public boolean checkTenant(String clientId, Map<String, Object> entity) {
		Long tenantId = getTenantIdByClientId(clientId);

		return isDataValid(entity, "tenantId", tenantId);
	}

	public boolean checkPortal(String clientId, Map<String, Object> entity) {
		Long portalId = getPortalIdByClientId(clientId);

		return isDataValid(entity, "portalId", portalId);
	}

	public boolean checkTenant(String clientId, TenantPortal bean) {
		Long tenantId = getTenantIdByClientId(clientId);

		return isDataValid(bean, "tenantId", tenantId);
	}

	public boolean checkPortal(String clientId, TenantPortal bean) {
		Long portalId = getPortalIdByClientId(clientId);

		return isDataValid(bean, "portalId", portalId);
	}
}
