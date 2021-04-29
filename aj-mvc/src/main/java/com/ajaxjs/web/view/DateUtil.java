/**
 * Copyright Sp42 frank@ajaxjs.com
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
package com.ajaxjs.web.view;

import java.io.IOException;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.ajaxjs.util.CommonUtil;

/**
 * 格式化日期的标签
 * 
 * @author sp42 frank@ajaxjs.com
 */
public class DateUtil extends SimpleTagSupport {
	/**
	 * 输入的值，可以是任意类型，要经过转换。
	 */
	private Object value;

	/**
	 * 格式化模版，如果为 null，则默认为 采用格式 YYYY-MM-dd HH:MM
	 */
	private String format;

	@Override
	public void doTag() throws JspException, IOException {
		if (value == null)
			return;

		Date date = CommonUtil.Objet2Date(value);

		if (date == null)
			return;

		String format = this.format == null ? CommonUtil.formatDateShorter(date) : CommonUtil.simpleDateFormatFactory(this.format).format(date);

		getJspContext().getOut().write(format);
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
}