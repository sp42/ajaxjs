package com.ajaxjs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class FileChain extends StreamChain {

	public static void main(String[] args) {
		FileChain f = new FileChain();
		f.setFilePath("c:\\temp\\a.txt").setOverwrite(true).save("hihi").close();
		f.setFilePath("c:\\temp\\a.txt").read().byteStream2stringStream().close();

		String fileContent = f.getContent();
		System.out.println(fileContent);
	}

	private String filePath;

	private File file;

	private boolean overwrite;

	public FileChain read() {
		try {
			if (!file.exists())
				throw new FileNotFoundException(file.getPath() + " 不存在！");

			setIn(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 保存文件
	 * 
	 * @param off
	 * @param len
	 * @return
	 */
	public FileChain save(int off, int len) {
		OutputStream out = null;

		try {
			out = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		setOut(out);

		try {
			out.write(getData(), off, len);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return this;
	}

	/**
	 * 
	 * @param content
	 *            文件内容，文本
	 * @return
	 */
	public FileChain save(String content) {
		OutputStream out = null;

		try {
			out = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		setOut(out);

		// OutputStreramWriter将输出的字符流转化为字节流输出（字符流已带缓冲）
		try (OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);) {
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
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
	public FileChain copyTo() {
		file.delete();
		return this;
	}

	/**
	 * 删除文件
	 * 
	 * @param filename
	 *            文件名
	 */
	public FileChain moveTo() {
		file.delete();
		return this;
	}


	/**
	 * 删除文件
	 */
	public FileChain delete() {
		file.delete();
		return this;
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
	public FileChain setFilePath(String filePath) {
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
	public FileChain setFile(File file) {
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
	public FileChain setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
		return this;
	}

}
