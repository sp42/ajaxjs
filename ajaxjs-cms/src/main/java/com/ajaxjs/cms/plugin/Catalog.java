package com.ajaxjs.cms.plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ajaxjs.cms.service.CatelogService;
import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.service.IService;
import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.logger.LogHelper;

public class Catalog {
	private static final LogHelper LOGGER = LogHelper.getLog(Catalog.class);

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

	/**
	 * 把列表（BaseModel 结构）转换为 map，以 id 作为键值。key 本来是 long，为照顾 el 转换为 int
	 * 
	 * @param bean
	 *            实体列表
	 * @return 以 id 作为键值的 map
	 */
	public static Map<Integer, BaseModel> list_bean2map_id_as_key(List<? extends BaseModel> bean) {
		if (CommonUtil.isNull(bean))
			return null;

		Map<Integer, BaseModel> map = new HashMap<>();

		for (BaseModel item : bean)
			map.put(new Long(item.getId()).intValue(), item);

		return map;
	}

	/**
	 * 把列表（Map结构）转换为 map，以 id 作为键值。key 本来是 long，为照顾 el 转换为 int
	 * 
	 * @param list
	 *            实体列表
	 * @return 以 id 作为键值的 map
	 */
	public static Map<Integer, Object> list2map_id_as_key(List<Map<String, Object>> list) {
		if (CommonUtil.isNull(list))
			return null;

		Map<Integer, Object> map = new HashMap<>();

		for (Map<String, Object> item : list)
			map.put(new Long(item.get("id").toString()).intValue(), item);

		return map;
	}
}
