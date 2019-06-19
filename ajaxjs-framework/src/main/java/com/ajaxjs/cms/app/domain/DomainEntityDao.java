package com.ajaxjs.cms.app.domain;

import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;

@TableName(beanClass = Map.class)
public interface DomainEntityDao extends IBaseDao<Map<String, Object>> {
	@Select("SELECT id, name, catelogId FROM(SELECT * FROM ${tableName} WHERE catelogId = ? ORDER BY id DESC LIMIT 0, 3 ) " + 
			"UNION ALL " + 
			"SELECT id, name, catelogId FROM(SELECT * FROM ${tableName} WHERE catelogId = ? ORDER BY id DESC LIMIT 0, 3 ) " + 
			"UNION ALL " + 
			"SELECT id, name, catelogId FROM(SELECT * FROM ${tableName} WHERE catelogId = ? ORDER BY id DESC LIMIT 0, 3 )")
	public List<Map<String, Object>> findHome(int c1, int c2, int c3);
}
