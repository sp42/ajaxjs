package com.ajaxjs.util.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class Templ {
	/**
	 * 
	 */
	private List<Exp> exps = new ArrayList<>();

	/**
	 * 
	 * @param templStr
	 * @return
	 */
	public static Templ of(String templStr) {
		Objects.requireNonNull(templStr, "模板为空");

		Templ templ = new Templ();
		StringBuilder sb = new StringBuilder();
		char[] chars = templStr.toCharArray();
		
		for (int i = 0; i < chars.length; i++) {
			switch (chars[i]) {
			case '#':
			case '$':
				if (i < chars.length - 1 && chars[i + 1] == '{') {
					templ.addExp(Exp.of(sb.toString()));
					sb.setLength(0);
				}
				sb.append(chars[i]);
				break;
			case '}':
				sb.append('}');
				if (sb.length() > 1 && sb.charAt(1) == '{') {
					templ.addExp(Exp.of(sb.toString()));
					sb.setLength(0);
				}
				break;
			default:
				sb.append(chars[i]);
				break;
			}
		}

		if (sb.length() > 0) 
			templ.addExp(Exp.of(sb.toString()));
		

		return templ;
	}

	/**
	 * 
	 * @param exp
	 * @return
	 */
	private Templ addExp(Exp exp) {
		Objects.requireNonNull(exp, "表达式为空");
		exps.add(exp);
		
		return this;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public String render(Map<String, String> context) {
		StringBuilder sb = new StringBuilder(128);
		
		for (Exp exp : exps)
			sb.append(exp.resolve(context));

		return sb.toString();
	}
}
