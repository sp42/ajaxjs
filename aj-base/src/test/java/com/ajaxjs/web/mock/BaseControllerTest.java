/**
 * Copyright sp42 frank@ajaxjs.com
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
package com.ajaxjs.web.mock;

import static org.mockito.Mockito.mock;

import java.io.StringWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.web.mvc.MvcDispatcher;

/**
 * 方便测试的基础类
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public abstract class BaseControllerTest {
	public static FilterChain chain;

	// 单测技巧，每个 url 对应一个 request、一个 response
	public HttpServletRequest request;

	public HttpServletResponse response;

	public StringWriter writer;

	public ServletOutputStream os;

	/**
	 * 控制器的包名
	 * 
	 * @param packageName 包名
	 * @throws ServletException
	 */
	public void init(String packageName) {
		chain = mock(FilterChain.class);
		ComponentMgr.scan(packageName);
		MvcDispatcher.init(null);
	}
}
