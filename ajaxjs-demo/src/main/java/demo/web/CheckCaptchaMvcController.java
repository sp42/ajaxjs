package demo.web;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.web.captcha.CaptchaFilter;

@Path("/CheckCaptcha-MVC")
public class CheckCaptchaMvcController implements IController {
	@POST
	@MvcFilter(filters = { CaptchaFilter.class})
	@Produces(MediaType.APPLICATION_JSON)
	public String upload() {
		// 你的业务逻辑……
		return BaseController.jsonOk("ok");
	}
}