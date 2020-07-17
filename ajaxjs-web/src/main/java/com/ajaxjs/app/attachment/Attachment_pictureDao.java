package com.ajaxjs.app.attachment;

import com.ajaxjs.sql.annotation.Delete;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.annotation.Update;
import com.ajaxjs.sql.orm.IBaseDao;

@TableName(value = "attachment_picture", beanClass = Attachment_picture.class)
public interface Attachment_pictureDao extends IBaseDao<Attachment_picture> {
	@Delete("DELETE FROM ${tableName} WHERE `owner` = ?")
	boolean deleteByOwnerId(Long ownerUid);

	@Update("UPDATE ${tableName} SET `index` = ? WHERE id = ?")
	public int saveImgIndex(int index, Long imgId);
	
	/**
	 * 实体别名必须为 entry
	 */
	public final static String LINK_COVER = "(SELECT path FROM attachment_picture p1 WHERE entry.uid = p1.owner AND p1.catalogId = 1 ORDER BY p1.id DESC LIMIT 0, 1)";
}