package com.ajaxjs.cms;

import java.util.List;

import com.ajaxjs.cms.app.catelog.Catelogable;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.dao.PageResult;

@Bean("AdsService")
public class AdsServiceImpl extends BaseService<Ads> implements AdsService, Catelogable<Ads> {
	public static AdsDao dao = new Repository().bind(AdsDao.class);

	{
		setUiName("广告");
		setShortName("ads");
		setDao(dao);
	}

	@Override
	public PageResult<Ads> findPagedListByCatelogId(int catelogId, int start, int limit) {
		return dao.findPagedListByCatelogId(catelogId, start, limit);
	}

	@Override
	public int getDomainCatelogId() {
		return ConfigService.getValueAsInt("data.adsCatalog_Id");
	}

	@Override
	public List<Ads> findListByCatelogId(int catelogId) {
		return null;
	}
}