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
package com.ajaxjs.web.security.filter;

/**
 * 
 * @author Frank
 *
 */
public class RePostFilter {

	/**
	 * 一种表单重复提交处理方法 http://blog.csdn.net/5iasp/article/details/4268710 判断是否为重复提交
	 * 1，检查Session中是否含有指定名字的属性 2，如果Session中没有该属性或者属性为空，证明已被处理过，判断为重复提交
	 * 3，否则，证明是第一次处理，并将属性从Session中删除。 1. 在生成表单时执行如下: //
	 * session.setAttribute("forum_add", "forum_add"); 2. 提交处理时作如下判断 // if
	 * (isRedo(request, "forum_add")) { // //提示重复提交,作相关处理 // }
	 * 
	 * 《JSP 防止重复提交方法 》 http://blog.csdn.net/seablue_xj/article/details/4934367
	 * 《javaEE开发中使用session同步和token机制来防止并发重复提交》
	 * http://www.iflym.com/index.php/code/avoid-conrrent-duplicate-submit-by-
	 * use-session-synchronized-and-token.html
	 * 
	 * @param key
	 *            键
	 * @return
	 */
	public boolean isRedo(String key) {
		Object obj = getSession().getAttribute(key);
		if (obj == null) {
			return true;
		} else {
			getSession().removeAttribute(key);
			return false;
		}
	}
}
