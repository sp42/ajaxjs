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
package com.ajaxjs.web.upload;

import com.ajaxjs.util.StringUtil;

/**
 * 表示上传 item 内容
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class MetaData {
	/**
	 * 表单字段名
	 */
	public String name;
	/**
	 * 文件名
	 */
	public String filename;

	/**
	 * 文件类型
	 */
	public String contentType;

	/**
	 * 文件大小
	 */
	public int contentLength;

	/**
	 * 解析 HTTP 报文，得出 文件名 和文件类型
	 * 
	 * @param dataStr
	 *            HTTP 报文字符串
	 */
	public void parseMeta(String dataStr) {
		name = StringUtil.regMatch("name=\"(\\w+)\"", dataStr, 1);
		if (name == null)
			throw new IllegalArgumentException("你的表单中没有设置一个 name，不能获取字段");

		filename = StringUtil.regMatch("filename=\"([^\"]*)\"", dataStr, 1);
		contentType = StringUtil.regMatch("Content-Type:\\s?([\\w/]+)", dataStr, 1);
	}

	private final static byte[] b = "\n".getBytes();

	public static int get(byte[] dataBytes) {
		int skip = 0;

		for (int i = 0; i < dataBytes.length; i++) {
			int temp = i, j = 0;
			while (dataBytes[temp] == b[j]) {
				temp++;
				j++;
				if (j == b.length) {
					skip++;
					if (skip == 3)
						return i + 3;// why plus 3?

					break;
				}
			}
		}

		return 0;
	}
}
