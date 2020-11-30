package com.ajaxjs.cms.service;

import java.util.List;
import java.util.function.Function;

import com.ajaxjs.cms.common.TreeLikeService;
import com.ajaxjs.cms.model.Ads;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.PageResult;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.util.ioc.Component;

@Component
public class AdsService extends BaseService<Ads> {
	@TableName(value = "cms_ads", beanClass = Ads.class)
	public interface AdsDao extends IBaseDao<Ads> {
	}

	public static AdsDao dao = new Repository().bind(AdsDao.class);

	{
		setUiName("广告");
		setShortName("ads");
		setDao(dao);
	}

	public int getDomainCatalogId() {
		return ConfigService.getValueAsInt("data.adsCatalog_Id");
	}

	/**
	 * 
	 * @param catalogId 分类 id
	 * @return
	 */
	public List<Ads> findListByCatalogId(int catalogId) {
		return dao.findList(TreeLikeService.byCatalogId(catalogId));
	}

	/**
	 * 
	 * @param catalogId 分类 id
	 * @param start
	 * @param limit
	 * @param status
	 * @return
	 */
	public PageResult<Ads> findPagedList(int catalogId, int start, int limit, int status) {
		Function<String, String> fn = setStatus(status).andThen(BaseService::searchQuery_NameOnly);

		if (catalogId != 0)
			fn = fn.andThen(TreeLikeService.setCatalog(catalogId));

		return dao.findPagedList(start, limit, fn);
	}
}