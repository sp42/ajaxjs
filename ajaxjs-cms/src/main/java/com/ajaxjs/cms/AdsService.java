package com.ajaxjs.cms;

import java.util.List;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.annotation.TableName;

@Bean("AdsService")
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

	@Override
	public Ads findById(Long id) {
		return dao.findById_cover(id);
	}

	public int getDomainCatalogId() {
		return ConfigService.getValueAsInt("data.adsCatalog_Id");
	}

	public List<Ads> findListByCatelogId(int catelogId) {
		return dao.findList_Cover(BaseService.addWhere("catelogId" + catelogId));
	}
}