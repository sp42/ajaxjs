package com.ajaxjs.cms.section;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 表示那些选中的实体，参与到栏目的那些记录
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class SelectedEntity {
	/**
	 * 从栏目表中收集的实体
	 */
	private List<Long> uids = new ArrayList<>();

	/**
	 * 该实体的表名
	 */
	private String tableName;

	/**
	 * 
	 * @param typeId 实体类型 id
	 */
	public SelectedEntity(int typeId) {
		switch (typeId) {
		case 1:
			tableName = "entity_article";
			break;
		case 2:
			tableName = "shop_goods";
			break;
		case 3:
			tableName = "entity_article";
			break;
		case 4:
			tableName = "entity_article";
			break;
		}
	}

	/**
	 * 到各自的表中查询实体
	 * 
	 * @return 实体列表，实体是特定的类型
	 */
	public List<Map<String, Object>> query() {
		String _uids = "";
		for (Long uid : uids)
			_uids += uid + ",";

		String uidsSql = _uids.substring(0, _uids.length() - 1); // 删除最后一个 ,
		List<Map<String, Object>> list = SectionService.dao.queryAnyTable(sql -> sql.replace("#tableName#", tableName).replace("?", uidsSql));

		return list;
	}

	public List<Long> getUids() {
		return uids;
	}

	public void setUids(List<Long> uids) {
		this.uids = uids;
	}

}