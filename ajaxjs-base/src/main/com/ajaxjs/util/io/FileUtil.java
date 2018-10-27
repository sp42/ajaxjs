/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 文件工具类。 可返回本实例供链式调用
 * 
 * @author Sp42 frank@ajaxjs.com
 */
public class FileUtil extends StreamChain<FileUtil> {
	private static final LogHelper LOGGER = LogHelper.getLog(FileUtil.class);

	private String filePath;

	private File file;

	private boolean overwrite;

	/**
	 * 读取文件内容
	 * 
	 * @return 返回本实例供链式调用
	 */
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
	 * 
	 * @param path
	 *            文件磁盘路径
	 * @return 文件内容
	 */
	public static String openAsText(String path) {
		return new FileUtil().setFilePath(path).read().byteStream2stringStream().close().getContent();
	}

	/**
	 * 保存文件
	 * 
	 * @param off
	 * @param len
	 * @return 返回本实例供链式调用
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
	 * 
	 * @return 返回本实例供链式调用
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
	 * @return 文件名集合
	 */
	public String[] getFiles() {
		return file.isDirectory() ? file.list() : null;
	}

	/**
	 * 复制文件 TODO
	 * 
	 * @return 返回本实例供链式调用
	 */
	public FileUtil copyTo() {
		return this;
	}

	/**
	 * 移动文件 TODO
	 * 
	 * @return 返回本实例供链式调用
	 */
	public FileUtil moveTo() {
		return this;
	}

	/**
	 * 删除文件
	 * 
	 * @return 返回本实例供链式调用
	 */
	public FileUtil delete() {
		LOGGER.info("文件 {0} 准备删除！");
		if (!file.delete())
			LOGGER.warning("文件 {0} 删除失败！", file.toString());

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
	 * 获取文件名的 MIME 类型。检测手段不会真正打开文件进行检查而是单纯文件名的字符串判断。
	 * 
	 * @param filename
	 *            文件名
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

	/**
	 * Shorthand for saving text file
	 * 
	 * @param fullPath
	 *            Full path of the file
	 * @param text
	 *            The content of file, in text.
	 */
	public static void save(String fullPath, String text) {
		LOGGER.info("正在保存文件{0}， 保存内容：\n{1}", fullPath, text);
		new FileUtil().setFilePath(fullPath).setOverwrite(true).setContent(text).save().close();
	}

	/**
	 * 创建文件，注意这是一个空的文件。如果没有指定目录则创建；检测是否可以覆盖文件
	 * 
	 * @param fullPath
	 *            文件完整路径，最后一个元素是文件名
	 * @param isOverwrite
	 *            是否覆盖文件
	 * @return 文件对象
	 * @throws IOException
	 */
	public static File createFile(String fullPath, boolean isOverwrite) throws IOException {
		LOGGER.info("正在新建文件 {0}", fullPath);
		
		mkDir(fullPath);

		File file = new File(fullPath);
		if (!isOverwrite && file.exists())
			throw new IOException("文件已经存在，禁止覆盖！");

		return file;
	}

	/**
	 * 如果没有输出目录则先创建
	 * 
	 * @param fullPath
	 *            完整路径，最后一个元素为文件名
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
	 * 写入流到输出
	 * 
	 * @param in 输入流
	 * @param out 输出流
	 * @param bufferSize 缓冲区大小
	 */
	public static void write(InputStream in, OutputStream out, int bufferSize) {
		int readSize; // 读取到的数据长度
		byte[] buffer = new byte[bufferSize]; // 通过 byte 作为数据中转，用于存放循环读取的临时数据

		try {
			while ((readSize = in.read(buffer, 0, bufferSize)) != -1) {
				out.write(buffer, 0, readSize);
			}

			out.flush();
		} catch (IOException e) {
			LOGGER.warning(e);
		}
	}

	private static final int BUFFER_SIZE = 2 * 1024;

	/**
	 * 压缩文件
	 * 
	 * @param dir 要压缩的本地目录
	 * @param save 保存的文件名，例如 c:\\temp\\foo.zip
	 * @return 如果压缩成功返回 true
	 */
	public static boolean toZip(String dir, String save) {
		long start = System.currentTimeMillis();
		File sourceFile = new File(dir);

		try (OutputStream saveOut = new FileOutputStream(new File(save)); ZipOutputStream zos = new ZipOutputStream(saveOut);) {
			compress(sourceFile, zos, sourceFile.getName());

			LOGGER.info("压缩完成，耗时：" + (System.currentTimeMillis() - start) + " ms");
			return true;
		} catch (IOException e) {
			LOGGER.warning(e);
			return false;
		}
	}

	/**
	 * 压缩文件
	 * 
	 * @param sourceFile 目录或文件
	 * @param zos 压缩流
	 * @param name 压缩后的名称
	 * @throws IOException
	 */
	private static void compress(File sourceFile, ZipOutputStream zos, String name) throws IOException {
		if (sourceFile.isFile()) {
			zos.putNextEntry(new ZipEntry(name));

			try (InputStream in = new FileInputStream(sourceFile);) {
				write(in, zos, BUFFER_SIZE);
			}

			zos.closeEntry();
		} else {
			File[] listFiles = sourceFile.listFiles();

			if (listFiles == null || listFiles.length == 0) {
				// 空文件夹的处理 没有文件，不需要文件的copy
				zos.putNextEntry(new ZipEntry(name + "/"));
				zos.closeEntry();
			} else {
				for (File file : listFiles) {
					compress(file, zos, name + "\\" + file.getName());
				}
			}
		}
	}
}
