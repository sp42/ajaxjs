package test.com.ajaxjs.mvc.controller.testcase;

import javax.mvc.annotation.Controller;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.simpleApp.CommonController;
import com.ajaxjs.framework.mock.News;
import com.ajaxjs.framework.mock.NewsService;
import com.ajaxjs.framework.mock.NewsServiceImpl;
import com.ajaxjs.framework.service.ServiceException;

@Controller
@Path("/news")
public class NewsController extends CommonController<News, Long, NewsService> {

	@Override
	public NewsService getService() {
		return new NewsServiceImpl();
	}
	
	@GET
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) throws ServiceException {
		pageList(start, limit, model);
		return "";
	}
	
	@GET
	@Path("/{id}")
	public String getInfo(@PathParam("id") Long id, ModelAndView model) throws ServiceException {
		super.info(id, model);
		return "";
	}
	
	@POST
	@Override
	public String create(News news, ModelAndView model) throws ServiceException {
		return super.create(news, model);
	}
	
	@PUT
	@Path("/{id}")
	@Override
	public String update(@PathParam("id")Long id, News news, ModelAndView model) throws ServiceException {
		return super.update(id, news, model);
	}
	
	@DELETE
	@Path("/{id}")
	@Override
	public String delete(News news, ModelAndView model) throws ServiceException {
		return super.delete(news, model);
	}
}
