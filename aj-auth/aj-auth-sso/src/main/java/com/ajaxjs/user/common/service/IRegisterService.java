package com.ajaxjs.user.common.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户注册
 * 
 * @author Frank Cheung
 *
 */
public interface IRegisterService extends IBaseUserService {
	/**
	 * 用户注册
	 * 
	 * TODO 数据库事务 和 验证码
	 * 
	 * @param params
	 * @param req
	 * @return
	 */
	Boolean register(Map<String, Object> params, HttpServletRequest req);

	/**
	 * 检查某个值是否已经存在一样的值
	 * 
	 * @param field    字段名，当前只能是 username/email/phone 中的任意一种
	 * @param value    字段值，要校验的值
	 * @param tenantId 租户 id
	 * @return
	 */
	Boolean checkRepeat(String field, String value, int tenantId);
}
