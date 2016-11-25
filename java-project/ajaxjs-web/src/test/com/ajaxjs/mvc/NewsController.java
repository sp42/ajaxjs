package test.com.ajaxjs.mvc;

import javax.mvc.annotation.Controller;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.framework.model.News;
import com.ajaxjs.mvc.controller.AbstractController;

import test.com.ajaxjs.framework.NewsService;

@Controller
@Path("/news")
public class NewsController extends AbstractController<News> {
	public NewsController(){
		setService(new NewsService());
	}
	
	@GET
	@Override
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {
		String _return = super.list(start, limit, model);
		return _return;
	}
	
	@GET
	@Path("/{id}")
	@Override
	public String getById(@PathParam("id") long id, ModelAndView model) {
		return super.getById(id, model);
	}
	
	@POST
	@Override
	public String create(News news, ModelAndView model) {
		return super.create(news, model);
	}
	
	@PUT
	@Path("/{id}")
	@Override
	public String update(@PathParam("id") long id, News news, ModelAndView model) {
		return super.update(id, news, model);
	}
	
	@DELETE
	@Path("/{id}")
	@Override
	public String delete(@PathParam("id") long id, ModelAndView model) {
		return super.delete(id, model);
	}
}
