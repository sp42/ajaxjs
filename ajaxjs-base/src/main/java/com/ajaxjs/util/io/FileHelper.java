package com.ajaxjs.util.io;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.activation.MimetypesFileTypeMap;

import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.Encode;
import com.ajaxjs.util.logger.LogHelper;

public class FileHelper {
	private static final LogHelper LOGGER = LogHelper.getLog(FileHelper.class);

	/**
	 * 如果没有输出目录则先创建
	 * 
	 * @param fullPath 完整路径，最后一个元素为文件名
	 */
	public static void mkDir(String fullPath) {
		String arr[] = fullPath.split("\\/|\\\\");
		arr[arr.length - 1] = "";// 取消文件名，让最后一个元素为空字符串
		String folder = String.join(File.separator, arr);
	
		File f = new File(folder); // 先检查目录是否存在，若不存在建立
		if (!f.exists())
			f.mkdirs();
	}

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
	 * 获取文件名的 MIME 类型。检测手段不会真正打开文件进行检查而是单纯文件名的字符串判断。
	 * 
	 * @param filename 文件名
	 * @return MIME 类型
	 */
	public static String getMime(String filename) {
		Path path = Paths.get(filename); // works on java7
	
		try {
			return Files.probeContentType(path);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
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
	 * 根据日期字符串得到目录名 格式: /2008/10/15/
	 * 
	 * @return 如 /2008/10/15/ 格式的字符串
	 */
	public static String getDirNameByDate() {
		String datatime = CommonUtil.now("yyyy-MM-dd");
		String year = datatime.substring(0, 4), mouth = datatime.substring(5, 7), day = datatime.substring(8, 10);
	
		return File.separator + year + File.separator + mouth + File.separator + day + File.separator;
	}

	/**
	 * 输入 /foo/bar/foo.jpg 返回 foo.jpg
	 * 
	 * @param str 输入的字符串
	 * @return 文件名
	 */
	public static String getFileName(String str) {
		String[] arr = str.split("/");
	
		return arr[arr.length - 1];
	}

	/**
	 * 获取 URL 上的文件名，排除 ? 参数部分
	 * 
	 * @param url
	 * @return
	 */
	public static String getFileNameFromUrl(String url) {
		return getFileName(url).split("\\?")[0];
	}

	/**
	 * 獲取文件名的擴展名
	 * 
	 * @param filename 文件名
	 * @return 擴展名
	 */
	public static String getFileSuffix(String filename) {
		return filename.substring(filename.lastIndexOf(".") + 1);
	}

	/**
	 * 打开文件，返回其文本内容
	 * 
	 * @param path 文件磁盘路径
	 * @return 文件内容
	 */
	public static String openAsText(String path) {
		return new FileUtil().setFilePath(path).read().byteStream2stringStream().close().getContent();
	}

}
