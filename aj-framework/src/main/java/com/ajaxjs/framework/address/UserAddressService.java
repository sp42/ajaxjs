package com.ajaxjs.framework.address;

import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.Repository;

@Component
public class UserAddressService extends BaseService<Address> {

	@TableName(value = "entity_address", beanClass = Address.class)
	public static interface UserAddressDao extends IBaseDao<Address> {
		@Select("SELECT a.*, u.name AS userIdName, u.username FROM ${tableName} a LEFT JOIN user u ON u.id = a.userId WHERE 1 = 1 ORDER BY id DESC")
		public PageResult<Address> findPagedList(int start, int limit, Function<String, String> doSql);

		@Select("SELECT id FROM ${tableName} WHERE isDefault = 1")
		public Long getDefaultAddressId();
	}

	public static UserAddressDao DAO = new Repository().bind(UserAddressDao.class);

	{
		setDao(DAO);
	}

	/**
	 * 默认地址是唯一的，如果已经有默认地址，取消其默认值
	 * 
	 * @param bean
	 */
	private static void checkIfExist(Address bean) {
		if (bean.getIsDefault() != null && bean.getIsDefault()) {
			Long defaultId = DAO.getDefaultAddressId();

			if (defaultId != null && defaultId != bean.getId()) { // 已经有默认地址
				Address oldDefault = new Address();
				oldDefault.setId(defaultId);
				oldDefault.setIsDefault(false);
				DAO.update(oldDefault);
			}
		}
	}

	public List<Address> findListByUserId(long userId) {
		return findList(by("userId", userId));
	}

	@Override
	public Long create(Address bean) {
		checkIfExist(bean);
		return super.create(bean);
	}

	@Override
	public int update(Address bean) {
		checkIfExist(bean);
		return super.update(bean);
	}

//	public static JsEngineWrapper AREA_DATA;
//
//	/**
//	 * 初始化一个 js 引擎并缓存之
//	 * 
//	 * @param r 请求对象，用来获取
//	 */
//	public static void initData(HttpServletRequest r) {
//		if (UserAddressService.AREA_DATA == null) {
//			InputStream in = r.getServletContext().getResourceAsStream("/asset/js/China_AREA_full.js");
//			Objects.requireNonNull(in, "未准备好地址 JSON 文件");
//			
//			JsEngineWrapper js = new JsEngineWrapper();
//			js.eval(IoHelper.byteStream2string(in));
//
//			try {
//				in.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//			UserAddressService.AREA_DATA = js;
//		}
//	}
}