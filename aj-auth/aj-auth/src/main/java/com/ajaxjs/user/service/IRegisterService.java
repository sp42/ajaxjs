package com.ajaxjs.user.service;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ajaxjs.spring.easy_controller.ControllerMethod;

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
	 * @return
	 */
	@PostMapping
	@ControllerMethod("用户注册")
	Boolean register(@RequestParam(required = true) Map<String, Object> params);

	/**
	 * 检查用户某个值是否已经存在一样的值
	 * 
	 * @param field 字段名，当前只能是 username/email/phone 中的任意一种
	 * @param value 字段值，要校验的值
	 * @return
	 */
	@GetMapping("/checkRepeat")
	@ControllerMethod("检查用户某个值是否已经存在一样的值")
	Boolean checkRepeat(@RequestParam String field, @RequestParam String value);
}
