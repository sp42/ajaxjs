package com.ajaxjs.cms;

import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;

@TableName(value = "entity_ads", beanClass = Ads.class)
public interface AdsDao extends IBaseDao<Ads> {
	/**
	 * 附件图片
	 */
	@Select("SELECT e.*,  (" + selectCover + ") AS cover, "
			+ " (SELECT path FROM attachment_picture p2 WHERE e.uid = p2.owner AND p2.catelog = 1 ORDER BY p2.id DESC LIMIT 0, 1) AS img " + " FROM ${tableName} e WHERE id = ?")
	@Override
	public Ads findById(Long id);


}