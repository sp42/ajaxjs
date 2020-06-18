package com.ajaxjs.user.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.jsonparser.JsEngineWrapper;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.user.model.UserAddress;
import com.ajaxjs.util.io.IoHelper;

@Bean
public class UserAddressService extends BaseService<UserAddress> {

	@TableName(value = "user_address", beanClass = UserAddress.class)
	public static interface UserAddressDao extends IBaseDao<UserAddress> {
		@Select("SELECT a.*, u.name AS userIdName, u.username FROM ${tableName} a LEFT JOIN user u ON u.id = a.userId WHERE 1 = 1 ORDER BY id DESC")
		public PageResult<UserAddress> findPagedList(int start, int limit, Function<String, String> doSql);

		@Select("SELECT id FROM ${tableName} WHERE isDefault = 1")
		public Long getDefaultAddressId();
	}

	public static UserAddressDao dao = new Repository().bind(UserAddressDao.class);

	{
		setUiName("用户地址簿");
		setShortName("UserAddress");
		setDao(dao);
	}

	/**
	 * 默认地址是唯一的，如果已经有默认地址，取消其默认值
	 * 
	 * @param bean
	 */
	private static void checkIfExist(UserAddress bean) {
		if (bean.getIsDefault()) {
			Long defaultId = dao.getDefaultAddressId();

			if (defaultId != null && defaultId != bean.getId()) { // 已经有默认地址
				UserAddress oldDefault = new UserAddress();
				oldDefault.setId(defaultId);
				oldDefault.setIsDefault(false);
				dao.update(oldDefault);
			}
		}
	}

	public List<UserAddress> findListByUserId(long userId) {
		return findList(by("userId", userId));
	}

	@Override
	public Long create(UserAddress bean) {
		checkIfExist(bean);
		return super.create(bean);
	}

	@Override
	public int update(UserAddress bean) {
		checkIfExist(bean);
		return super.update(bean);
	}

	public static JsEngineWrapper AREA_DATA;

	/**
	 * 初始化一个 js 引擎并缓存之
	 * 
	 * @param r 请求对象，用来获取
	 */
	public static void initData(HttpServletRequest r) {
		if (UserAddressService.AREA_DATA == null) {
			InputStream in = r.getServletContext().getResourceAsStream("/asset/js/China_AREA_full.js");
			Objects.requireNonNull(in, "未准备好地址 JSON 文件");
			
			JsEngineWrapper js = new JsEngineWrapper();
			js.eval(IoHelper.byteStream2string(in));

			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			UserAddressService.AREA_DATA = js;
		}
	}
}