package com.ajaxjs.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.activation.MimetypesFileTypeMap;

import com.ajaxjs.util.logger.LogHelper;

public class FileHelper {
	private static final LogHelper LOGGER = LogHelper.getLog(FileHelper.class);

	/**
	 * 新建一个空文件
	 * 
	 * @param saveDirPath 	如果路径不存在则自动创建
	 * @param fileName 		保存的文件名
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
	 * @param saveDirPath	如果路径不存在则自动创建
	 * @param fileName 		保存的文件名
	 * @param text			文本内容
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

}
