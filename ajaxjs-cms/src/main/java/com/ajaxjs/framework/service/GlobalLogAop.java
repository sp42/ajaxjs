package com.ajaxjs.framework.service;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ajaxjs.cms.dao.GlobalLogDao;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.ioc.Aop;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.util.Encode;

/**
 * 全局的操作日志
 * 
 * @author sp42 frank@ajaxjs.com
 *
 * @param <T>
 * @param <ID>
 * @param <S>
 */
public class GlobalLogAop<T, ID extends Serializable, S extends IService<T, ID>> extends Aop<S> {
	public static final GlobalLogDao dao = new DaoHandler().bind(GlobalLogDao.class);

	@Override
	public Object before(S o, Method method, String methodName, Object[] args) {
		return null;
	}

	@Override
	public void after(S o, Method method, String methodName, Object[] args, Object returnObj) {
		if (!ConfigService.getValueAsBool("System.isGlobalLog")) {
			return; // 系统关闭了，无须记录日志
		}

		if ("create".equals(methodName) || "update".equals(methodName) || "delete".equals(methodName)) {
			Map<String, Object> map = new HashMap<>();
			map.put("name", methodName);
			map.put("content", getContent(o, methodName));
			map.put("userId", 10000); // TODO
			map.put("ip", getIp());
			map.put("_sql", getSql());
			map.put("createDate", new Date());

			dao.create(map);
		}
	}

	/**
	 * 获取上下文的所有 SQL
	 * 
	 * @return SQL
	 */
	private static String getSql() {
		List<String> sqls = JdbcConnection.getSqls();
		String sql = String.join(";\\n", sqls);
		JdbcConnection.cleanSql();

		return Encode.base64Encode(sql);// for escape
	}

	private static String getIp() {
		try {
			return MvcRequest.getMvcRequest().getIp();
		} catch (Throwable e) {
			return null; // for junit easily
		}
	}

	/**
	 * 生成操作内容
	 * 
	 * @param o
	 *            业务对象
	 * @param methodName
	 *            方法名称
	 * @return 操作内容
	 */
	private String getContent(S o, String methodName) {
		String content = o.toString().split("@")[0];

		switch (methodName) {
		case "create":
			content += " 创建";
			break;
		case "update":
			content += " 修改";
			break;
		case "delete":
			content += " 删除";
			break;
		}

		content += o.getName();

		return content;
	}
}
