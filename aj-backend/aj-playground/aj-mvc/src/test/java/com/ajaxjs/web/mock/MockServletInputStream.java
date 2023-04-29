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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

/**
 * 模拟一个 Servlet 输入流对象
 * 出处:https://stackoverflow.com/questions/4466770/how-to-write-unit-tests-with-commons-fileupload
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class MockServletInputStream extends ServletInputStream {
	/**
	 * 输入流
	 */
	private InputStream delegate;

	/**
	 * 创建一个 Servlet 输入流对象
	 * 
	 * @param b 数据
	 */
	public MockServletInputStream(byte[] b) {
		delegate = new ByteArrayInputStream(b);
	}

	@Override
	public int read() throws IOException {
		return delegate.read();
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public boolean isReady() {
		return false;
	}

	@Override
	public void setReadListener(ReadListener readListener) {
	}
}
