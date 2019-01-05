package com.ajaxjs.cms;

import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.TableName;

@TableName(value = "entity_ads", beanClass = Ads.class)
public interface AdsDao extends IBaseDao<Ads> {
	


}