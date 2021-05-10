package com.ajaxjs.jxc.base.service;

import java.util.function.Function;

import com.ajaxjs.entity.service.TreeLikeService;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.jxc.base.model.Supplier;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.PageResult;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.util.ioc.Component;

@Component
public class SupplierService extends BaseService<Supplier> {
	@TableName(value = "jxc_基础_供应商主文件", beanClass = Supplier.class)
	public static interface SupplierDao extends IBaseDao<Supplier> {
		@Select("SELECT e.*, b.name AS 银行名称, b.account AS 银行账号,  u.name AS 采购员name, u2.name AS 供应商会员name FROM ${tableName} e"
				+ " LEFT JOIN entity_bank b ON e.银行码  = b.id LEFT JOIN user u ON e.`采购员` = u.id " + "LEFT JOIN user u2 ON e.userId = u2.id WHERE e.id = ?")
		@Override
		public Supplier findById(Long id);

		@Select("SELECT e.id, e.name, e.catalogId, e.createDate, e.stat, a.name AS 联系人, a.phone AS 联系电话 FROM ${tableName} e "
				+ "LEFT JOIN entity_address a ON e.addressId = a.id " + WHERE_REMARK_ORDER)
		@Override
		public PageResult<Supplier> findPagedList(int start, int limit, Function<String, String> sqlHandler);
	}

	public static SupplierDao dao = new Repository().bind(SupplierDao.class);

	{
		setUiName("供应商");
		setShortName("Supplier");
		setDao(dao);
	}

	public static final int CATALOGID = 66;

	public PageResult<Supplier> findPagedList(int catalogId, int start, int limit) {
		Function<String, String> handler = TreeLikeService.setCatalog(catalogId, CATALOGID);
		handler = BaseService::searchQuery_NameOnly;
		handler = handler.andThen(BaseService::betweenCreateDate);

		return findPagedList(start, limit, handler);
	}
}