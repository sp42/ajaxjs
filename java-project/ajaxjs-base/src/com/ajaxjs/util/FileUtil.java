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
import java.nio.charset.StandardCharsets;

import javax.activation.MimetypesFileTypeMap;

/**
 * 打开、保存字符流的文件；或者读取字符流，将其转为 String；删除文件；获取文件扩展名；获取文件的 MIMI 类型
 * @author frank
 *
 */
public class FileUtil {
 
	 


	/**
	 * 写文件不能用 FileWriter，原因是会中文乱码
	 * 
	 * @param filepath
	 *            文件路径
	 * @param content
	 *            文件内容，文本
	 * @throws IOException
	 */
	@Deprecated
	public static void save2file(String filepath, String content) throws IOException {
		try (OutputStream out = new FileOutputStream(filepath);
				// OutputStreramWriter将输出的字符流转化为字节流输出（字符流已带缓冲）
				OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);) {
			writer.write(content);
		} catch (IOException e) {
			System.err.println("写入文件" + filepath + "失败");
			throw e;
		}
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
	 *            文件名
	 * @return 擴展名
	 */
	public static String getFileSuffix(String filename) {
		return filename.substring(filename.lastIndexOf(".") + 1);
	}

	/**
	 * 获取文件名的 MIME 类型
	 * 
	 * @param filename
	 *            文件名
	 * @return MIME 类型
	 */
	public static String getMime(String filename) {
		return new MimetypesFileTypeMap().getContentType(filename);
	}

	/**
	 * 获取文件名的 MIME 类型
	 * 
	 * @param file
	 *            文件对象
	 * @return MIME 类型
	 */
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
	 * @return 如 /2008/10/15/ 格式的字符串
	 */
	public static String getDirnameByDate() {
		String datatime = DateTools.now("yyyy-MM-dd");
		String year = datatime.substring(0, 4), mouth = datatime.substring(5, 7), day = datatime.substring(8, 10);

		return File.separator + year + File.separator + mouth + File.separator + day + File.separator;
	} 
}
