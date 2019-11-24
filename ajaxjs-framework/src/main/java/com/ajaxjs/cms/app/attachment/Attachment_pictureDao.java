package com.ajaxjs.cms.app.attachment;

import java.util.List;

import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.orm.annotation.Update;

@TableName(value = "attachment_picture", beanClass = Attachment_picture.class)
public interface Attachment_pictureDao extends IBaseDao<Attachment_picture> {
	@Select("SElECT * FROM ${tableName} WHERE owner = ?")
	public List<Attachment_picture> findByOwner(Long owner);

	@Delete("DELETE FROM ${tableName} WHERE `owner` = ?")
	boolean deleteByOwnerId(Long ownerUid);

	@Update("UPDATE ${tableName} SET `index` = ? WHERE id = ?")
	public int saveImgIndex(int index, Long imgId);

	@Select("SElECT * FROM ${tableName} WHERE owner = ? AND catalog = " + Attachment_pictureService.ATTACHMENT)
	public List<Attachment_picture> findAttachmentPictureByOwner(Long owner);
}