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
package com.ajaxjs.util.io;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.ajaxjs.util.logger.LogHelper;

/**
 * 流操作助手类
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class StreamHelper {
	private static final LogHelper LOGGER = LogHelper.getLog(StreamHelper.class);

	/**
	 * 读输入的字节流转换到字符流，将其转换为文本（多行）的字节流转换为字符串
	 * 
	 * @param in 输入流，无须手动关闭
	 * @return 字符串
	 */
	public static String byteStream2string(InputStream in) {
		return byteStream2string_Charset(in, StandardCharsets.UTF_8);
	}

	/**
	 * 读输入的字节流转换到字符流，将其转换为文本（多行）的字节流转换为字符串。可指定字符编码
	 * 
	 * @param in     输入流，无须手动关闭
	 * @param encode 字符编码
	 * @return 字符串
	 */
	public static String byteStream2string_Charset(InputStream in, Charset encode) {
		StringBuilder result = new StringBuilder();

		// InputStreamReader 从一个数据源读取字节，并自动将其转换成 Unicode 字符
		// 相对地，OutputStreamWriter 将字符的 Unicode 编码写到字节输出流
		try (InputStreamReader inReader = new InputStreamReader(in, encode);
				/*
				 * Decorator，装饰模式，又称为 Wrapper，使它具有了缓冲功能 BufferedInputStream、BufferedOutputStream
				 * 只是在这之前动态的为它们加上一些功能（像是缓冲区功能）
				 */
				BufferedReader reader = new BufferedReader(inReader);) {

			String line = null;
			while ((line = reader.readLine()) != null) { // 一次读入一行，直到读入 null 为文件结束
				// 指定编码集的另外一种方法 line = new String(line.getBytes(), encodingSet);
				result.append(line);
				result.append('\n');
			}

			inReader.close();
		} catch (IOException e) {
			LOGGER.warning(e);
			return null;
		}

		return result.toString();
	}

	/**
	 * 1K 的数据块
	 */
	public static final int BUFFER_SIZE = 1024;

	/**
	 * 两端速度不匹配，需要协调 理想环境下，速度一样快，那就没必要搞流，直接一坨给弄过去就可以了 流的意思很形象，就是一点一滴的，不是一坨坨大批量的
	 * 带缓冲的一入一出 出是字节流，所以要缓冲（字符流自带缓冲，所以不需要额外缓冲） 请注意，改方法不会关闭流 close，你需要手动关闭
	 * 
	 * @param in       输入流，无须手动关闭
	 * @param out      输出流
	 * @param isBuffer 是否加入缓冲功能
	 */
	public static void write(InputStream in, OutputStream out, boolean isBuffer) {
		int readSize; // 读取到的数据长度
		byte[] buffer = new byte[BUFFER_SIZE]; // 通过 byte 作为数据中转，用于存放循环读取的临时数据

		try {
			if (isBuffer) {
				try (OutputStream _out = new BufferedOutputStream(out);) {// 加入缓冲功能
					while ((readSize = in.read(buffer)) != -1) {
						_out.write(buffer, 0, readSize);
					}
				}
			} else {
				// 每次读 1KB 数据，将输入流数据写入到输出流中
				// readSize = in.read(buffer, 0, bufferSize);
				while ((readSize = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
					out.write(buffer, 0, readSize);
					// readSize = in.read(buffer, 0, bufferSize);
				}

				out.flush();
			}
		} catch (IOException e) {
			LOGGER.warning(e);
		}
	}

	/**
	 * 使用内存操作流，读取二进制，也就是将流转换为内存的数据。 InputStream 转换到 byte[]. 从输入流中获取数据， 转换到 byte[]
	 * 也就是 in 转到内存。虽然大家可能都在内存里面了但还不能直接使用，要转换
	 * 
	 * @param in 输入流
	 * @return 返回本实例供链式调用
	 */
	public static byte[] inputStream2Byte(InputStream in) {
		// 使用内存操作流，读取二进制
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			write(in, out, true);

			return out.toByteArray();
		} catch (IOException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 送入的 byte[] 转换为输出流。可指定 byte[] 某一部分数据。 注意这函数不会关闭输出流，请记得在适当的时候将其关闭。
	 * 
	 * @param out    输出流
	 * @param data   输入的数据
	 * @param off    偏移
	 * @param length 长度
	 */
	public static void bytes2output(OutputStream out, byte[] data, int off, int length) {
		bytes2output(out, data, true, off, length);
	}

	/**
	 * 送入的 byte[] 转换为输出流。可指定 byte[] 某一部分数据。 注意这函数不会关闭输出流，请记得在适当的时候将其关闭。
	 * 
	 * @param out        输出流
	 * @param data       输入的数据
	 * @param isBuffered 是否需要缓冲
	 * @param off        偏移
	 * @param length     长度
	 */
	public static void bytes2output(OutputStream out, byte[] data, boolean isBuffered, int off, int length) {
		try {
			if (isBuffered)
				out = new BufferedOutputStream(out, BUFFER_SIZE);

			if (off == 0 && length == 0)
				out.write(data);
			else
				out.write(data, off, length);

			out.flush();
		} catch (IOException e) {
			LOGGER.warning(e);
		}

	}

	/**
	 * 在字节数组中截取指定长度数组
	 * 
	 * @param data   输入的数据
	 * @param off    偏移
	 * @param length 长度
	 * @return 指定 范围的字节数组
	 */
	public static byte[] subBytes(byte[] data, int off, int length) {
		byte[] bs = new byte[length];
		System.arraycopy(data, off, bs, 0, length);
		
		return bs;
	}

	/**
	 * 在字节数组里查找某个字节数组，找到返回&lt;=0，未找到返回-1
	 * 
	 * @param data   被搜索的内容
	 * @param search 要搜索内容
	 * @param start  搜索起始位置
	 * @return 目标位置，找不到返回-1
	 */
	public static int byteIndexOf(byte[] data, byte[] search, int start) {
		int len = search.length;

		for (int i = start; i < data.length; i++) {
			int temp = i, j = 0;

			while (data[temp] == search[j]) {
				temp++;
				j++;

				if (j == len)
					return i;
			}
		}

		return -1;
	}

	/**
	 * 在字节数组里查找某个字节数组，找到返回&lt;=0，未找到返回-1
	 * 
	 * @param data   被搜索的内容
	 * @param search 要搜索内容
	 * @return 目标位置，找不到返回-1
	 */
	public static int byteIndexOf(byte[] data, byte[] search) {
		return byteIndexOf(data, search, 0);
	}

	/**
	 * 合并两个字节数组
	 * 
	 * @param a 数组a
	 * @param b 数组b
	 * @return 新合并的数组
	 */
	public static byte[] concat(byte[] a, byte[] b) {
		byte[] c = new byte[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);

		return c;
	}
}
