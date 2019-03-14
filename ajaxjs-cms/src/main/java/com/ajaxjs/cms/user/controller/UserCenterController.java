package com.ajaxjs.cms.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.cms.ArticleDao;
import com.ajaxjs.cms.FeedbackService.FeedbackDao;
import com.ajaxjs.cms.app.attachment.Attachment_picture;
import com.ajaxjs.cms.user.controller.BaseUserController;
import com.ajaxjs.cms.user.controller.LoginCheck;
import com.ajaxjs.cms.user.service.UserCommonAuthService;
import com.ajaxjs.cms.user.service.UserService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;

@Path("/user/center")
@Bean("UserPcCenterController")
public class UserCenterController extends BaseUserController {
	@Resource("User_common_authService")
	private UserCommonAuthService authService;

	@Resource("UserService")
	private UserService service;

	static ArticleDao newsDao = new Repository().bind(ArticleDao.class);

	@GET
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Path("/home")
	public String home(ModelAndView mv) {
//		// 更多动态
//		List<Map<String, Object>> topNews = newsDao.findListTop(5);
//		mv.put("topNews", topNews);
//
//		// 最近登录
//		Map<String, String[]> requestData = new HashMap<>();
//		requestData.put("filterField", new String[] { "userId" });
//		requestData.put("filterValue", new String[] { getUserId().toString() });
//		PageResult<Map<String, Object>> loginLog = userDao.findLoginLog(0, 5);
//		mv.put("loginLog", loginLog);

		return jsp("user/home");
	}

	@GET
	@Path("/info")
	@MvcFilter(filters = { LoginCheck.class })
	public String info() {
		return jsp("user/info");
	}

	@GET
	@Path("/info/modifly")
	@MvcFilter(filters = { LoginCheck.class })
	public String infoModifly() {
		return jsp("user/infoModifly");
	}

	@GET
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Path("/info/avatar")
	public String avatar(ModelAndView mv) {
		Attachment_picture avatar = service.findAvaterByUserId(getUserUid());
		mv.put("avatar", avatar);
		return jsp("user/avater");
	}

	@GET
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Path("/loginInfo")
	public String changePassword(ModelAndView mv) {
		mv.put("email", UserService.dao.findById(getUserId()).getEmail());
		return jsp("user/loginInfo");
	}

	static FeedbackDao feedbackDao = new Repository().bind(FeedbackDao.class);

	@GET
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Path("/feedback")
	public String feedback(ModelAndView mv) {
		List<Map<String, Object>> feedbacks = feedbackDao.getListByUserId(getUserId());
		mv.put("feedbacks", feedbacks);
		return jsp("user/feedback");
	}

	@POST
	@Path("/sendSMScode")
	@Produces(MediaType.APPLICATION_JSON)
	@MvcFilter(filters = { LoginCheck.class })
	public String sendSMScode(@QueryParam("phoneNo") @NotNull String phoneNo) {
		return super.sendSMScode(phoneNo);
	}

	@Override
	public UserService getService() {
		return service;
	}

}
