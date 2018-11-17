package com.ajaxjs.cms.service;

import java.util.List;

import com.ajaxjs.cms.dao.AdsDao;
import com.ajaxjs.cms.model.Ads;
import com.ajaxjs.cms.service.aop.CommonService;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.framework.service.aop.CacheService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.orm.dao.QueryParams;

@Bean(value = "AdsService", aop = { CommonService.class, CacheService.class })
public class AdsServiceImpl implements AdsService {
	AdsDao dao = new DaoHandler<AdsDao>().bind(AdsDao.class);

	public Ads findById(Long id) {
		return dao.findById(id);
	}

	@Override
	public Long create(Ads bean) {
		return dao.create(bean);
	}

	@Override
	public int update(Ads bean) {
		return dao.update(bean);
	}

	@Override
	public boolean delete(Ads bean) {
		return dao.delete(bean);
	}

	@Override
	public PageResult<Ads> findPagedList(int start, int limit) {
		return dao.findPagedList(start, limit);
	}

	@Override
	public String getName() {
		return "广告";
	}

	@Override
	public String getTableName() {
		return "entity_Ads";
	}

	@Override
	public PageResult<Ads> findPagedList(QueryParams params, int start, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageResult<Ads> findPagedListByCatelogId(int catelogId, QueryParams params, int start, int limit) {
		return dao.findPagedListByCatelogId(catelogId, params, start, limit);
	}

	@Override
	public int getDomainCatelogId() {
		return ConfigService.getValueAsInt("data.adsCatalog_Id");
	}

	@Override
	public List<Ads> findListByCatelogId(int catelogId, QueryParams params) {
		return dao.findListByCatelogId(catelogId, params);
	}

}