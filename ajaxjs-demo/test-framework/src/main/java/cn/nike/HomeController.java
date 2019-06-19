package cn.nike;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ajaxjs.mvc.Constant;
import com.ajaxjs.mvc.controller.IController;

@Path("/index")
public class HomeController implements IController {
	@GET
	public String home() {
		return Constant.jsp_perfix_webinf + "/home";
	}
}
