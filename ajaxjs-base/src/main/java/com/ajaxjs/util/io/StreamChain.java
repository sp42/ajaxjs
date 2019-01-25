/**
 b * Copyright 2015 Sp42 frank@ajaxjs.com
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
package com.ajaxjs.util.io;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.ajaxjs.util.logger.LogHelper;

/**
 * 抽象的流链式处理器
 * 
 * @author Sp42 frank@ajaxjs.com
 * @param <T> 该泛型是子类，以便实现链式调用
 */
public abstract class StreamChain<T> {
	private static final LogHelper LOGGER = LogHelper.getLog(StreamChain.class);

	/**
	 * 输入流
	 */
	private InputStream in;

	/**
	 * 输出流
	 */
	private OutputStream out;

	/**
	 * 字节数组
	 */
	private byte[] data;

	/**
	 * 文本内容
	 */
	private String content;

	/**
	 * 输出流转换到字节数组
	 * 
	 * @return 返回本实例供链式调用
	 */
	@SuppressWarnings("unchecked")
	public T output2byte() {
		return (T) this;
	}

	/**
	 * 输出流写入字节数据
	 * 
	 * @return 返回本实例供链式调用
	 */
	@SuppressWarnings("unchecked")
	public T outputWriteData() {
		try {
			out.write(getData()); // 输出流写入字节数据
			out.flush();
		} catch (IOException e) {
			LOGGER.warning(e);
			return null;
		}

		return (T) this;
	}

	/**
	 * 读输入的字节流转换到字符流，将其转换为文本（多行）的字节流转换为字符串
	 * 
	 * @return 返回本实例供链式调用
	 */
	@SuppressWarnings("unchecked")
	public T byteStream2stringStream() {
		StringBuilder result = new StringBuilder();

		// InputStreamReader 从一个数据源读取字节，并自动将其转换成 Unicode 字符
		// 相对地，OutputStreamWriter 将字符的 Unicode 编码写到字节输出流
		try (InputStreamReader inReader = new InputStreamReader(in, StandardCharsets.UTF_8);
				/*
				 * Decorator，装饰模式，又称为 Wrapper，使它具有了缓冲功能 BufferedInputStream、BufferedOutputStream
				 * 只是在这之前动态的为它们加上一些功能（像是缓冲区功能）
				 */
				BufferedReader reader = new BufferedReader(inReader);) {

			String line = null;
			while ((line = reader.readLine()) != null) { // 一次读入一行，直到读入null为文件结束
				// 指定编码集的另外一种方法 line = new String(line.getBytes(), encodingSet);
				result.append(line);
				result.append('\n');
			}

		} catch (IOException e) {
			LOGGER.warning(e);
		}

		content = result.toString();

		return (T) this;
	}

	/**
	 * 两端速度不匹配，需要协调 理想环境下，速度一样快，那就没必要搞流，直接一坨给弄过去就可以了 流的意思很形象，就是一点一滴的，不是一坨坨大批量的
	 * 带缓冲的一入一出 出是字节流，所以要缓冲（字符流自带缓冲，所以不需要额外缓冲） 请注意，改方法不会关闭流 close，你需要手动关闭
	 * 
	 * @param in       输入流
	 * @param out      输出流
	 * @param isBuffer 是否加入缓冲功能
	 * @return 是否成功
	 */
	static final int bufferSize = 1024; // 1K 的数据块

	@SuppressWarnings("unchecked")
	public T write(boolean isBuffer) {
		InputStream in = getIn();
		OutputStream out = getOut();
		int readSize; // 读取到的数据长度
		byte[] buffer = new byte[bufferSize]; // 通过 byte 作为数据中转，用于存放循环读取的临时数据

		try {
			if (isBuffer) {
				try (OutputStream _out = new BufferedOutputStream(out);) {// 加入缓冲功能
					while ((readSize = in.read(buffer)) != -1) {
						_out.write(buffer, 0, readSize);
					}
				}
			} else {
				// readSize = in.read(buffer, 0, bufferSize);

				while ((readSize = in.read(buffer, 0, bufferSize)) != -1) {
					out.write(buffer, 0, readSize);
					// readSize = in.read(buffer, 0, bufferSize);
				}
				out.flush();
			}
		} catch (IOException e) {
			LOGGER.warning(e);
		}

		return (T) this;
	}

	/**
	 * InputStream 转换到 byte[]. 从输入流中获取数据， 转换到 byte[] 也就是 in
	 * 转到内存。虽然大家可能都在内存里面了但还不能直接使用，要转换
	 * 
	 * @return 返回本实例供链式调用
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public T inputStream2Byte() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();// 使用内存操作流，读取二进制
		setOut(out);
		write(true);

		setData(out.toByteArray());
		return (T) this;
	}

	/**
	 * 送入 byte[] 转换为输出流。可指定 byte[] 某一部分数据
	 * 
	 * @param off    偏移
	 * @param length 长度
	 * @return 返回本实例供链式调用
	 */
	@SuppressWarnings("unchecked")
	public T stringStream2output(int off, int length) {
		try (OutputStream out = new BufferedOutputStream(getOut(), bufferSize);) {
			if (off == 0 && length == 0)
				out.write(getData());
			else
				out.write(getData(), off, length);
			out.flush();
		} catch (IOException e) {
			LOGGER.warning(e);
		}

		return (T) this;
	}

	/**
	 * 送入 byte[] 转换为输出流。送入的 byte[] 是全部
	 * 
	 * @return 返回本实例供链式调用
	 */
	public T stringStream2output() {
		return stringStream2output(0, 0);
	}

	/**
	 * 关闭流，包含 in 和 out。
	 * 
	 * @return 返回本实例供链式调用
	 */
	@SuppressWarnings("unchecked")
	public T close() {
		try {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		} catch (IOException e) {
			LOGGER.warning(e);
		}

		return (T) this;
	}

	/**
	 * 获取输入流
	 * 
	 * @return 输入流
	 */
	public InputStream getIn() {
		return in;
	}

	/**
	 * 设置输入流
	 * 
	 * @param in 输入流
	 * @return 返回本实例供链式调用
	 */
	@SuppressWarnings("unchecked")
	public T setIn(InputStream in) {
		this.in = in;
		return (T) this;
	}

	/**
	 * 获取输出流
	 * 
	 * @return 输出流
	 */
	public OutputStream getOut() {
		return out;
	}

	/**
	 * 保存输出流
	 * 
	 * @param out 输出流
	 * @return 返回本实例供链式调用
	 */
	@SuppressWarnings("unchecked")
	public T setOut(OutputStream out) {
		this.out = out;
		return (T) this;
	}

	/**
	 * 获取文件字节数据
	 * 
	 * @return 文件字节数据
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * 设置文件字节数据
	 * 
	 * @param data 文件字节数据
	 * @return 返回本实例供链式调用
	 */
	@SuppressWarnings("unchecked")
	public T setData(byte[] data) {
		this.data = data;
		return (T) this;
	}

	/**
	 * 获取文本内容
	 * 
	 * @return 文本内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置文本内容
	 * 
	 * @param content 文本内容
	 * @return 返回本实例供链式调用
	 */
	@SuppressWarnings("unchecked")
	public T setContent(String content) {
		this.content = content;
		return (T) this;
	}
}
