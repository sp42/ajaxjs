package com.ajaxjs.cms.app.attachment;

import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.orm.annotation.Update;

@TableName(value = "attachment_picture", beanClass = Attachment_picture.class)
public interface Attachment_pictureDao extends IBaseDao<Attachment_picture> {
	@Delete("DELETE FROM ${tableName} WHERE `owner` = ?")
	boolean deleteByOwnerId(Long ownerUid);

	@Update("UPDATE ${tableName} SET `index` = ? WHERE id = ?")
	public int saveImgIndex(int index, Long imgId);
	
	/**
	 * 实体别名必须为 entry
	 */
	public final static String LINK_COVER = "(SELECT path FROM attachment_picture p1 WHERE entry.uid = p1.owner AND p1.catalog = 1 ORDER BY p1.id DESC LIMIT 0, 1)";
}