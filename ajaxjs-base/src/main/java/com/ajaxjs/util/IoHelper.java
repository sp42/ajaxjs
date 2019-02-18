package com.ajaxjs.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.activation.MimetypesFileTypeMap;

import com.ajaxjs.util.logger.LogHelper;

public class IoHelper {
	private static final LogHelper LOGGER = LogHelper.getLog(IoHelper.class);

	/**
	 * 新建一个空文件
	 * 
	 * @param saveDirPath 如果路径不存在则自动创建
	 * @param fileName 保存的文件名
	 * @return 新建文件的 File 对象
	 */
	public static File createFile(String saveDirPath, String fileName) {
		File saveDir = new File(saveDirPath);
		if (!saveDir.exists())
			saveDir.mkdir();

		return new File(saveDir + File.separator + fileName);
	}

	/**
	 * 保存文本文件，注意该方法会覆盖现有相同文件名的文件
	 * 
	 * @param saveDirPath 如果路径不存在则自动创建
	 * @param fileName 保存的文件名
	 * @param text 文本内容
	 * @return 新建文件的 Path 对象
	 */
	public static Path createFile(String saveDirPath, String fileName, String text) {
		try {
			return Files.write(createFile(saveDirPath, fileName).toPath(), text.getBytes());
		} catch (IOException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 读取文件文本内容。 此方法不适合读取很大的文件，因为可能存在内存空间不足的问题。开发者还应该明确规定文件的字符编码，以避免任异常或解析错误。 默认是
	 * UTF-8 编码。如果读入的文件的编码是 ANSI 编码，那么会报 java.nio.charset.MalformedInputException:
	 * Input length = 1 错误
	 * 
	 * @param filePath
	 * @return
	 */
	public static String readFile(String filePath) {
		StringBuilder sb = new StringBuilder();

		try {
			Files.lines(Paths.get(filePath), StandardCharsets.UTF_8).forEach(str -> sb.append(str));
			return sb.toString();
		} catch (IOException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	public static String readFile2(String filePath) {
		try {
			return Encode.byte2String(Files.readAllBytes(Paths.get(filePath)));
		} catch (IOException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 获取 URL 上的文件名，排除 ? 参数部分
	 * 
	 * @param url
	 * @return
	 */
	public static String getFileNameFromUrl(String url) {
		String[] arr = url.split("/");
		String last = arr[arr.length - 1];
		return last.split("\\?")[0];
	}

	/**
	 * 获取文件名的 MIME 类型
	 * 
	 * @param file 文件对象
	 * @return MIME 类型
	 */
	public static String getMime(File file) {
		String contentType = new MimetypesFileTypeMap().getContentType(file);
		if (file.getName().endsWith(".png"))
			contentType = "image/png"; // TODO needs?
		if (contentType == null)
			contentType = "application/octet-stream";
		return contentType;
	}

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
	public static void write(InputStream in, OutputStream out, boolean isBuffer) {
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
