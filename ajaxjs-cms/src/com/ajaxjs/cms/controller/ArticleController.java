package com.ajaxjs.cms.controller;

import java.util.Map;

import javax.mvc.annotation.Controller;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.ajaxjs.cms.model.Article;
import com.ajaxjs.cms.service.ArticleService;
import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.framework.model.PageResult;
import com.ajaxjs.js.JsonHelper;
import com.ajaxjs.mvc.controller.CommonController;

/**
 * just a json
 * 
 * @author xinzhang
 *
 */
@Controller
@Path("/news_api")
public class ArticleController extends CommonController<Article, Long> {
	public ArticleController() {
		setJSON_output(true);
	}
	
	@Override
	public ArticleService getService() {
		return new ArticleService();
	}

	@GET
	@Override
	public String list(@QueryParam("start") int start, @QueryParam("limit") int limit, ModelAndView model) {
		// CatalogController.initCatalog(Common.newsCatalog_Id, model, true);
		initDb();

		ArticleService service = getService(); // 避免 service 为单例

		PageResult<Map<String, Object>> pageResult = null;
		try {
			pageResult = getService().findPagedList2(getParam(start, limit));
			model.put("PageResult", pageResult);
		} catch (Exception e) {
			e.printStackTrace();
			model.put(errMsg, e);
		} finally {
			closeDb();
		}

		service.prepareData(model);
		//model.put("MapOutput", JsonHelper.stringifyListMap(pageResult.getRows())); // Map 类型的输出
		return paged_json_List;
	}

}
