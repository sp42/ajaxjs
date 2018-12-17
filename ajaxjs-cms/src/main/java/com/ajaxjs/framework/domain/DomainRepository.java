package com.ajaxjs.framework.domain;

import java.util.List;

import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;


@TableName(value="entity_article", beanClass = Domain.class)
public interface DomainRepository extends IBaseDao<Domain> {
	@Select("SELECT id, name, catelog FROM(SELECT * FROM ${tableName} WHERE catelog = ? ORDER BY id DESC LIMIT 0, 3 ) " + "UNION ALL "
			+ "SELECT id, name, catelog FROM(SELECT * FROM ${tableName} WHERE catelog = ? ORDER BY id DESC LIMIT 0, 3 ) " + "UNION ALL "
			+ "SELECT id, name, catelog FROM(SELECT * FROM ${tableName} WHERE catelog = ? ORDER BY id DESC LIMIT 0, 3 )")
	public List<Domain> findHome(int c1, int c2, int c3);

}