package com.ajaxjs.cms.app;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.ajaxjs.cms.app.catalog.Catalogable;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.QueryParams;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;

@Bean(value = "ArticleService")
public class ArticleService extends BaseService<Map<String, Object>> implements Catalogable<Map<String, Object>> {
	@TableName(value = "entity_article", beanClass = Map.class)
	public interface ArticleDao extends IBaseDao<Map<String, Object>> {

		/**
		 * 可分类的，可分页的列表
		 * 
		 * @param catelogId
		 * @param start
		 * @param limit
		 * @return
		 */
		@Select(value = "SELECT entry.id, entry.name, entry.createDate, entry.updateDate, entry.catelogId, intro, c.name AS catelogName FROM ${tableName} entry INNER JOIN " + catelog_finById
				+ "ON entry.`catelogId` = c.id  WHERE 1 = 1 ORDER BY entry.createDate DESC", 
				countSql = "SELECT COUNT(entry.id) AS count FROM ${tableName} entry WHERE catelogId IN " + catelog_find + " AND 1 = 1",

				sqliteValue = "SELECT id, name, createDate, updateDate, entry.catelogId, catelogName, intro FROM ${tableName} entry INNER JOIN " + catelog_finById_sqlite
						+ " ON entry.`catelogId` = c.catelogId  WHERE 1 = 1 ORDER BY entry.createDate DESC", 
				sqliteCountSql = "SELECT COUNT(entry.id) AS count FROM ${tableName} entry WHERE catelogId IN " + catelog_find_sqlite + " AND 1 = 1")
		@Override
		public PageResult<Map<String, Object>> findPagedListByCatelogId(int catelogId, int start, int limit, Function<String, String> sqlHandler);
	}

	public static ArticleDao dao = new Repository().bind(ArticleDao.class);

	{
		setUiName("文章");
		setShortName("article");
		setDao(dao);
	}

	@Override
	public PageResult<Map<String, Object>> findPagedListByCatelogId(int catelogId, int start, int limit) {
		if (catelogId == 0)
			catelogId = getDomainCatelogId();

		return dao.findPagedListByCatelogId(catelogId, start, limit, QueryParams.initSqlHandler(QueryParams.init()));
	}

	@Override
	public List<Map<String, Object>> findListByCatelogId(int catelogId) {
		if (catelogId == 0)
			catelogId = getDomainCatelogId();

		return dao.findPagedListByCatelogId(catelogId, 0, 9999, QueryParams.initSqlHandler(QueryParams.init()));
	}

	@Override
	public int getDomainCatelogId() {
		return ConfigService.getValueAsInt("data.articleCatalog_Id");
	}
	
	public List<Map<String, Object>> findListTop(int top) {
		return dao.findListTop(top);
	}
}
