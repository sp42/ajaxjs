package com.ajaxjs.article.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.mvc.annotation.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.ajaxjs.article.model.Article;
import com.ajaxjs.article.service.ArticleService;
import com.ajaxjs.framework.dao.MyBatis;
import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.jdbc.Helper;
import com.ajaxjs.mvc.controller.AdminController;
import com.ajaxjs.util.json.JsonHelper;

@Controller
@Path("/admin/Article")
public class ArticleAdminController extends AdminController<Article> {
	public ArticleAdminController() {
		setService(new ArticleService());
	}

	@Override
	public void prepareData(HttpServletRequest reqeust, ModelAndView model) {
		// 获取分类
		try (Connection conn = MyBatis.getConnection();) {
			String catalogSql = "SELECT * FROM catalog";
			List<Map<String, Object>> all_catalogs = Helper.queryList(conn, catalogSql);
			model.put("all_catalogs", JsonHelper.stringify(all_catalogs));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		super.prepareData(reqeust, model);
	}

	@POST
	@Override
	public String create(Article entity, ModelAndView model) {
		return super.create(entity, model);
	}

	@PUT
	@Path("/{id}")
	@Override
	public String update(@PathParam("id")long id, Article entry, ModelAndView model) {
		 return super.update(id, entry, model);
	}

}
