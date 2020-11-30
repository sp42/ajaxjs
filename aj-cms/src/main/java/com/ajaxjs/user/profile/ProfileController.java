package com.ajaxjs.user.profile;

import java.io.IOException;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.cms.common.AttachmentController;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.sql.SnowflakeIdWorker;
import com.ajaxjs.user.BaseUserController;
import com.ajaxjs.user.User;
import com.ajaxjs.user.filter.CurrentUserOnly;
import com.ajaxjs.user.filter.LoginCheck;
import com.ajaxjs.user.login.LoginController;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.UploadFileInfo;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.controller.MvcRequest;
import com.ajaxjs.web.mvc.filter.MvcFilter;;

/**
 * 用户中心（前端的）
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Path("/user/profile")
@Component
public class ProfileController extends BaseUserController {
	private static final LogHelper LOGGER = LogHelper.getLog(ProfileController.class);

	@Resource("autoWire:ioc.ProfileService")
	private ProfileService service;

	@Override
	public ProfileService getService() {
		return service;
	}

	@GET
	@Path("/user")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String home(ModelAndView mv) throws ServiceException {
		LOGGER.info("用户会员中心（前台）");
		service.onUserCenterHome(mv);
		return user("profile/home");
	}

	@GET
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String profile(ModelAndView mv) {
		LOGGER.info("用户会员中心-个人信息");
		mv.put("info", service.findById(getUserId()));
		return user("profile/profile");
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@MvcFilter(filters = { LoginCheck.class, CurrentUserOnly.class, DataBaseFilter.class })
	public String saveProfile(User user) {
		LOGGER.info("用户会员中心-修改个人信息");
		return update(user.getId(), user);
	}

	@GET
	@Path("avatar")
	@MvcFilter(filters = { LoginCheck.class })
	public String avatar() {
		LOGGER.info("用户会员中心-个人信息-修改头像页面");
		return user("profile/avatar");
	}

	@POST
	@Path("avatar/" + ID_INFO)
	@Produces(MediaType.APPLICATION_JSON)
	@MvcFilter(filters = { LoginCheck.class, CurrentUserOnly.class, DataBaseFilter.class })
	public String saveAvater(MvcRequest req, @PathParam(ID) Long owenerUid) throws IOException {
		LOGGER.info("用户会员中心-个人信息-修改头像");

		UploadFileInfo info = new UploadFileInfo();
		String filename = "user_avater/" + SnowflakeIdWorker.getId();

		return AttachmentController.upload(req, _filename -> {
			// 修改 user avater 字段，保存图片文件名
			User user = new User();
			user.setId(LoginController.getUserId());
			user.setAvatar(_filename);
			service.update(user);
		}, info, filename);
	}

	@GET
	@Path("avatar/updateAvatar")
	@Produces(MediaType.APPLICATION_JSON)
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String updateAvatar(MvcRequest request, @NotNull @QueryParam("avatar") String avatar) throws IOException {
		request.getSession().setAttribute("userAvatar", ConfigService.get("uploadFile.imgPerfix") + avatar);

		return jsonOk("ok");
	}

	@GET
	@Path("info")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String info() {
		return jsp("user/info");
	}

	@GET
	@Path("info/{id}")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String info2(@PathParam(ID) Long userId, ModelAndView mv) {
		mv.put("info", getService().findById(userId));
		return jsp("user/info");
	}

	@GET
	@Path("info/modifly")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String infoModifly() {
		return jsp("user/infoModifly");
	}

	@GET
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Path("loginInfo")
	public String changePassword(ModelAndView mv) {
		mv.put("email", ProfileService.DAO.findById(getUserId()).getEmail());
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

	@PUT
	@Path(ID_INFO)
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Override
	public String update(@PathParam(ID) Long id, User entity) {
		return super.update(id, entity);
	}

}
