package com.ajaxjs.user.controller;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.cms.app.attachment.Attachment_picture;
import com.ajaxjs.cms.app.attachment.Attachment_pictureController;
import com.ajaxjs.cms.utils.sms.SMS;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.user.filter.LoginCheck;
import com.ajaxjs.user.model.User;
import com.ajaxjs.user.service.UserCommonAuthService;
import com.ajaxjs.user.service.UserService;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.UploadFileInfo;

/**
 * 用户中心（前端的）
 * 
 * @author Administrator
 *
 */
@Path("/user/user-center")
@Bean("PcUserInfoController")
public class UserCenterController extends AbstractAccountInfoController {
	private static final LogHelper LOGGER = LogHelper.getLog(UserCenterController.class);

	@Resource("UserService")
	private UserService service;
	
	@Override
	public UserService getService() {
		return service;
	}

	@Resource("User_common_authService") // 指定 service id
	private UserCommonAuthService passwordService;
	
	@Resource("AliyunSMSSender")
	private SMS sms;
	
	@Override
	public SMS getSms() {
		return sms;
	}
	
	@GET
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String home(ModelAndView mv) {
		LOGGER.info("用户会员中心（前台）");
		return jsp("user/user-center/home");
	}
	
	@GET
	@Path("/profile")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String profile() {
		LOGGER.info("用户会员中心-个人信息");
		return jsp("user/user-center/profile");
	}
	
	@GET
	@Path("/profile/avater")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String avater() {
		LOGGER.info("用户会员中心-个人信息-修改头像");
		return jsp("user/user-center/avater");
	}
	
	@GET
	@Path("/address")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String address() {
		LOGGER.info("用户会员中心-我的地址");
		return jsp("user/user-center/address");
	}


//	@GET
//	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
//	@Path("/home")
//	public String home(ModelAndView mv) {
//		return jsp("user/home");
//	}

	@GET
	@Path("/info")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String info() {
		return jsp("user/info");
	}
	
	@GET
	@Path("/info/{id}")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String info2(@PathParam(id)Long userId, ModelAndView mv) {
		mv.put("info", getService().findById(userId));
		return jsp("user/info");
	}

	@GET
	@Path("/info/modifly")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String infoModifly() {
		return jsp("user/infoModifly");
	}

	@GET
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Path("/info/avatar")
	public String avatar(ModelAndView mv) {
		
		Attachment_picture avatar = UserService.dao.findAvaterByUserId(getUserUid());
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

//	static FeedbackDao feedbackDao = new Repository().bind(FeedbackDao.class);
//
//	@GET
//	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
//	@Path("/feedback")
//	public String feedback(ModelAndView mv) {
//		List<Map<String, Object>> feedbacks = feedbackDao.getListByUserId(getUserId());
//		mv.put("feedbacks", feedbacks);
//		return jsp("user/feedback");
//	}

	@POST
	@Path("/sendSMScode")
	@Produces(MediaType.APPLICATION_JSON)
	@MvcFilter(filters = { LoginCheck.class })
	public String sendSMScode(@QueryParam("phoneNo") @NotNull String phoneNo) {
		return super.sendSMScode(phoneNo);
	}
	
	@PUT
	@Path(idInfo)
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Override
	public String update(@PathParam(id) Long id, User entity) {
		return super.update(id, entity);
	}
	
	@POST
	@Path("/avatar")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String updateOrCreateAvatar(MvcRequest request) throws Exception {
		LOGGER.info("更新头像,uid:" + getUserUid());
		UploadFileInfo info;

		info = Attachment_pictureController.uploadByConfig(request);

		final Attachment_picture avatar = getService().updateOrCreateAvatar(getUserUid(), info);

		return toJson(new Object() {
			@SuppressWarnings("unused")
			public Boolean isOk = true;
			@SuppressWarnings("unused")
			public String msg = "修改头像成功！";
			@SuppressWarnings("unused")
			public String imgUrl = avatar.getPath();
		});
	}

}
