package com.ajaxjs.app.attachment;

import java.util.List;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.Repository;

@Bean
public class AttachmentService extends BaseService<Attachment> {
	@TableName(value = "attachment", beanClass = Attachment.class)
	public static interface AttachmentDao extends IBaseDao<Attachment> {
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
	public List<Attachment> findByOwner(Long uid) {
		List<Attachment> list = dao.findList(by("owner", uid));
		return list;
	}
}