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
package com.ajaxjs.web.test;

import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author frank
 *
 */
public class StubServletOutputStream extends ServletOutputStream {
	public OutputStream os = new ByteArrayOutputStream();

	/**
	 * 
	 */
	public void write(int i) throws IOException {
		os.write(i);
	}

	/**
	 * 
	 * @return
	 */
	public String getContent() {
		return os.toString();
	}

	public static StubServletOutputStream OutputStreamFactory(HttpServletResponse response) {
		StubServletOutputStream servletOutputStream = new StubServletOutputStream();
		try {
			when(response.getOutputStream()).thenReturn(servletOutputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return servletOutputStream;
	}
	public static StringWriter WriterFactory(HttpServletResponse response) {
		StringWriter writer = new StringWriter();
		
		try {
			when(response.getWriter()).thenReturn(new PrintWriter(writer));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return writer;
	}
}