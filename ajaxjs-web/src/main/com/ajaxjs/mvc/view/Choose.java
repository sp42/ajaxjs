/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.mvc.view;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * 选择标签之父类标签
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class Choose extends SimpleTagSupport {
	/**
	 * 保存是否执行的状态
	 */
	private boolean isExecute;

	/**
	 * 用于保存 if 判断是否为 true
	 * 
	 * @return true 表示执行 when 标签内容，否则执行 otherwise 标签内容
	 */
	public boolean isExecute() {
		return isExecute;
	}

	/**
	 * 保存 if 表达式结果
	 * 
	 * @param isExcute
	 *            true 表示执行 when 标签内容，否则执行 otherwise 标签内容
	 */
	public void setExecute(boolean isExcute) {
		this.isExecute = isExcute;
	}

	@Override
	public void doTag() throws JspException, IOException {
		getJspBody().invoke(null);
	}
}
