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
package com.ajaxjs.javatools.net;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.ajaxjs.core.Util;
import com.ajaxjs.framework.web.RequestHelper;
import com.ajaxjs.util.Tools;

/**
 * 检查HTTP 的 Basic认证代码示例
 * 
 * @author frank
 * 
 */
public class Auth {
	
	public static String[] loginByHTTP_Basic_Auth(RequestHelper request){
		String Authorization = request.getHeader("Authorization");
		
		String[] username_password = null;
		
		if(Util.isEmptyString(Authorization)){
			request.pushErr("HTTP 请求缺少 Authorization 字段，非法请求");
		}else{
			String[] basicArray = Authorization.split("\\s+");
			if (!Util.isNotNull(basicArray) || basicArray.length != 2){
				request.pushErr("HTTP Authorization 字段解析错误，非法请求");
			}else{
//				String basic  = basicArray[0];
				String base64 = basicArray[1], idpass = Tools.base64Decode(base64);
				
				if (Util.isEmptyString(idpass)){
					request.pushErr("HTTP Authorization 字段 idpass 解析错误，非法请求");
				}else{
					String[] idpassArray = idpass.split(":");
					
					if (!Util.isNotNull(idpassArray) || idpassArray.length != 2){
						request.pushErr("HTTP Authorization 字段 idpassArray 解析错误，非法请求");
					}else{
						username_password = idpassArray;
					}
					
				}
			}
		}
		
		return username_password;
	}
	// 检查HTTP 的 Basic认证. since http1.0
	public static boolean checkBasicAuth(HttpServletRequest request, String id, String pwd) {
		boolean authOK = false;

		// 认证后每次HTTP请求都会附带上 Authorization 头信息
		String Authorization = request.getHeader("Authorization");
		
		if(Util.isEmptyString(Authorization))return authOK;// 需要认证

		String[] basicArray = Authorization.split("\\s+");
		
		if (null == basicArray || 2 != basicArray.length)return authOK;

//		String basic  = basicArray[0];
		String base64 = basicArray[1], idpass = Tools.base64Decode(base64);
		
		if (Util.isEmptyString(idpass))return authOK;// 需要认证

		String[] idpassArray = idpass.split(":");
		if (null == idpassArray || 2 != idpassArray.length)return authOK;

		String _id = idpassArray[0], _pass = idpassArray[1];
		if (id.trim().equalsIgnoreCase(_id) && pwd.trim().equalsIgnoreCase(_pass))
			authOK = true;// 认证成功

		return authOK;
	}

	@SuppressWarnings("deprecation")
	public static void requireAuth(HttpServletResponse response, String msg) {
		response.setStatus(401, "Authentication Required");		// 发送状态码 401, 不能使用 sendError，坑
		response.addHeader("WWW-Authenticate", "Basic realm=" + msg);// 发送要求输入认证信息,则浏览器会弹出输入框
	}

	/**
	 *  检查HTTP 的 Digest认证. since http1.0
	 *  http://blog.csdn.net/renfufei/article/details/46552897
	 * @param request
	 * @param _username
	 * @param _password
	 * @return
	 */
	public static boolean checkDigestAuth(HttpServletRequest request, String _username, String _password) {
		boolean authOK = false;
		// 认证后每次HTTP请求都会附带上 Authorization 头信息
		String Authorization = request.getHeader("Authorization");
		if (null == Authorization || Authorization.trim().isEmpty()) {
			// 需要认证
			return authOK;
		}
		// 示例数据, 没有换行,有逗号,也有空格
		// Digest username="admin", realm="DIGEST tiemao",
		// nonce="227c89449fd644a3b9df12e7cb8b0e33", uri="/digest.jsp",
		// algorithm=MD5, response="a8bc07c1d6dc38802ce538247e22f773",
		// qop=auth, nc=00000001, cnonce="f337ac5d88670ef5"

		String[] digestArray = Authorization.split("\\s+");
		if (null == digestArray || digestArray.length < 2) {
			return authOK;
		}
		//
		Map<String, String> authMap = new HashMap<String, String>();
		for (int i = 0; i < digestArray.length; i++) {
			String paraAndValue = digestArray[i];
			//
			String[] pvArray = paraAndValue.split("=");
			if (null == pvArray || 2 != pvArray.length) {
				continue; // 不处理0
			}
			String key = pvArray[0];
			String value = pvArray[1];
			
			if (null == key || null == value) {

			}
			
			value = value.replace("'", "");
			value = value.replace(",", "");
			value = value.replace("\"", "");
			value = value.trim();
			authMap.put(key, value);
		}
		
		String username = authMap.get("username");
//		String nonce = authMap.get("nonce");
		String response = authMap.get("response");
		// 这里应该有个 RFC2617 算法,与客户端一致,即计算用户密码
		if (_username.equalsIgnoreCase(username) && checkAuth_RFC2617(_username, _password, response)) {
			authOK = true;// 认证成功,
		}

		return authOK;
	}

	/**
	 * RFC2617 运算,这算法比较复杂,暂时未实现
	 * 参考地址: http://www.faqs.org/rfcs/rfc2617.html https://gist.github.com/usamadar/2912088
	 * @param _username
	 * @param _password
	 * @param response
	 * @return
	 */
	public static boolean checkAuth_RFC2617(String _username, String _password, String response) {
		boolean authOK = false;
		
		if (null != _username || null != _password || response.equalsIgnoreCase(response))
			authOK = true;// 认证成功,
		
		return authOK;
	}

	// 不依赖 this 状态的方法,其实都应该设置为 static
	@SuppressWarnings("deprecation")
	public static void requireDigestAuth(HttpServletResponse response, String nonce) {
		// 发送状态码 401, 不能使用 sendError，坑
		response.setStatus(401, "Authentication Required");
		String authHeader = "Digest realm=\"DIGEST tiemao\"";
		authHeader += ",nonce=\"" + nonce + "\"";
		authHeader += ",algorithm=MD5";
		authHeader += ",qop=\"" + "auth" + "\"";
	
		response.addHeader("WWW-Authenticate", authHeader);// 发送要求输入认证信息,则浏览器会弹出输入框
	}
}
