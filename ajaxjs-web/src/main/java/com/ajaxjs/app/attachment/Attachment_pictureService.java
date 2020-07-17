package com.ajaxjs.app.attachment;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.util.ioc.Component;

@Component
public class Attachment_pictureService extends BaseService<Attachment_picture> {
	public Attachment_pictureDao dao = new Repository().bind(Attachment_pictureDao.class);

	{
		setUiName("图片");
		setShortName("attachment_picture");
		setDao(dao);
	}

	/**
	 * 根据实体 uid 找到其所拥有的图片
	 * 
	 * @param owner 实体 uid
	 * @return 图片列表
	 */
	public List<Attachment_picture> findByOwner(Long owner) {
		List<Attachment_picture> list = dao.findList(by("owner", owner));

		if (null != list) {
			Collections.sort(list, new Comparator<Attachment_picture>() {
				@Override
				public int compare(Attachment_picture pic1, Attachment_picture pic2) {
					if (pic1.getIndex() == null)
						return -1;
					if (pic2.getIndex() == null)
						return -1;

					if (pic1.getIndex() > pic2.getIndex())
						return 1;

					if (pic1.getIndex() == pic2.getIndex())
						return 0;

					return -1;
				}
			});
		}

		return list;
	}

	/**
	 * 根据实体 uid 找到其所拥有的相册图片
	 * 
	 * @param owner 实体 uid
	 * @return 图片列表
	 */
	public List<Attachment_picture> findAttachmentPictureByOwner(Long owner) {
		return dao.findList(by("owner", owner).andThen(by("catalogId", Attachment_pictureService.ATTACHMENT)));
	}

	/**
	 * 修改图片索引
	 * 
	 * @param map
	 * @return
	 */
	public boolean saveImgIndex(Map<String, Object> map) {
		boolean isOk = true;

		for (String str : map.keySet()) {
			int index = (int) map.get(str);
			Long imgId = Long.parseLong(str);

			if (dao.saveImgIndex(index, imgId) < 0)
				isOk = false;
		}

		return isOk;
	}

	@Override
	public boolean delete(Attachment_picture bean) {
//		FileHelper.delete(bean.getPath()); // 删除文件
		return dao.delete(bean);
	}

	/**
	 * 根据实体Uid删除图片
	 * 
	 * @param owner 实体 uid
	 * @return
	 */
	public boolean deleteByOwnerId(Long OwnerId) {
		return dao.deleteByOwnerId(OwnerId);
	}

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