package com.ajaxjs.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.ajaxjs.util.logger.LogHelper;

public class IoHelper {
	private static final LogHelper LOGGER = LogHelper.getLog(IoHelper.class);

	/**
	 * 读输入的字节流转换到字符流，将其转换为文本（多行）的字节流转换为字符串
	 * 
	 * @return 返回本实例供链式调用
	 */
	public static String byteStream2stringStream(InputStream in) {
		StringBuilder result = new StringBuilder();

		/*
		 * InputStreamReader 从一个数据源读取字节，并自动将其转换成 Unicode 字符 相对地，OutputStreamWriter 将字符的
		 * Unicode 编码写到字节输出流。 Decorator，装饰模式，又称为 Wrapper，使它具有了缓冲功能
		 * BufferedInputStream、BufferedOutputStream 只是在这之前动态的为它们加上一些功能（像是缓冲区功能）
		 */
		try (InputStreamReader inReader = new InputStreamReader(in, StandardCharsets.UTF_8); BufferedReader reader = new BufferedReader(inReader);) {

			String line = null;

			while ((line = reader.readLine()) != null) { // 一次读入一行，直到读入null为文件结束
				// 指定编码集的另外一种方法 line = new String(line.getBytes(), encodingSet);
				result.append(line);
				result.append('\n');
			}

		} catch (IOException e) {
			LOGGER.warning(e);
		}

		return result.toString();
	}

	static final int bufferSize = 1024; // 1K 的数据块

	/**
	 * 两端速度不匹配，需要协调 理想环境下，速度一样快，那就没必要搞流，直接一坨给弄过去就可以了 流的意思很形象，就是一点一滴的，不是一坨坨大批量的
	 * 带缓冲的一入一出 出是字节流，所以要缓冲（字符流自带缓冲，所以不需要额外缓冲） 请注意，改方法不会关闭流 close，你需要手动关闭
	 * 
	 * @param in 输入流
	 * @param out 输出流
	 * @param isBuffer 是否加入缓冲功能
	 * @return 是否成功
	 */
	public void write(InputStream in, OutputStream out, boolean isBuffer) {
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
	}
}
