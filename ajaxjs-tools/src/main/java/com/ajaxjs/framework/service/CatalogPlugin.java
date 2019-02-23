package com.ajaxjs.framework.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.service.IService;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.logger.LogHelper;

public class CatalogPlugin {
	private static final LogHelper LOGGER = LogHelper.getLog(CatalogPlugin.class);

	@SuppressWarnings("unchecked")
	public static void doAfter(IService<?, ?> obj, Object returnObj) {
		if(obj instanceof CatelogService) {
			return; // 排除自己
		}
		
		PageResult<Map<String, Object>> result = (PageResult<Map<String, Object>>) returnObj;
		Object o = ReflectUtil.executeMethod(obj, "getCatalog");
		if (o == null) {
			return;
		}
		
		List<? extends BaseModel> l = (List<? extends BaseModel>) o;
		Map<Integer, BaseModel> catalogMap = list_bean2map_id_as_key(l);

		for (Map<String, Object> map : result) {
			BaseModel catalogBean = catalogMap.get((int) map.get("catalog"));
			
			if (catalogBean == null) {
				LOGGER.warning("找不到对应的分类，实体分类id 为：" + map.get("catalog"));
			} else {
				map.put("catalogName", catalogBean.getName());
			}
		}
	}

	
}
