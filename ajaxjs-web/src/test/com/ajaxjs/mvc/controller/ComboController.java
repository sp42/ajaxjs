package test.com.ajaxjs.mvc.controller;

import java.io.IOException;

import javax.mvc.annotation.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.mvc.controller.MvcRequest;

/**
 * 整合所有控制器的测试
 * @author xinzhang
 *
 */
@Controller
@Path("/combo")
public class ComboController implements IController {
	@GET
	public String a(MvcRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().print("hihi");
		return null;
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
	@Path("mvc")
	public String a1() {
		return "index.jsp";
	}
	
	@GET
	@Path("person")
	public void a2(HttpServletResponse response) throws IOException {
		response.getWriter().print("just person");
	}

	@POST
	@Path("person")
	public String b2(@QueryParam("name") String name) {
		return "json::{\"name\":\"" + name + "\"}";
	}

	@GET
	@Path("person/{id}")
	public void a2_2(@QueryParam("name") String name, @QueryParam("word") String word, @PathParam("id") int count, HttpServletResponse response) throws IOException {
		response.getWriter().print(name + " " + word + "_" + count);
	}
}
