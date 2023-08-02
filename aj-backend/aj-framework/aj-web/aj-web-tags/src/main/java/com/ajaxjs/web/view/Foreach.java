/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.web.view;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @author sp42 frank@ajaxjs.com
 * @version 2017年7月8日 下午11:10:04
 */
public class Foreach extends SimpleTagSupport {
	/**
	 * 变量名称
	 */
	private String var;

	/**
	 * 抽象为 Collection。
	 */
	private Collection<Object> collection;

	/**
	 * 判别集合，支持 Collection、Map、Array
	 * 
	 * @param items 输入的集合
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setItems(Object items) { // int[]
		if (items == null)
			return;

		if (items instanceof Collection)
			collection = (Collection<Object>) items;

		if (items instanceof Map)
			collection = ((Map) items).entrySet(); // set

		if (items.getClass().isArray()) {
			collection = new ArrayList<>();
			int length = Array.getLength(items);

			for (int i = 0; i < length; i++) {
				Object value = Array.get(items, i);
				collection.add(value);
			}
		}
	}

	/**
	 * 保存变量
	 * 
	 * @param var 变量
	 */
	public void setVar(String var) {
		this.var = var;
	}

	public String getVar() {
		return var;
	}

	@Override
	public void doTag() throws JspException, IOException {
		if (collection == null || collection.isEmpty())
			return;

		Iterator<Object> it = collection.iterator();
		JspContext context = getJspContext();
		int i = 0;

		context.setAttribute("totalCount", collection.size()); // 写出循环总次数
		String var = getVar();

		if (var == null)
			var = "item"; // 默认

		while (it.hasNext()) {
			Object value = it.next();
			context.setAttribute(var, value); // 每遍历出数据后写出
			context.setAttribute("currentIndex", i++); // 每遍历出数据后写出:当前循环次数
			JspFragment f = getJspBody();

			if (f != null)
				f.invoke(null);
		}
	}
}
