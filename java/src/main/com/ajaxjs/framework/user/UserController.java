package com.ajaxjs.framework.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import com.ajaxjs.framework.controller.AbstractController;

@Controller
@RequestMapping(value = "/service/user")
public class UserController extends AbstractController<User> {

	public UserController() {
		setService(new Service());
	}
	// private static final Log LOGGER =
	// LogFactory.getLog(NewsController.class);

 

}
