package com.ajaxjs.jxc.base.service;

import java.util.function.Function;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.jxc.base.model.Bank;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.PageResult;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.util.ioc.Component;

@Component
public class BankService extends BaseService<Bank> {
	@TableName(value = "jxc_bank", beanClass = Bank.class)
	public static interface SupplierDao extends IBaseDao<Bank> {
	}

	public static SupplierDao dao = new Repository().bind(SupplierDao.class);

	{
		setUiName("银行");
		setShortName("Bank");
		setDao(dao);
	}

	public PageResult<Bank> findPagedList(int catalogId, int start, int limit) {
		Function<String, String> handler = BaseService::searchQuery_NameOnly;
		handler = handler.andThen(BaseService::betweenCreateDate);

		return findPagedList(start, limit, handler);
	}
}