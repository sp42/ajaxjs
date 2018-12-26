package com.ajaxjs.cms;

import com.ajaxjs.framework.EntityMap;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.TableName;


@TableName(value = "entity_article", beanClass = EntityMap.class)
public interface ArticleDao extends IBaseDao<EntityMap> {
}
