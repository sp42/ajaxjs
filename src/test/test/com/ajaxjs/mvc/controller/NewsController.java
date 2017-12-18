package test.com.ajaxjs.mvc.controller;

import javax.mvc.annotation.Controller;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.util.mock.News;
import com.ajaxjs.util.mock.NewsService;
import com.ajaxjs.util.mock.NewsServiceImpl;
import com.ajaxjs.web.CommonController;

@Controller
@Path("/news")
public class NewsController extends CommonController<News, Long> {
	public NewsController(){
		
		setJSON_output(true);
//		setService(new NewsService());
	}

	@Override
	public NewsService getService() {
		return new NewsServiceImpl();
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
	public String info(@PathParam("id") Long id, ModelAndView model) {
		return super.info(id, model);
	}
	
	@POST
	@Override
	public String create(News news, ModelAndView model) {
		return super.create(news, model);
	}
	
	@PUT
	@Path("/{id}")
	@Override
	public String update(News news, ModelAndView model) {
		return super.update(news, model);
	}
	
	@DELETE
	@Path("/{id}")
	@Override
	public String delete(News news, ModelAndView model) {
		return super.delete(news, model);
	}
}
