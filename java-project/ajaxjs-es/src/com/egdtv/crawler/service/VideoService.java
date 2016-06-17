package com.egdtv.crawler.service;

import org.springframework.ui.Model;

import com.ajaxjs.framework.exception.ServiceException;
import com.ajaxjs.framework.service.BaseEsService;
import com.egdtv.crawler.model.Video;

public class VideoService extends BaseEsService<Video> {
	public VideoService() {
		setIndexName("crawler");
		setTypeName("video");
		setUiName("视频");
	}
	
	@Override
	public Video getById(long id) throws ServiceException {
		Video bean = super.getById(id);
		
		if(bean != null && getModel() != null) {
			Model model = getModel();
			Common.getLinkDataForInfo(bean, model);
		}
		
		return bean;
	}
}
