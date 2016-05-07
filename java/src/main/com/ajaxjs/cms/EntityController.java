package com.ajaxjs.cms;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class EntityController {
	private static final com.ajaxjs.util.LogHelper LOGGER = com.ajaxjs.util.LogHelper.getLog(EntityController.class);
	
	/**
	 * 分类界面
	 * @return
	 */
	@RequestMapping(value = "/cms/catalog", method = RequestMethod.GET)
	public String catalogUI() {
		LOGGER.info("分类界面");
		return "common/entity/catalog";
	}
}
