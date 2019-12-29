package com.ajaxjs.cms.app;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.ajaxjs.cms.app.catalog.CatalogServiceImpl;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.framework.ViewObjectService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.util.CommonUtil;

@Bean
public class ArticleService extends BaseService<Map<String, Object>> implements ViewObjectService {

	@TableName(value = "entity_article", beanClass = Map.class)
	public interface ArticleDao extends IBaseDao<Map<String, Object>> {
		@Select("SELECT e.id, e.name, e.createDate, e.updateDate, e.catalogId, e.intro, e.cover, e.stat FROM ${tableName} e WHERE "
				+ WHERE_REMARK + DESCENDING_ID)
		public PageResult<Map<String, Object>> list(int start, int limit, Function<String, String> sqlHandler);

		@Select("SELECT e.id, e.name, e.createDate, e.cover, e.intro FROM ${tableName} e WHERE " + WHERE_REMARK
				+ DESCENDING_ID)
		public List<Map<String, Object>> simpleList(Function<String, String> sqlHandler);

		/**
		 * 可分类的，可分页的列表
		 * 
		 * @param catelogId
		 * @param start
		 * @param limit
		 * @return
		 */
		@Select(value = "SELECT entry.id, entry.name, entry.createDate, entry.updateDate, entry.catalogId, intro, c.name AS catelogName FROM ${tableName} entry INNER JOIN "
				+ catelog_finById
				+ "ON entry.`catalogId` = c.id  WHERE 1 = 1 ORDER BY entry.createDate DESC", countSql = "SELECT COUNT(entry.id) AS count FROM ${tableName} entry WHERE catalogId IN "
						+ catelog_find + " AND 1 = 1",

				sqliteValue = "SELECT id, name, createDate, updateDate, entry.catelogId, catalogName, intro FROM ${tableName} entry INNER JOIN "
						+ catelog_finById_sqlite
						+ " ON entry.`catelogId` = c.catelogId  WHERE 1 = 1 ORDER BY entry.createDate DESC", sqliteCountSql = "SELECT COUNT(entry.id) AS count FROM ${tableName} entry WHERE catelogId IN "
								+ catelog_find_sqlite + WHERE_REMARK_AND)
		@Override
		public PageResult<Map<String, Object>> findPagedListByCatelogId(int catelogId, int start, int limit,
				Function<String, String> sqlHandler);

		@Select("SELECT YEAR(`createDate`) year , MONTH(`createDate`) month FROM entity_article t "
				+ "GROUP BY YEAR(`createDate`), MONTH(`createDate`) ORDER BY YEAR(`createDate`) DESC, MONTH(`createDate`) DESC LIMIT 0, ?")
		public List<Map<String, Object>> groupByMonth(int maxMonth);
	}

	public static ArticleDao dao = new Repository().bind(ArticleDao.class);

	{
		setUiName("文章");
		setShortName("article");
		setDao(dao);
	}

	public PageResult<Map<String, Object>> list(int catalogId, int start, int limit, int status) {
		return dao.list(start, limit, CatalogServiceImpl.setCatalog(catalogId, getDomainCatalogId())
				.andThen(setStatus(status)).andThen(BaseService::searchQuery).andThen(BaseService::betweenCreateDate));
	}

	public int getDomainCatalogId() {
		return ConfigService.getValueAsInt("data.articleCatalog_Id");
	}

	public List<Map<String, Object>> findListTop(int top) {
		return dao.simpleList(CatalogServiceImpl.setCatalog(getDomainCatalogId(), getDomainCatalogId())
				.andThen(BaseService.setStatus(CommonConstant.ON_LINE).andThen(sql -> sql + " LIMIT 0, " + top)));
	}

	/**
	 * 获取最近 x 个月的月份
	 * 
	 * @param maxMonth 最近 x 个月
	 * @return
	 */
	public List<Map<String, Object>> groupByMonth(int maxMonth) {
		List<Map<String, Object>> list = dao.groupByMonth(maxMonth);
		
		for (Map<String, Object> map : list) {
			int year = (int)map.get("year"), month = (int)map.get("month");

			map.put("start", year + "-" + month + "-1"); // 某个月份的第一天
			
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month, 1); // 这里先设置要获取月份的下月的第一天
			calendar.add(Calendar.DATE, -1);//这里将日期值减去一天，从而获取到要求的月份最后一天
			
			String end = CommonUtil.simpleDateFormatFactory(CommonUtil.commonDateFormat_shortest).format(calendar.getTime());
			map.put("end", end);
		}
		
		return list;
	}
	
	@Override
	public void showList(ModelAndView mv) {
	}

	@Override
	public void showInfo(ModelAndView mv, Long id) {
	}
}
