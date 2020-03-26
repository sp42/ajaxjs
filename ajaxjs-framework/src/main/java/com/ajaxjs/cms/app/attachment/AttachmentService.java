package com.ajaxjs.cms.app.attachment;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.TableName;

@Bean
public class AttachmentService extends BaseService<Attachement> {
	@TableName(value = "attachment", beanClass = Attachement.class)
	public static interface AttachmentDao extends IBaseDao<Attachement> {
	}
	
	public static AttachmentDao dao = new Repository().bind(AttachmentDao.class);
	
	{
		setUiName("通用附件");
		setShortName("attachment");
		setDao(dao);
	}

}