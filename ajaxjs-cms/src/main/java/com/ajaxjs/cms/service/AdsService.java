package com.ajaxjs.cms.service;

import com.ajaxjs.cms.app.catelog.Catelogable;
import com.ajaxjs.cms.dao.Ads;
import com.ajaxjs.framework.service.IService;

public interface AdsService extends IService<Ads, Long>, Catelogable<Ads> {

}