package com.ajaxjs.spring;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import org.springframework.web.multipart.MultipartFile;

import com.ajaxjs.sql.util.SnowflakeId;
import com.ajaxjs.util.WebHelper;
import com.ajaxjs.util.io.FileHelper;

/**
 * 文件上传的辅助类
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public class FileUploadHelper {
	public static final String CONTENT_TYPE = "multipart/form-data";

	public static final String CONTENT_TYPE2 = "multipart/form-data;charset=UTF-8";

	public static String uploadInWeb(MultipartFile file, String uploadDir, boolean isNewAutoName) {
		String _uploadDir = WebHelper.mappath(DiContextUtil.getRequest(), uploadDir);
		return upload(file, _uploadDir, isNewAutoName);
	}

	/**
	 * 保存上传的文件
	 * 
	 * @param file          文件
	 * @param uploadDir     保存目录
	 * @param isNewAutoName 是否重新命名？
	 * @return
	 */
	public static String upload(MultipartFile file, String uploadDir, boolean isNewAutoName) {
		Objects.requireNonNull(file);
		String filename = file.getOriginalFilename();

		if (filename == null)
			throw new IllegalArgumentException("表单上传的参数 name 与方法中 MultipartFile 的参数名是否一致?");

		if (isNewAutoName)
			filename = getAutoName(filename);

		FileHelper.mkDir(uploadDir);
		File file2 = new File(uploadDir + filename);

		try {
			file.transferTo(file2);
			file2.setReadable(true, false);
			file2.setExecutable(true, false);
			file2.setWritable(true, false);
		} catch (IllegalStateException | IOException e) {
			System.err.println("文件上传失败");
			e.printStackTrace();
		}

		return filename;
	}

	/**
	 * 生成自动名称，保留扩展名
	 * 
	 * @param originalFilename
	 * @return
	 */
	public static String getAutoName(String originalFilename) {
		String[] arr = originalFilename.split("\\.");
		String ext = "";

		if (arr.length >= 2)
			ext = "." + arr[arr.length - 1];
		else {
			// 没有扩展名
		}

		return SnowflakeId.get() + ext;
	}
}
