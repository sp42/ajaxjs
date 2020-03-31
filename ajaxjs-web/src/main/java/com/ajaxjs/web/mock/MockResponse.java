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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.ArgumentCaptor;

import com.ajaxjs.web.ByteArrayServletOutputStream;

/**
 * 模拟一个 Response 对象
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class MockResponse {
	/**
	 * 除了字符串使用 StringWriter，Response 输出的还可以是流
	 * 
	 * @param response 响应对象
	 * @return 流对象以便获取信息
	 */
	public static ServletOutputStream streamFactory(HttpServletResponse response) {
		ServletOutputStream out = new ByteArrayServletOutputStream();

		/*
		 * ServletOutputStream out = new ServletOutputStream() { private OutputStream os
		 * = new ByteArrayOutputStream();
		 * 
		 * @Override public void write(byte[] data, int offset, int length) { try {
		 * os.write(data, offset, length); } catch (IOException e) { Auto-generated
		 * catch block e.printStackTrace(); } }
		 * 
		 * @Override public void write(int i) throws IOException { os.write(i); }
		 * 
		 * @Override public String toString() { return os.toString(); }
		 * 
		 * public boolean isReady() { return false; }
		 * 
		 * public void setWriteListener(WriteListener arg0) { } };
		 */
		try {
			when(response.getOutputStream()).thenReturn(out);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return out;
	}

	/**
	 * Writer:形象的比喻：当我们调用 response.getWriter()
	 * 这个对象同时获得了网页的画笔，这时你就可以通过这个画笔在网页上画任何你想要显示的东西。Writer 就是向页面输出信息，负责让客户端显示内容
	 * 
	 * @param response 响应对象
	 * @return writer 以便获取输出信息
	 */
	public static StringWriter writerFactory(HttpServletResponse response) {
		StringWriter writer = new StringWriter();

		try {
			when(response.getWriter()).thenReturn(new PrintWriter(writer));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return writer;
	}

	/**
	 * 获取 MVC 跳转模版的那个路径
	 * 
	 * @param request 请求对象
	 * @return 模版路径
	 */
	public static String getRequestDispatcheResult(HttpServletRequest request) {
		ArgumentCaptor<String> dispatcherArgument = ArgumentCaptor.forClass(String.class);
		verify(request).getRequestDispatcher(dispatcherArgument.capture());

		return dispatcherArgument.getValue();
	}
}
