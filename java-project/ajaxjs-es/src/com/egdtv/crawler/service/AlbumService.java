package com.egdtv.crawler.service;

import org.springframework.ui.Model;

import com.ajaxjs.framework.exception.ServiceException;
import com.ajaxjs.framework.service.BaseEsService;
import com.egdtv.crawler.model.Album;

public class AlbumService extends BaseEsService<Album> {
	public AlbumService() {
		setIndexName("crawler");
		setTypeName("audio");
		setUiName("专辑");
	}
	
	@Override
	public Album getById(long id) throws ServiceException {
		Album bean = super.getById(id);
		Model model = getModel(); // 没 model 表示不深入获取信息
		
		if(bean != null && model != null) {
			// 分类
			Integer catalogId = bean.getCatalog();
			System.out.println("model：" + model);
			if(catalogId != null) {
				System.out.println();
				model.addAttribute("catalog", new CatalogService().getById(catalogId));
			}
			// 门户 
			int protalId = bean.getPortalId();
			System.out.println("protalId:" + protalId);
			System.out.println("protalId:" + new PortalService().getById(protalId));
			model.addAttribute("portal", new PortalService().getById(protalId));
			// 爬虫分类
			Common.prepareData(model);
		}
		
		return bean;
	}
}