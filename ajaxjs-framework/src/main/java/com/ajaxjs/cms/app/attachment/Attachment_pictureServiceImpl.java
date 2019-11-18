package com.ajaxjs.cms.app.attachment;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.util.io.FileHelper;

@Bean("Attachment_pictureService")
public class Attachment_pictureServiceImpl extends BaseService<Attachment_picture> implements Attachment_pictureService {
	public Attachment_pictureDao dao = new Repository().bind(Attachment_pictureDao.class);
	
	{
		setUiName("图片");
		setShortName("attachment_picture");
		setDao(dao);
	}

	@Override
	public boolean delete(Attachment_picture bean) {
		FileHelper.delete(bean.getPath()); // 删除文件
		return dao.delete(bean);
	}

	@Override
	public List<Attachment_picture> findByOwner(Long owner) {
		List<Attachment_picture> list = dao.findByOwner(owner);
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

	@Override
	public boolean saveImgIndex(Map<String, Object> map) {
		boolean isOk = true;

		for (String str : map.keySet()) {
			int index = (int) map.get(str);
			Long imgId = Long.parseLong(str);

			if (dao.saveImgIndex(index, imgId) < 0) {
				isOk = false;
			}
		}

		return isOk;
	}

	@Override
	public boolean deleteByOwnerId(Long OwnerId) {
		return dao.deleteByOwnerId(OwnerId);
	}

	@Override
	public List<Attachment_picture> findAttachmentPictureByOwner(Long owner) {
		return dao.findAttachmentPictureByOwner(owner);
	}

}