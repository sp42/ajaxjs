package com.ajaxjs.cms.domain;

import java.util.List;

import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;

@TableName(beanClass = DomainEntity.class)
public interface DomainEntityDao extends IBaseDao<DomainEntity> {
	@Select("SELECT id, name, catelog FROM(SELECT * FROM ${tableName} WHERE catelog = ? ORDER BY id DESC LIMIT 0, 3 ) " + 
			"UNION ALL " + 
			"SELECT id, name, catelog FROM(SELECT * FROM ${tableName} WHERE catelog = ? ORDER BY id DESC LIMIT 0, 3 ) " + 
			"UNION ALL " + 
			"SELECT id, name, catelog FROM(SELECT * FROM ${tableName} WHERE catelog = ? ORDER BY id DESC LIMIT 0, 3 )")
	public List<DomainEntity> findHome(int c1, int c2, int c3);
}
