package com.ajaxjs.framework.dao;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.ajaxjs.framework.dao.annotation.Param;
import com.ajaxjs.util.CommonUtil;

/**
 * 
 * @author Frank Cheung
 *
 */
public class SqlFactoryTemplateArgs implements SqlFactory {
	@Override
	public SqlAndArgs toSql(SqlAndArgs s) {
		Object sqlTplArg = null; // SQL 替换模版的参数
		String regexp = "#\\{(\\w+)\\}", first = CommonUtil.regMatch(regexp, s.sql, 1); // 当期仅支持一个替换

		if (first != null) {
			sqlTplArg = getArgValue(s.method, s.args);

			if (sqlTplArg != null)
				s.sql = s.sql.replaceAll(regexp, sqlTplArg.toString());
		}

		return s;
	}

	/**
	 * 
	 * @param method
	 * @param args
	 * @return
	 */
	private static Object getArgValue(Method method, Object[] args) {
		int i = 0;

		for (Annotation[] a : method.getParameterAnnotations()) {
			for (Annotation aa : a) {
				if (Param.class.equals(aa.annotationType())) {
					return args[i];
				}
			}

			i++;
		}

		return null;
	}
}
