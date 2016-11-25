package com.ajaxjs.client.controller;

import javax.mvc.annotation.Controller;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.ajaxjs.framework.exception.ServiceException;
import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.mvc.controller.common.Read_Create_Controller;
import com.ajaxjs.user.User;
import com.ajaxjs.user.UserService;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.web.Requester;

@Controller
@Path("/user")
public class UserInfoController extends Read_Create_Controller<User> {
	private static final LogHelper LOGGER = LogHelper.getLog(UserInfoController.class);

	public UserInfoController() {
		setJSON_output(true);
		setService(new UserService());
	}

	public String logout() {
		LOGGER.info("用户登出");

		return "";
	}

	@POST
	@Path("/login")
	public String login(User user, ModelAndView model, Requester request) {
		LOGGER.info("用户登录");
		UserService service = (UserService) getService();
		User _user; // 数据库里面的用户

		try {
			_user = service.findByPhone(user.getPhone());
			if (UserService.isRightUser(_user, user)) {
				service.afterLogin(_user, request.getIP());
				return String.format(json_ok, _user.getName() + "登录成功！");
			}

			return json_not_ok_simple;
		} catch (ServiceException e) {
			return String.format(json_not_ok, e.getMessage());
		}

	}

	@POST
	@Override
	public String create(User entity, ModelAndView model) {
		LOGGER.info("创建用户");
		return super.create(entity, model);
	}
}
