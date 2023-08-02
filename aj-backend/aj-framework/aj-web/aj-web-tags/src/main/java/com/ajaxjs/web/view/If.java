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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * 判断 if 逻辑
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class If extends SimpleTagSupport {
	/**
	 * true 表示执行 when 标签内容，否则执行 otherwise 标签内容
	 */
	private boolean test;

	/**
	 * 用于保存 if 判断是否为 true
	 * 
	 * @param test true 表示执行 when 标签内容，否则执行 otherwise 标签内容
	 */
	public void setTest(boolean test) {
		this.test = test;
	}

	@Override
	public void doTag() throws JspException, IOException {
		if (test)
			getJspBody().invoke(null);
	}
}
