package cn.nike;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.mvc.controller.IController;

@Path("/index")
public class MainController implements IController {

	@GET
	public String home() {
		return BaseController.jsp("home");
	}
}
