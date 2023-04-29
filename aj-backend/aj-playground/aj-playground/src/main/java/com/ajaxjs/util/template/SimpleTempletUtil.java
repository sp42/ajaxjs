package com.ajaxjs.util.template;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Frank Cheung
 *
 */
public class SimpleTempletUtil {
	public static final String DEFAULT_SPLIT = "$$";

	/**
	 * 使用 context 中对应的值替换 templet 中用 $$ 包围的变量名(也是context的key)
	 * 
	 * @param templet 模板
	 * @param context 用于替换模板中的变量
	 * @return 例如 参数 : dddd$$aaa$$$$bbb$$ccc$$, $$, {<aaa, value1>, <bbb, value2>}
	 *         结果:ddddvalue1value2ccc$$
	 */
	public static String render(String templet, Map<String, String> context) {
		return render(templet, DEFAULT_SPLIT, context);
	}

	/**
	 * 使用context中对应的值替换templet中用split包围的变量名(也是context的key)
	 * 
	 * @param templet 模板
	 * @param split   用于标识变量名的标志
	 * @param context 用于替换模板中的变量
	 * @return 例如 参数 : dddd$$aaa$$$$bbb$$ccc$$, $$, {<aaa, value1>, <bbb, value2>}
	 *         结果:ddddvalue1value2ccc$$
	 */
	public static String render(String templet, String split, Map<String, String> context) {
		Set<String> paramNames = getParamNames(templet, split);

		for (String name : paramNames) {
			String value = context.get(name);
			value = value == null ? "" : value;
			String regex = "\\Q" + split + name + split + "\\E";
			templet = templet.replaceAll(regex, value);
		}

		return templet;
	}

	/**
	 * 根据分割符从模板中取得变量的名字($$变量名$$) eg: $$aaa$$$$bbb$$ccc$$ 返回 aaa,bbb
	 * 
	 * @param templet 模板
	 * @param split   包围变量名的字符串
	 * @return 模板中的变量名
	 */
	public static Set<String> getParamNames(String templet, String split) {
		Set<String> paramNames = new HashSet<>();
		int start = 0, end = 0;

		while (end < templet.length()) {
			start = templet.indexOf(split, end) + split.length();

			if (start == -1)
				break;

			// start += split.length();

			end = templet.indexOf(split, start);

			if (end == -1)
				break;

			paramNames.add(templet.substring(start, end));
			end = end + split.length();
		}

		return paramNames;
	}
}
