package com.ajaxjs.cms;

import java.util.Map;

import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.TableName;


@TableName(value = "entity_article", beanClass = Map.class)
public interface ArticleDao extends IBaseDao<Map<String, Object>> {
}
