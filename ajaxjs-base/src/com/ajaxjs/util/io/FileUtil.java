/**
 * Copyright 2015 Frank Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import javax.activation.MimetypesFileTypeMap;

import com.ajaxjs.util.DateTools;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.StringUtil;

public class FileUtil extends StreamChain<FileUtil> {
	private static final LogHelper LOGGER = LogHelper.getLog(FileUtil.class);
	
	private String filePath;

	private File file;

	private boolean overwrite;

	public FileUtil read() {
		try {
			if (!file.exists())
				throw new FileNotFoundException(file.getPath() + " 不存在！");

			setIn(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			LOGGER.warning(e);
		}
		return this;
	}
	
	/**
	 * 打开文件，返回其文本内容
	 * @param path
	 * @return
	 */
	public static String openAsText(String path) {
		return new FileUtil().setFilePath(path).read().byteStream2stringStream().close().getContent();
	}

	/**
	 * 保存文件
	 * 
	 * @param off
	 * @param len
	 * @return
	 */
	public FileUtil save(int off, int len) {
		OutputStream out = null;

		try {
			out = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			LOGGER.warning(e);
		}

		setOut(out);

		try {
			out.write(getData(), off, len);
		} catch (IOException e) {
			LOGGER.warning(e);
		}

		return this;
	}

	/**
	 * 写文件不能用 FileWriter，原因是会中文乱码
	 * @param content
	 *            文件内容，文本
	 * @return
	 */
	public FileUtil save() {
		OutputStream out = null;

		try {
			out = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			LOGGER.warning(e);
		}

		setOut(out);

		// OutputStreramWriter将输出的字符流转化为字节流输出（字符流已带缓冲）
		try (OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);) {
			writer.write(getContent());
		} catch (IOException e) {
			LOGGER.warning(e);
		}

		return this;
	}
	
	/**
	 * 返回某个文件夹里面的所有文件
	 * 
	 * @param folderName
	 *            文件夹名称
	 * @return
	 */
	public String[] getFiles() {
		return file.isDirectory() ? file.list() : null;
	}
	
	/**
	 * 删除文件
	 * 
	 * @param filename
	 *            文件名
	 */
	public FileUtil copyTo() {
		file.delete();
		return this;
	}

	/**
	 * 删除文件
	 * 
	 * @param filename
	 *            文件名
	 */
	public FileUtil moveTo() {
		file.delete();
		return this;
	}

	/**
	 * 删除文件
	 */
	public FileUtil delete() {
		file.delete();
		return this;
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
	
	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath
	 *            the filePath to set
	 */
	public FileUtil setFilePath(String filePath) {
		this.filePath = filePath;
		file = new File(filePath); // 同时设置 File 对象

		return this;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public FileUtil setFile(File file) {
		this.file = file;
		return this;
	}

	/**
	 * @return the overwrite
	 */
	public boolean isOverwrite() {
		return overwrite;
	}

	/**
	 * @param overwrite
	 *            the overwrite to set
	 */
	public FileUtil setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
		return this;
	}

}
