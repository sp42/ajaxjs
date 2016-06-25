package com.ajaxjs.framework.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import com.ajaxjs.framework.controller.AbstractController;
import com.ajaxjs.framework.user.User;
import com.ajaxjs.framework.user.service.Service;

@Controller
@RequestMapping(value = "/service/user")
public class UserController extends AbstractController<User> {

	public UserController() {
		setService(new Service());
	}
	// private static final Log LOGGER =
	// LogFactory.getLog(NewsController.class);

 

}
