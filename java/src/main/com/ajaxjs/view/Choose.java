/**
 * Copyright 2015 Frank Cheung
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
package com.ajaxjs.view;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * 选择标签之父类标签
 * 
 * @author frank
 *
 */
public class Choose extends SimpleTagSupport {
	private boolean isExcute;

	/**
	 * 用于保存 if 判断是否为 true
	 * 
	 * @return true 表示执行 when 标签内容，否则执行 otherwise 标签内容
	 */
	public boolean isExcute() {
		return isExcute;
	}

	/**
	 * 保存 if 表达式结果
	 * 
	 * @param isExcute
	 *            true 表示执行 when 标签内容，否则执行 otherwise 标签内容
	 */
	public void setExcute(boolean isExcute) {
		this.isExcute = isExcute;
	}

	@Override
	public void doTag() throws JspException, IOException {
		getJspBody().invoke(null);
	}
}
