package com.ajaxjs.cms.service;

import java.util.List;

import com.ajaxjs.cms.dao.Ads;
import com.ajaxjs.cms.dao.AdsDao;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.service.CommonService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;

@Bean(value = "AdsService", aop = { CommonService.class })
public class AdsServiceImpl implements AdsService {
	public static AdsDao dao = new DaoHandler().bind(AdsDao.class);

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
	public PageResult<Ads> findList() {
		return null;
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
		return dao.findListByCatelogId(catelogId);
	}
}