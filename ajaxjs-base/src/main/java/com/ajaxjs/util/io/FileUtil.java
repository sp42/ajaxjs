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
		
		LOGGER.info("写文件: " + file);

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
	 * 删除文件
	 * 
	 * @return 返回本实例供链式调用
	 */
	public FileUtil delete() {
		LOGGER.info("文件 {0} 准备删除！", file.toString());
		if (!file.delete())
			LOGGER.warning("文件 {0} 删除失败！", file.toString());

		return this;
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath the filePath to set
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
	 * @param file the file to set
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
	 * @param overwrite the overwrite to set
	 */
	public FileUtil setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
		return this;
	}

	/**
	 * Shorthand for saving text file
	 * 
	 * @param fullPath Full path of the file
	 * @param text     The content of file, in text.
	 */
	public static void save(String fullPath, String text) {
		LOGGER.info("正在保存文件{0}， 保存内容：\n{1}", fullPath, text);
		new FileUtil().setFilePath(fullPath).setOverwrite(true).setContent(text).save().close();
	}

	/**
	 * 创建文件，注意这是一个空的文件。如果没有指定目录则创建；检测是否可以覆盖文件
	 * 
	 * @param fullPath    文件完整路径，最后一个元素是文件名
	 * @param isOverwrite 是否覆盖文件
	 * @return 文件对象
	 * @throws IOException
	 */
	public static File createFile(String fullPath, boolean isOverwrite) throws IOException {
		LOGGER.info("正在新建文件 {0}", fullPath);

		FileHelper.mkDir(fullPath);

		File file = new File(fullPath);
		if (!isOverwrite && file.exists())
			throw new IOException("文件已经存在，禁止覆盖！");

		return file;
	}

	/**
	 * 写入流到输出
	 * 
	 * @param in         输入流
	 * @param out        输出流
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

	static final int BUFFER_SIZE = 2 * 1024;
}
