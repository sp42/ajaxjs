package com.ajaxjs.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class StreamChain {
	private byte[] data;

	/**
	 * 输入流
	 */
	private InputStream in;

	/**
	 * 输出流
	 */
	private OutputStream out;

	/**
	 * 文本内容
	 */
	private String content;
	
	/**
	 * 输出流转换到字节数组
	 * @return
	 */
	public StreamChain output2byte() {
		return this;
	}

	/**
	 * 读输入流，将其转换为文本（多行） 字节流转换为字符串
	 * 
	 * @return StreamChain
	 */
	public StreamChain byteStream2stringStream() {
		StringBuilder result = new StringBuilder();

		// InputStreamReader 从一个数据源读取字节，并自动将其转换成 Unicode 字符
		// OutputStreamWriter 将字符的 Unicode 编码写到字节输出流
		try (InputStreamReader inReader = new InputStreamReader(in, StandardCharsets.UTF_8); // 字节流转换到字符流
				/*
				 * Decorator，装饰模式，又称为 Wrapper，使它具有了缓冲功能
				 * BufferedInputStream、BufferedOutputStream
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
			e.printStackTrace();
		}

		content = result.toString();

		return this;
	}

	public void close() {
		try {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the in
	 */
	public InputStream getIn() {
		return in;
	}

	/**
	 * @param in
	 *            the in to set
	 */
	public StreamChain setIn(InputStream in) {
		this.in = in;
		return this;
	}

	/**
	 * @return the out
	 */
	public OutputStream getOut() {
		return out;
	}

	/**
	 * @param out
	 *            the out to set
	 */
	public StreamChain setOut(OutputStream out) {
		this.out = out;
		return this;
	}

	/**
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(byte[] data) {
		this.data = data;
	}

	/**
	 * @return 文本内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            文本内容
	 */
	public StreamChain setContent(String content) {
		this.content = content;
		return this;
	}
}
