package com.ajaxjs.website.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.sql.annotation.Delete;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.annotation.Update;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.website.model.Attachment;

@Component
public class AttachmentService extends BaseService<Attachment> {
	@TableName(value = "common_attachment", beanClass = Attachment.class)
	public interface AttachmentDao extends IBaseDao<Attachment> {
		@Delete("DELETE FROM ${tableName} WHERE `owner` = ?")
		boolean deleteByOwnerId(Long ownerUid);

		@Update("UPDATE ${tableName} SET `index` = ? WHERE id = ?")
		public int saveImgIndex(int index, Long imgId);

		/**
		 * 实体别名必须为 entry
		 */
		public final static String LINK_COVER = "(SELECT path FROM ${tableName} p1 WHERE entry.uid = p1.owner AND p1.catalogId = 1 ORDER BY p1.id DESC LIMIT 0, 1)";
	}

	public static AttachmentDao dao = new Repository().bind(AttachmentDao.class);

	{
		setUiName("通用附件");
		setShortName("attachment");
		setDao(dao);
	}

	/**
	 * 根据实体 uid 找到其所拥有的图片
	 * 
	 * @param owner 实体 uid
	 * @return 图片列表
	 */
	public List<Attachment> findByOwner(Long owner) {
		List<Attachment> list = dao.findList(by("owner", owner));

		if (null != list) {
			Collections.sort(list, new Comparator<Attachment>() {
				@Override
				public int compare(Attachment pic1, Attachment pic2) {
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
	public List<Attachment> findAttachmentByOwner(Long owner) {
		return dao.findList(by("owner", owner).andThen(by("catalogId", AttachmentService.ATTACHMENT)));
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
	public boolean delete(Attachment bean) {
//		FileHelper.delete(bean.getPath()); // 删除文件
		return dao.delete(bean);
	}

	/**
	 * 根据实体 uid 删除图片
	 * 
	 * @param ownerId 实体 uid
	 * @return
	 */
	public boolean deleteByOwnerId(Long ownerId) {
		return dao.deleteByOwnerId(ownerId);
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