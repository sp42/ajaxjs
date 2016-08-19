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
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.web.Requester;
import com.ajaxjs.web.Responser;

@Controller
@Path("/hello")
public class FooController implements IController {
	@GET
	public String a(Requester request, Responser response) throws IOException {
		// if(request.getRequestURI().contains(".doc")) {
		// String[] strs = DocumentRenderer.getEntityInfo(Video.class);
		// request.setAttribute("entityInfo", strs[0]);
		//
		// if(strs[1] != null) { // 更多关于该实体的文档
		// request.setAttribute("moreDocument", strs[1]);
		// }
		//
		//// request.setAttribute("meta",
		// DocumentRenderer.getDocument(Video.class,
		// getService().getSQL_TableName()));
		// return "/WEB-INF/jsp/common/entity/showDocument.jsp";
		// }
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
	@Path("person")
	public void a2(Responser response) throws IOException {
		response.getWriter().print("just person");
	}
	
	@GET
	@Path("person/{id}")
	public void a2_2(@QueryParam("name") String name, @QueryParam("word") String word, @PathParam("id") int count, Responser response)
			throws IOException {
		response.getWriter().print(name + " " + word + "_" + count);
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
