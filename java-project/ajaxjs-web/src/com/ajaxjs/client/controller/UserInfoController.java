package com.ajaxjs.client.controller;

import javax.mvc.annotation.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.ajaxjs.Constant;
import com.ajaxjs.client.model.Version;
import com.ajaxjs.framework.exception.ServiceException;
import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.mvc.controller.ReadOnlyController;
import com.ajaxjs.user.User;
import com.ajaxjs.user.UserService;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.web.Requester;
import com.ajaxjs.web.upload.Upload;
import com.ajaxjs.web.upload.UploadException;
import com.ajaxjs.web.upload.UploadRequest;

@Controller
@Path("/user")
public class UserInfoController extends ReadOnlyController<User> {
	private static final LogHelper LOGGER = LogHelper.getLog(UserInfoController.class);

	public UserInfoController() {
		setJSON_output(true);
		setService(new UserService());
	}

	public String logout() {
		LOGGER.info("用户登出");

		return "";
	}
	
	@Override
	public void prepareData(HttpServletRequest request, ModelAndView model) {
		Requester req = (Requester)request;
		String perfix = req.getBasePath();

		if (model.get("info") != null) {
			User user = (User) model.get("info");
			user.setAvatar(perfix + upload_avatar_folder + user.getAvatar());
		}
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
				
				return String.format("json::{\"isOk\": true, \"msg\" : \"%s\", \"id\": %s}", _user.getName() + "登录成功！", _user.getId());
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
	
	@PUT
	@Path("/{id}")
	@Override
	public String update(@PathParam("id") long id, User entity, ModelAndView model) {
		return super.update(id, entity, model);
	}
	
	private final static String upload_avatar_ok = "json::{\"isOk\": true, \"msg\" : \"用户 id：%s %s\", \"url\": \"%s\"}";
	private final static String upload_avatar_folder = "/img/avatar/";
	
	/**
	 * 用户头像上传
	 * @param id
	 * @param request
	 * @param model
	 * @return
	 */
	@POST
	@Path("/{id}/avatar")
	public String uploadAvatar(@PathParam("id") long id, HttpServletRequest request, ModelAndView model) {
		Requester req = (Requester)request;
		
		UploadRequest ur = new UploadRequest();
		ur.setUpload_save_folder(req.Mappath(upload_avatar_folder) + Constant.file_pathSeparator);// 指定保存目录
		ur.setRequest(request);

		try {
			new Upload().upload(ur);
		} catch (UploadException e) {
			return String.format(json_not_ok, e.getMessage());
		}

		if (!ur.isOk()) {
			return json_not_ok_simple;
		}

		LOGGER.info("上传头像成功！" + ur.getUploaded_save_filePath());

		// 保存到数据库
		UserService service = (UserService) getService();
		User user; 

		try {
			user = service.getById(id);
			if (user == null) {
				return String.format(json_not_ok, "用户 id：" + id + "不存在！");
			}
			
			// 不想 UPDATE SQL 除 avatar 字段之外的
			User _user = new User();
			_user.setId(user.getId());
			_user.setAvatar(ur.getUploaded_save_fileName());
			_user.setService(service);
			
			if (service.update(_user)) {
				String perfix = req.getBasePath(); // 拼凑 url 前缀
				
				return String.format(upload_avatar_ok, id, "修改头像成功！", perfix + upload_avatar_folder + ur.getUploaded_save_fileName());
			} else throw new ServiceException("修改头像数据库失败！");
		} catch (ServiceException e) {
			return String.format(json_not_ok, e.getMessage());
		}
	}
}
