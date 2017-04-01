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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.util.DateTools;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.map.MapHelper;

/**
 * 页面工具类，在页面调用
 * 
 * @author frank
 *
 */
public class PageUtil {
	/**
	 * 对某段 URL 参数剔除其中的一个。
	 * 
	 * @param withoutParam
	 *            不需要的那个参数
	 * @param queryString
	 *            通常由 request.getQueryString() 或 ${pageContext.request.queryString} 返回的 url 参数
	 * @return 特定的 url 参数
	 */
	public String getParams_without(String withoutParam, String queryString) {
		if (queryString == null) {
			return null;
		} else {
			queryString = queryString.replaceAll("&?" + withoutParam + "=[^&]*", "");// 删除其中一个参数

			if (StringUtil.isEmptyString(queryString))
				return null;
			return queryString.startsWith("&") ? queryString : "&" + queryString; // 补充关联的符号
		}
	}
	
	/**
	 * 对某段 URL 参数剔除其中的一个。但是返回 map。
	 * 
	 * @param withoutParam
	 *            不需要的那个参数
	 * @param queryString
	 *            通常由 request.getQueryString() 或  ${pageContext.request.queryString} 返回的 url 参数
	 * @return
	 */
	public Map<String, Object> getParams_without_asMap(String withoutParam, String queryString) {
		queryString = getParams_without(withoutParam, queryString);
		
		if(StringUtil.isEmptyString(queryString))
			return null;
		
//		if(queryString.startsWith("&"))
			queryString = queryString.replaceFirst("&", "");// 去掉第一个无用的 &
		return MapHelper.toMap(queryString.split("&"));
	}	

	/**
	 * 保持状态，对当前的参数记录，不受分页的影响
	 * @deprecated
	 * @param request
	 */
	public String appendParams(HttpServletRequest request, Map<String, String> params) {
		Map<String, String[]> map = request.getParameterMap();

		List<String> list = new ArrayList<>();

		for (String name : map.keySet()) {
			String[] values = map.get(name); // 即使是 key 都变成 value 了
			
			// 如果要重新指定参数
			if(params != null && params.size() > 0) {
				for(int i = 0; i < values.length; i++) {
					String v = values[i];
					if(params.containsKey(v)) {
						values[i] = params.get(v);
					}
				}
			}

			String aa;
			if (name.equals("start")) {
				continue;
			} else if (values.length == 1) {
				aa = StringUtil.stringJoin(values, name + "=");
			} else {
				aa = name + "=" + StringUtil.stringJoin(values, "&" + name + "=");
			}

			list.add(aa);
		}
		
		String s = StringUtil.stringJoin(list, "&");
		return s;

		// Service service = new Service();
		// service.setRequest(request);
		// String qs = MapHelper.join(service.getAll("start"), "&");
		//
		// if (!"".equals(qs))
		// return "&" + qs;
		// else
		// return "";
	}

	public int[] jumpPage(int totalPage) {
		return new int[totalPage];
	}
	
	public static String arrayJoin(String[] arr) {
		return StringUtil.stringJoin(arr, ",");
	}

	public String formatDate(Date date) {
		return DateTools.formatDateShorter(date);
	}
	public String formatShortDate(Date date) {
		return DateTools.formatDateShortest(date);
	}
	
	public static String jsonString_covernt(String str){
		return str.replace("\r\n", "\\n");
	}
	
	/**
	 * 输入一个相对地址，补充成为绝对地址 相对地址转换为绝对地址，并转换斜杠
	 * 
	 * @param relativePath
	 *            相对地址
	 * @return 绝对地址
	 */
	public static String Mappath(HttpServletRequest request, String relativePath) {
		String absolute = request.getServletContext().getRealPath(relativePath); // 绝对地址
		
		if (absolute != null)
			absolute = absolute.replace('\\', '/');
		return absolute;
	}
	
	public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(!"unKnown".equalsIgnoreCase(ip)){
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        
        ip = request.getHeader("X-Real-IP");
        if(!"unKnown".equalsIgnoreCase(ip))
            return ip;
        
        return request.getRemoteAddr();
    }
}
