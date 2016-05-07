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
package com.ajaxjs.web;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.sun.xml.internal.fastinfoset.stax.events.Util;

/**
 * 注意:如果form表单里增加了enctype="multipart/form-data" 这个属性,会导致 HttpServletRequestWrapper 里取不到表单里的内容
 * 表单重复提交处理:
1. 在生成表单时执行如下:
//   session.setAttribute("forum_add", "forum_add");
2. 提交处理时作如下判断
//        if (isRedo(request, "forum_add")) {
//            //提示重复提交,作相关处理
//        }
 * 
《CSRF 攻击的应对之道》
http://www.ibm.com/developerworks/cn/web/1102_niugang_csrf/
《JSP 防止重复提交方法 》
http://blog.csdn.net/seablue_xj/article/details/4934367
《javaEE开发中使用session同步和token机制来防止并发重复提交》
http://www.iflym.com/index.php/code/avoid-conrrent-duplicate-submit-by-use-session-synchronized-and-token.html
 
 * @author frank
 *
 */
public class RequestChecker {
	public RequestChecker(HttpServletRequest request) {
		this.request = request;
	}

	private HttpServletRequest request;

	/**
	 * 利用 Referer 请求头阻止"盗链"
	 * 
	 * @return true 表示为同域
	 */
	public boolean isSameDomain() {
		String referer = request.getHeader("referer");
		if (Util.isEmptyString(referer)) return false;
		
		String site = "http://" + request.getServerName();
		return referer.startsWith(site);
	}
	
	/**
	 * 一种表单重复提交处理方法 http://blog.csdn.net/5iasp/article/details/4268710 
	 * 判断是否为重复提交
	 * 1，检查Session中是否含有指定名字的属性 
	 * 2，如果Session中没有该属性或者属性为空，证明已被处理过，判断为重复提交
	 * 3，否则，证明是第一次处理，并将属性从Session中删除。
	 * 
	 * @param key
	 *            键
	 */
	public boolean isRedo(String key) {
		String value = (String) request.getSession().getAttribute(key);
		if (value == null) {
			return true;
		} else {
			request.getSession().removeAttribute(key);
			return false;
		}
	}
	
	/**
	 * 过滤 XSS字符
	 * 
	 * @param value
	 *            输入的字符串
	 * @return 已过滤字符
	 */
	public String cleanXSS(String value) {
		// You'll need to remove the spaces from the html entities below
		value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
		value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
		value = value.replaceAll("'", "& #39;");
		value = value.replaceAll("eval\\((.*)\\)", "");
		value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']",
				"\"\"");
		value = value.replaceAll("script", "");
		return value;
	}
	
	public String stripXSS(String value) {
		// NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
		// avoid encoded attacks.
		// value = ESAPI.encoder().canonicalize(value);
		
		// Avoid null characters
		value = value.replaceAll("", "");
		
		// Avoid anything between script tags
		Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
		value = scriptPattern.matcher(value).replaceAll("");
		
		// Avoid anything in a src='...' type of e­xpression
		scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
		value = scriptPattern.matcher(value).replaceAll("");
		
		scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
		value = scriptPattern.matcher(value).replaceAll("");
		
		// Remove any lonesome </script> tag
		scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
		value = scriptPattern.matcher(value).replaceAll("");
		
		// Remove any lonesome <script ...> tag
		scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
		value = scriptPattern.matcher(value).replaceAll("");
		
		// Avoid eval(...) e­xpressions
		scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
		value = scriptPattern.matcher(value).replaceAll("");
		
		// Avoid e­xpression(...) e­xpressions
		scriptPattern = Pattern.compile("e­xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
		value = scriptPattern.matcher(value).replaceAll("");
		
		// Avoid javascript:... e­xpressions
		scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
		value = scriptPattern.matcher(value).replaceAll("");
		
		// Avoid vbscript:... e­xpressions
		scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
		value = scriptPattern.matcher(value).replaceAll("");
		
		// Avoid onload= e­xpressions
		scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
		value = scriptPattern.matcher(value).replaceAll("");
		
		return value;
	}
}
