package test.com.ajaxjs.mvc;

import java.io.IOException;

import javax.mvc.annotation.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.web.Requester;
import com.ajaxjs.web.Responser;

@Path("/hello")
@Controller
public class NewsController implements IController {
	@GET
	public void a(Requester request, Responser response) throws IOException {
		response.getWriter().print("hihi");
	}

	@POST
	public String b() {
		return "redirect::http://qq.com";
	}

	@PUT
	public String c(HttpServletRequest request, HttpServletResponse response) {
		return "hi.jsp";
	}

	@DELETE
	public String d(HttpServletRequest request, HttpServletResponse response) {
		return "html::Hello World!";
	}

	@GET
	@Path("person")
	public String a2(@QueryParam("name") String name) {
		return "";
	}

	@POST
	@Path("person")
	public String b2(@QueryParam("name") String name) {
		return "";
	}

	@PUT
	@Path("person")
	public String c3(@QueryParam("name") String name) {
		return "";
	}

	@DELETE
	@Path("person")
	public String c4(@QueryParam("name") String name) {
		return "";
	}

}
