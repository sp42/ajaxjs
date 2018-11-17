package com.ajaxjs.cms.service;

import java.util.List;
import java.util.Map;

import com.ajaxjs.cms.model.Attachment_picture;
import com.ajaxjs.framework.service.IService;
import com.ajaxjs.framework.service.ServiceException;

public interface Attachment_pictureService extends IService<Attachment_picture, Long> {

	/**
	 * 根据实体 uid 找到其所拥有的图片
	 * 
	 * @param owner 实体 uid
	 * @return 图片列表
	 */
	List<Attachment_picture> findByOwner(Long owner);
	
	/**
	 * 根据实体Uid删除图片
	 * @param OwnerId
	 * @return
	 * @throws ServiceException
	 */
	boolean deleteByOwnerId(Long OwnerId) throws ServiceException;

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