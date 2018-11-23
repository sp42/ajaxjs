package com.ajaxjs.cms.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.ajaxjs.cms.dao.Attachment_pictureDao;
import com.ajaxjs.cms.model.Attachment_picture;
import com.ajaxjs.cms.service.aop.CommonService;
import com.ajaxjs.cms.service.aop.GlobalLogAop;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.util.io.FileUtil;

@Bean(value = "Attachment_pictureService", aop = { CommonService.class, GlobalLogAop.class })
public class Attachment_pictureServiceImpl implements Attachment_pictureService {
	Attachment_pictureDao dao = new DaoHandler().bind(Attachment_pictureDao.class);

	@Override
	public Attachment_picture findById(Long id) {
		return dao.findById(id);
	}

	@Override
	public Long create(Attachment_picture bean) {
		return dao.create(bean);
	}

	@Override
	public int update(Attachment_picture bean) {
		return dao.update(bean);
	}

	@Override
	public boolean delete(Attachment_picture bean) {
		new FileUtil().setFilePath(bean.getPath()).delete(); // 删除文件
		return dao.delete(bean);
	}

	@Override
	public PageResult<Attachment_picture> findPagedList(int start, int limit) {
		return dao.findPagedList(start, limit);
	}

	@Override
	public List<Attachment_picture> findList() {
		return null;
	}

	@Override
	public List<Attachment_picture> findByOwner(Long owner) {
		List<Attachment_picture> list = dao.findByOwner(owner);
		if (null != list) {
			Collections.sort(list, new Comparator<Attachment_picture>() {
				@Override
				public int compare(Attachment_picture pic1, Attachment_picture pic2) {
					if(pic1.getIndex() == null)
						return -1;
					if(pic2.getIndex() == null)
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
	public String getName() {
		return "图片";
	}

	@Override
	public String getTableName() {
		return "attachment_picture";
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

}