package com.ajaxjs.app.service;

import java.util.List;
import java.util.function.Function;

import com.ajaxjs.app.catalog.CatalogService;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.annotation.TableName;

@Bean
public class AdsService extends BaseService<Ads> {
	@TableName(value = "entity_ads", beanClass = Ads.class)
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

	public List<Ads> findListByCatalogId(int catalogId) {
		return dao.findList(CatalogService.byCatalogId(catalogId));
	}

	public PageResult<Ads> findPagedList(int catalogId, int start, int limit, int status) {
		Function<String, String> fn = setStatus(status).andThen(BaseService::searchQuery_NameOnly);

		if (catalogId != 0)
			fn = fn.andThen(CatalogService.setCatalog(catalogId));

		return dao.findPagedList(start, limit, fn);
	}
}