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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.web.security.ListControl;

/**
 * 禁止非法 POST 请求。 只允许 post 提交的 url 列表，需要配置 onlyPostUrlList 参数
 * 
 * @author Frank
 *
 */
public class Post extends ListControl implements SecurityFilter{

	public Post(List<String> whiteList, List<String> blackList) {
		this.whiteList = whiteList;
		this.blackList = blackList;
	}

	@Override
	public boolean check(HttpServletRequest request) {
		if (!"POST".equalsIgnoreCase(request.getMethod())) {
			return true;
		}
		String uri = request.getRequestURI();
		
//		if(!isInWhiteList(uri) && !isInBlackList(uri)){
//			return true;
//		}
		if(isInWhiteList(uri))
			return true;
		if(isInBlackList(uri))
			return false;	
		
		return true; // 没有任何信息则通过
	}

}
