package com.ajaxjs.cms.app.attachment;

import java.util.HashMap;
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
	public static final int COMMON_IMG = 1;

	public static final int AVATAR = 2;

	public static final int ALBUM = 3;
	
	public static final int ATTACHMENT = 4;
	
	public static final Map<Integer, String> DICT = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;

		{
			put(0, "普通图片");
			put(COMMON_IMG, "普通图片");
			put(AVATAR, "封面/头像");
			put(ALBUM, "相册图片");
			put(ATTACHMENT, "附件图片");
		}
	};
}