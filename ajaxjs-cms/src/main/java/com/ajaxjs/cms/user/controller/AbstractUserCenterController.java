package com.ajaxjs.cms.user.controller;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.cms.FeedbackService.FeedbackDao;
import com.ajaxjs.cms.app.attachment.Attachment_picture;
import com.ajaxjs.cms.user.service.UserService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;

 
public abstract class AbstractUserCenterController extends BaseUserController {

//	@GET
//	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
//	@Path("/home")
//	public String home(ModelAndView mv) {
//		return jsp("user/home");
//	}

	@GET
	@Path("/info")
	@MvcFilter(filters = { LoginCheck.class })
	public String info() {
		return jsp("user/info");
	}
	
	@GET
	@Path("/info/{id}")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class  })
	public String info2(@PathParam("id")Long userId, ModelAndView mv) {
		mv.put("info", getService().findById(userId));
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
		Attachment_picture avatar = getService().findAvaterByUserId(getUserUid());
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

}
