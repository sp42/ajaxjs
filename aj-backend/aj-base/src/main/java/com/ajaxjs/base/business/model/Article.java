package com.ajaxjs.base.business.model;

import com.ajaxjs.data.jdbc_helper.common.TableName;
import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.IBaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 图文
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("article")
public class Article extends BaseModel implements IBaseModel {
}
