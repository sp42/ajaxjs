package com.ajaxjs.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
}
