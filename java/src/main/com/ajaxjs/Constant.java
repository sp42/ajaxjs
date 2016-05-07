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
package com.ajaxjs;

import java.io.File;

/**
 * 通用的常量
 * @author Frank Cheung
 *
 */
public interface Constant {
	public static final String ConsoleDiver = System.getProperty("line.separator") + "---------------------------------";
	public static final String printObject_or_status = "--------> ";
	public static final String json_Err_KeyName = "errMsg";
	public static final String page_css_lessFile = "/asset/less/main.less";
	public static final String JsSrc_local = "http://%s";
	public static final String file_pathSeparator = File.separator;
	public static final String sql_query_zero = "此次查询没有符合条件的任何数据。";
	public static final String outputKeyName = "output";
	public static final String encoding_UTF8 = "UTF-8";
	public static final String lineFeet = "\r\n";
	public final static char newline = '\n';
	
	/**
	 * 空字符串常量
	 */
	public static final String emptyString = "";
}
