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
import java.util.function.Consumer;

import com.ajaxjs.util.logger.LogHelper;

/**
 * 流助手类
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class StreamHelper {
	private static final LogHelper LOGGER = LogHelper.getLog(StreamHelper.class);

	public static void read(InputStream in, Charset cSet, Consumer<String> fn) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, cSet));
		String line = null;

		try {
			while ((line = reader.readLine()) != null) { // 一次读入一行，直到读入null为文件结束
				fn.accept(line);
			}
		} catch (IOException e) {
			LOGGER.warning(e);
		}
	}

	/**
	 * 
	 * @param in
	 * @param cSet
	 * @return
	 */
	public static String asString(InputStream in, Charset cSet) {
		StringBuilder sb = new StringBuilder();

		read(in, StandardCharsets.UTF_8, str -> {
			sb.append(str);
			sb.append('\n');
		});

		return sb.toString();
	}

	public static final int BUFFER_SIZE = 1024; // 1K 的数据块

	// TODO 重复了？
	public static void write(InputStream in, OutputStream out, boolean isBuffer) {
		int readSize; // 读取到的数据长度
		byte[] buffer = new byte[BUFFER_SIZE]; // 通过 byte 作为数据中转，用于存放循环读取的临时数据

		if (isBuffer)
			out = new BufferedOutputStream(out);

		try {
			while ((readSize = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
				out.write(buffer, 0, readSize);

			}
		} catch (IOException e) {
			LOGGER.warning(e);
		}

	}

	/**
	 * 使用内存操作流，读取二进制，也就是将流转换为内存的数据
	 * 
	 * @param in 输入流
	 * @return 内存的数据
	 */
	public static byte[] asByte(InputStream in) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		write(in, out, true);
		return out.toByteArray();
	}

	/**
	 * 送入 byte[] 转换为输出流。可指定 byte[] 某一部分数据
	 * 
	 * @param out    输出流
	 * @param off    偏移
	 * @param length 长度
	 */
	public static void save(OutputStream out, byte[] data, int off, int length) {
		try {
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
	 * @param src
	 * @param begin
	 * @param count
	 * @return
	 */
	public static byte[] subBytes(byte[] src, int begin, int count) {
		byte[] bs = new byte[count];
		System.arraycopy(src, begin, bs, 0, count);
		return bs;
	}
}
