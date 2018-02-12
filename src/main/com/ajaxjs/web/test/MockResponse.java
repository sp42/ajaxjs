/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
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
package com.ajaxjs.web.test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.ArgumentCaptor;

/**
 * 包装一个 Response 输出流
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class MockResponse {
	/**
	 * 
	 * @author Sp42 frank@ajaxjs.com
	 *
	 */
	public static class StubServletOutputStream extends ServletOutputStream {
		private OutputStream os = new ByteArrayOutputStream();

		@Override
		public void write(int i) throws IOException {
			os.write(i);
		}

		public String getContent() {
			return os.toString();
		}
	}

	@Deprecated
	class StubServletOutputStream22 extends ServletInputStream {
		public ByteArrayOutputStream os = new ByteArrayOutputStream();

		public void write(int i) throws IOException {
			os.write(i);
		}

		public String getContent() {
			return os.toString();
		}

		@Override
		public int read() throws IOException {
			return 0;
		}
	}

	/**
	 * Writer:形象的比喻：当我们调用 response.getWriter()
	 * 这个对象同时获得了网页的画笔，这时你就可以通过这个画笔在网页上画任何你想要显示的东西。Writer 就是向页面输出信息，负责让客户端显示内容
	 * 
	 * @param response
	 *            响应对象
	 * @return writer 以便获取输出信息
	 */
	public static StringWriter writerFactory(HttpServletResponse response) {
		StringWriter writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		
		try {
			when(response.getWriter()).thenReturn(printWriter);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return writer;
	}

	/**
	 * 除了字符串使用 StringWriter，Response 输出的还可以是流
	 * 
	 * @param response
	 *            响应对象
	 * @return 流对象以便获取信息
	 */
	public static StubServletOutputStream streamFactory(HttpServletResponse response) {
		StubServletOutputStream os = new StubServletOutputStream();

		try {
			when(response.getOutputStream()).thenReturn(os);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return os;
	}

	/**
	 * 获取 MVC 跳转模版的那个路径
	 * 
	 * @param request
	 *            请求对象
	 * @return 模版路径
	 */
	public static String getRequestDispatcheResult(HttpServletRequest request) {
		ArgumentCaptor<String> dispatcherArgument = ArgumentCaptor.forClass(String.class);
		verify(request).getRequestDispatcher(dispatcherArgument.capture());

		return dispatcherArgument.getValue();
	}
}