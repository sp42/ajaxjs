package com.ajaxjs.cms.app;

import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.IBaseService;

public interface Attachment_pictureService extends IBaseService<Attachment_picture> {

	/**
	 * 根据实体 uid 找到其所拥有的图片
	 * 
	 * @param owner 实体 uid
	 * @return 图片列表
	 */
	List<Attachment_picture> findByOwner(Long owner);

	/**
	 * 根据实体 uid 找到其所拥有的相册图片
	 * 
	 * @param owner
	 * @return
	 */
	public List<Attachment_picture> findAttachmentPictureByOwner(Long owner);

	/**
	 * 根据实体Uid删除图片
	 * 
	 * @param OwnerId
	 * @return
	 * @throws ServiceException
	 */
	boolean deleteByOwnerId(Long OwnerId);

	/**
	 * 修改图片索引
	 * 
	 * @param map
	 * @return
	 */
	boolean saveImgIndex(Map<String, Object> map);

	/**
	 * 頭像
	 */
	public static final int AVATAR = 2;
}