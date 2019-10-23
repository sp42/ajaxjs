package com.ajaxjs.user.controller;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.user.service.ForgetPassword;
import com.ajaxjs.user.service.VerifyToken;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 
 * 重置密码
 */
@Path("/user/reset_password")
public class ResetPasswordController extends BaseController<Map<String, Object>> {
	private static final LogHelper LOGGER = LogHelper.getLog(ResetPasswordController.class);

	@GET
	public String get() {
		LOGGER.info("重置密码-输入邮箱");
		return jsp("user/reset-password");
	}

	@POST()
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String findByEmail(@QueryParam("email") String email, MvcRequest request) {
		return jsonOk(ForgetPassword.findByEmail(request.getBasePath() + "/user/reset_password/findByEmail/", email));
	}

	@GET
	@Path("/findByEmail")
	public String findByEmailJSP(@QueryParam("token") String token) throws IllegalAccessException {
		LOGGER.info("重置密码-输入新密码");
		
		if(VerifyToken.verifyToken(token, VerifyToken.getTimeout(), false) != null)
			return jsp("user/reset-password-findByEmail");
		else {
			throw new IllegalAccessException("非法访问");
		}
	}

	@POST
	@Path("/findByEmail")
	@Produces(MediaType.APPLICATION_JSON)
	public String updatePwd(@QueryParam("token") String token) throws IllegalAccessException {
		LOGGER.info("重置密码-保存新密码");
		
		if(VerifyToken.verifyToken(token, VerifyToken.getTimeout(), false) != null)
			return jsp("user/reset-password-findByEmail");
		else {
			throw new IllegalAccessException("非法访问");
		}
	}

	@Override
	public IBaseService<Map<String, Object>> getService() {
		return null;
	}
}
