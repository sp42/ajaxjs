package com.ajaxjs.app.service.section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ajaxjs.util.CommonUtil;

/**
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class SectionData extends HashMap<Integer, SelectedEntity> {
	private static final long serialVersionUID = 1164227271752733138L;

	/**
	 * 
	 * @param cols 本身的栏目数据
	 */
	public SectionData(List<Section> cols) {
		this.cols = cols;
	}

	// 本身的栏目数据
	private List<Section> cols;

	// 从各个表查询回来的数据组合在一起
	private List<Map<String, Object>> union = new ArrayList<>();

	/**
	 * 
	 * @param uid
	 * @return
	 */
	private Map<String, Object> findByUid(long uid) {
		for (Map<String, Object> entity : union) {
			if (uid == (long) entity.get("uid"))
				return entity;
		}

		return null;
	}

	/**
	 * 
	 * @return
	 */
	public List<Section> getListByCatalogId() {
		for (Section col : cols) {
			Integer typeId = col.getTypeId();
			SelectedEntity section;

			if (!containsKey(typeId)) {
				section = new SelectedEntity(typeId);
				put(typeId, section);
			} else {
				section = get(typeId);
			}

			section.getUids().add(col.getEntityUid());
		}

		for (Integer typeId : keySet()) {
			union.addAll(get(typeId).query());
		}

		if (union.size() != cols.size())
			throw new RuntimeException("栏目记录数与实体总数不匹配！");

		for (Section col : cols) {
			Map<String, Object> entity = findByUid(col.getEntityUid());
			Object id = entity.get("id");
			col.setEntityId((long) id);

			if (CommonUtil.isEmptyString(col.getName()))
				col.setName(entity.get("name").toString());

			if (CommonUtil.isEmptyString(col.getCover()))
				col.setCover(entity.get("cover").toString());
		}

		return cols;
	}
}
