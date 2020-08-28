package com.ajaxjs.user.register;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.framework.filter.CaptchaFilter;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.framework.filter.EnableTransaction;
import com.ajaxjs.user.User;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.mvc.filter.MvcFilter;

/**
 * 注册控制器。为方便使用，它继承与登录控制器，但它们的关系的平等的。只是为了 Java的单根继承
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Path("/user/register")
@Component
public class RegisterController extends BaseController<User> {
	private static final LogHelper LOGGER = LogHelper.getLog(RegisterController.class);

	@Resource("autoWire:ioc.RegisterService")
	private RegisterService service;

	@GET
	public String regsiter() {
		LOGGER.info("用户注册页");
		return jsp("user/register");
	}

	@POST
	@MvcFilter(filters = { CaptchaFilter.class, DataBaseFilter.class })
	@EnableTransaction
	@Produces(MediaType.APPLICATION_JSON)
	public String doRegister(User user, Map<String, Object> params) throws ServiceException {
		LOGGER.info("正在注册");
		getService().registerByPsw(user, params);
		return jsonOk("恭喜你，注册成功");
	}

	/**
	 * 检查是否重复的用户名
	 * 
	 * @param phone 手机号码
	 * @return true=已存在
	 * @throws ServiceException
	 */
	@GET
	@Path("checkIfRepeat")
	@MvcFilter(filters = { DataBaseFilter.class })
	public String checkIfRepeat(@QueryParam("name") String name, @QueryParam("email") String email, @QueryParam("phone") String phone) {
		LOGGER.info("检查是否重复的" + name);
		boolean checkIfRepeated = service.checkIfRepeated_(name, email, phone);

		return toJson(new HashMap<String, Boolean>() {
			private static final long serialVersionUID = -5033049204280154615L;
			{
				put("isRepeat", checkIfRepeated);
			}
		});
	}

	@Override
	public RegisterService getService() {
		return service;
	}
}
