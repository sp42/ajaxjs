package com.ajaxjs.js.jsonparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.Value;

/**
 * 主要的方法入口 parse()。 还有其他的一些工具方法。
 */
public class JSONParser {
	/**
	 * 是否空格字符
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isSpace(char c) {
		return c == ' ' || c == '\t' || c == '\n';
	}

	/**
	 * 是否字符或者下划线
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isLetterUnderline(char c) {
		return (c >= 'a' && c <= 'z') || c == '_';
	}

	/**
	 * 是否数字字符
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isNum(char c) {
		return c >= '0' && c <= '9';
	}

	/**
	 * 是否数字字符或小数
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isDecimal(char c) {
		return isNum(c) || c == '.';
	}

	/**
	 * 是否字符或者下划线或下划线
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isNumLetterUnderline(char c) {
		return isLetterUnderline(c) || isNum(c) || c == '_';
	}

	/**
	 * 是否引号字符串，JSON 支持 " 和 ' 两种的字符串字面量
	 * 
	 * @param c
	 * @return
	 */
	public static boolean hasQuoataion(char c) {
		return c != '\"' && c != '\'';
	}



	/**
	 * 这是一个更加的 json 解析方法 就算是{'a:a{dsa}':"{fdasf[dd]}"} 这样的也可以处理
	 * 当然{a:{b:{c:{d:{e:[1,2,3,4,5,6]}}}}}更可以处理
	 * 
	 * @param jsonstring
	 *            合法格式的json字符串
	 * @return 有可能map有可能是list
	 */
	public static Object json2Map2(String jsonStr) {
		if (StringUtil.isEmptyString(jsonStr))
			return null;

		Stack<Map<String, Object>> maps = new Stack<>(); // 用来保存所有父级对象
		Stack<List<Object>> lists = new Stack<>(); // 用来表示多层的list对象
		Stack<Boolean> isList = new Stack<>();// 判断是不是list
		Stack<String> keys = new Stack<>(); // 用来表示多层的key

		boolean hasQuoataion = false; // 是否有引号
		String keytmp = null;
		Object valuetmp = null;
		StringBuilder builder = new StringBuilder();
		char[] cs = jsonStr.toCharArray();

		for (int i = 0; i < cs.length; i++) {
			char c = cs[i];

			if (c == ' ') // 忽略空格
				continue;

			if (hasQuoataion) {
				if (hasQuoataion(cs[i]))
					builder.append(cs[i]);
				else
					hasQuoataion = false;

				continue;
			}

			switch (cs[i]) {
			case '{': // 如果是 { map 进栈

				maps.push(new HashMap<String, Object>());
				isList.push(false);
				continue;
			case '\'':
			case '\"':
				hasQuoataion = true;
				continue;
			case ':':// 如果是：表示这是一个属性建，key进栈

				keys.push(builder.toString());
				builder = new StringBuilder();
				continue;
			case '[':

				lists.push(new ArrayList<Object>());
				isList.push(true);
				continue;
			case ',':
				// 这是一个分割，因为可能是简单地 string 的键值对，也有可能是 string=map 的键值对，因此 valuetmp 使用 object 类型；
				// 如果valuetmp是null 应该是第一次，如果value不是空有可能是string，那是上一个键值对，需要重新赋值
				// 还有可能是map对象，如果是map对象就不需要了
				if (builder.length() > 0)
					valuetmp = builder.toString();
				builder = new StringBuilder();

				boolean listis = isList.peek();
				if (!listis) {
					keytmp = keys.pop();
					if (valuetmp instanceof String)
						maps.peek().put(keytmp, Value.toJavaValue(valuetmp.toString())); // 保存 Map 的 Value
					else
						maps.peek().put(keytmp, valuetmp);
				} else
					lists.peek().add(valuetmp);

				continue;
			case ']':

				isList.pop();

				if (builder.length() > 0)
					valuetmp = builder.toString();

				builder = new StringBuilder();
				lists.peek().add(valuetmp);
				valuetmp = lists.pop();
				continue;
			case '}':

				isList.pop();
				// 这里做的和，做的差不多，只是需要把valuetmp=maps.pop();把map弹出栈
				keytmp = keys.pop();

				if (builder.length() > 0)
					valuetmp = builder.toString();

				builder = new StringBuilder();
				maps.peek().put(keytmp, valuetmp);
				valuetmp = maps.pop();
				continue;
			default:
				builder.append(cs[i]);
				continue;
			}

		}

		return valuetmp;
	}
}
