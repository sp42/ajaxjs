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
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * 迭代标签
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class Loop extends SimpleTagSupport {
	/**
	 * 保存迭代次数
	 */
	private int cnt;

	/**
	 * 要迭代的次数
	 * 
	 * @param cnt
	 *            迭代的次数
	 */
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	@Override
	public void doTag() throws JspException, IOException {
		JspFragment body = getJspBody();

		for (int i = 0; i < cnt; i++) {
			getJspContext().setAttribute("loopcnt", i + 1); // 当前循环次数
			body.invoke(null);
		}
	}
}
