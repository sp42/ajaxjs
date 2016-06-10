package com.ajaxjs.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.activation.MimetypesFileTypeMap;

import com.ajaxjs.Constant;

public class FileUtil {
	/**
	 * 读取文件，以文本格式返回。如果失败则返回 null。
	 * 
	 * @param filepath
	 *            文件路径
	 * @return 返回文本。如果失败则返回 null。
	 * @throws IOException
	 */
	public static String readFileAsText(String filepath) throws IOException {
		return readFileAsText(new File(filepath));
	}

	/**
	 * 读取文件，以文本格式返回。如果失败则返回 null。
	 * 
	 * @param file
	 *            文件对象
	 * @return 返回文本。如果失败则返回 null。
	 * @throws IOException
	 */
	public static String readFileAsText(File file) throws IOException {
		if (!file.exists())
			throw new FileNotFoundException(file.getPath() + file.getName() + " 不存在！");
		try (InputStream is = new FileInputStream(file)) {
			return readText(is);
		} catch (IOException e) {
			throw e;
		}
	}

	/**
	 * 读输入流，将其转换为文本（多行）
	 * 
	 * @param is
	 *            输入流
	 * @return 文本
	 * @throws IOException
	 */
	public static String readText(InputStream is) throws IOException {
		String line = null;
		StringBuilder result = new StringBuilder();

		// InputStreamReader从一个数据源读取字节，并自动将其转换成Unicode字符
		// OutputStreamWriter将字符的Unicode编码写到字节输出流
		try (InputStreamReader isReader = new InputStreamReader(is, "UTF-8"); // 字节流转换到字符流
				/*
				 * Decorator，装饰模式，又称为 Wrapper，使它具有了缓冲功能
				 * BufferedInputStream、BufferedOutputStream
				 * 只是在这之前动态的为它们加上一些功能（像是缓冲区功能）
				 */
				BufferedReader reader = new BufferedReader(isReader);) {
			while ((line = reader.readLine()) != null) { // 一次读入一行，直到读入null为文件结束
				// 指定编码集的另外一种方法 line = new String(line.getBytes(), encodingSet);
				result.append(line);
				result.append(Constant.newline);
			}
		} catch (IOException e) {
			System.err.println(e);
			throw e;
		}

		return result.toString();
	}

	/**
	 * 写文件不能用 FileWriter，原因是会中文乱码
	 * 
	 * @param filepath
	 *            文件路径
	 * @param content
	 * @throws IOException
	 */
	public static void save2file(String filepath, String content) throws IOException {
		try (OutputStream out = new FileOutputStream(filepath);
				// OutputStreramWriter将输出的字符流转化为字节流输出（字符流已带缓冲）
				OutputStreamWriter writer = new OutputStreamWriter(out, Constant.encoding_UTF8);) {
			writer.write(content);
		} catch (IOException e) {
			System.err.println("写入文件" + filepath + "失败");
			throw e;
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filename
	 */
	public static boolean delete(String filename) {
		return new File(filename).delete();
	}

	/**
	 * 输入 /foo/bar/foo.jpg 返回 foo.jpg
	 * 
	 * @param str
	 *            输入的字符串
	 * @return 文件名
	 */
	public static String getFileName(String str) {
		String[] arr = str.split("/");

		return arr[arr.length - 1];
	}

	/**
	 * 獲取文件名的擴展名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getFileSuffix(String filename) {
		return filename.substring(filename.lastIndexOf(".") + 1);
	}

	/**
	 * 获取文件名的 MIME 类型
	 * 
	 * @param filename
	 * @return
	 */
	public static String getMime(String filename) {
		return new MimetypesFileTypeMap().getContentType(filename);
	}

	public static String getMime(File file) {
		String contentType = new MimetypesFileTypeMap().getContentType(file);
		if (file.getName().endsWith(".png"))
			contentType = "image/png"; // TODO needs?
		if (StringUtil.isEmptyString(contentType))
			contentType = "application/octet-stream";
		return contentType;
	}

	/**
	 * 根据日期字符串得到目录名 格式: /2008/10/15/
	 * 
	 * @return
	 */
	public static String getDirnameByDate() {
		String datatime = DateTools.now("yyyy-MM-dd");
		String year = datatime.substring(0, 4), mouth = datatime.substring(5, 7), day = datatime.substring(8, 10);

		return Constant.file_pathSeparator + year + Constant.file_pathSeparator + mouth + Constant.file_pathSeparator
				+ day + Constant.file_pathSeparator;
	}

	/**
	 * 两端速度不匹配，需要协调 理想环境下，速度一样快，那就没必要搞流，直接一坨给弄过去就可以了 流的意思很形象，就是一点一滴的，不是一坨坨大批量的
	 * 带缓冲的一入一出 出是字节流，所以要缓冲（字符流自带缓冲，所以不需要额外缓冲） 请注意，改方法不会关闭流 close，你需要手动关闭
	 * 
	 * @param is
	 *            输入流
	 * @param os
	 *            输出流
	 * @param isBuffer
	 *            是否加入缓冲功能
	 * @return
	 */
	public static boolean write(InputStream is, OutputStream os, boolean isBuffer) throws IOException {
		boolean isOk = false;

		int bufferSize = 1024, // 1K 的数据块
				readSize; // 读取到的数据长度
		byte[] buffer = new byte[bufferSize]; // 通过 byte 作为数据中转，用于存放循环读取的临时数据

		if (isBuffer) {
			try (OutputStream _out = new BufferedOutputStream(os);) {// 加入缓冲功能
				while ((readSize = is.read(buffer)) != -1) {
					_out.write(buffer, 0, readSize);
				}
			}
			isOk = true;
		} else {
			readSize = is.read(buffer, 0, bufferSize);
			while (readSize != -1) {
				os.write(buffer, 0, readSize);
				readSize = is.read(buffer, 0, bufferSize);
			}
			os.flush();
			isOk = true;
		}
		return isOk;
	}

	/**
	 * 从输入流中获取数据， 转换到 byte[] 也就是 in 转到内存 虽然大家可能都在内存里面了但还不能直接使用，要转换
	 * 
	 * @param in
	 *            输入流
	 * @return
	 * @throws IOException
	 */
	public static byte[] stream2byte(InputStream is) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();// 使用内存操作流，读取二进制
		write(is, out, true);
		return out.toByteArray();// InputStream 转换到 byte[]
	}

	public static OutputStream byte2stream(OutputStream os, byte[] data, int off, int length) throws IOException {
		try (OutputStream bos = new BufferedOutputStream(os, 1024);) {
			if (off == 0 && length == 0)
				bos.write(data);
			else
				bos.write(data, off, length);
			bos.flush();
		}

		return os;
	}
}
