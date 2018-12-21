package com.ajaxjs.cms.app.attachment;

import java.util.List;

import com.ajaxjs.framework.DataDict;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.orm.annotation.Update;

@TableName(value = "attachment_picture", beanClass = Attachment_picture.class)
public interface Attachment_pictureDao extends IBaseDao<Attachment_picture> {
	public final static String selectByUid = "SELECT path FROM attachment_picture WHERE catelog = 2 AND owner = e.uid LIMIT  0, 1";

	/**
	 * 实体别名必须为 entry
	 */
	public final static String selectCover = "(SELECT path FROM attachment_picture p1 WHERE entry.uid = p1.owner AND p1.catelog = " + DataDict.PIC_COVER + " ORDER BY p1.id DESC LIMIT 0, 1)";

	@Select("SElECT * FROM ${tableName} WHERE owner = ?")
	public List<Attachment_picture> findByOwner(Long owner);

	@Delete("DELETE FROM ${tableName} WHERE `owner` = ?")
	boolean deleteByOwnerId(Long ownerUid);

	@Update("UPDATE ${tableName} SET `index` = ? WHERE id = ?")
	public int saveImgIndex(int index, Long imgId);
}