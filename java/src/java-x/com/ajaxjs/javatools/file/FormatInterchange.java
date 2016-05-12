package com.ajaxjs.javatools.file;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * 文件或者文件夹目录下文件的类型转换：UTF-8与GBK 互转
 * 
 * @author weidong
 * 
 */
public class FormatInterchange {
	/**
	 * 将UTF-8格式的source文件或者此路径下的所有子文件转换成GBK格式，并存放到destination路径下<br>
	 * （若是文件夹，树形结构保持不变）<br>
	 * （SVN的.svn目录则直接复制，不转换）
	 * 
	 * @param source
	 *            需要转换的UTF-8文件或者文件夹（文件夹下所有文件需都是UTF-8格式）
	 * @param destination
	 *            转换成的GBK文件存放的路径(无需包含文件名，此函数将按照原有的文件名生成对应文件)
	 * @return true 转换成功； false 转换失败
	 */
	public boolean transfer_UTF8_To_GBK(String source, String destination) {
		File src = new File(source);
		if (!src.exists()) {
			System.out.println("文件源： " + source + " 不存在！");
			return false;
		}
		// 单个文件，则直接转换
		if (src.isFile()) {
			if (!destination.endsWith(File.separator))
				destination += File.separator;
			
			File dest = new File(destination);
			if (!dest.exists())
				dest.mkdirs();
			
			try {
				this.transfer_UTF8_to_GBK(source, destination + src.getName());
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} else {// 如果是目录，则按照原来树形结构迭代进行转换和保存
			File[] files = src.listFiles();
			if (files == null) {
				System.out.println("文件源： " + source + " 下没有文件！");
				return false;
			}
			for (File temp : files) {
				if (!destination.endsWith(File.separator))
					destination += File.separator;
				if (temp.getName().equals(".svn")) {
					this.copyFile(temp.getAbsolutePath(), destination + temp.getParentFile().getName());
					continue;
				}
				this.transfer_UTF8_To_GBK(temp.getAbsolutePath(), destination
						+ temp.getParentFile().getName());
			}

		}

		return true;
	}

	/**
	 * 将GBK格式的source文件或者此路径下的所有子文件转换成UTF-8格式，并存放到destination路径下<br>
	 * （若是文件夹，树形结构保持不变）<br>
	 * （SVN的.svn目录则直接复制，不转换）
	 * 
	 * @param source
	 *            需要转换的GBK文件或者文件夹（文件夹下所有文件需都是GBK格式）
	 * @param destination
	 *            转换成的UTF-8文件存放的路径(无需包含文件名，此函数将按照原有的文件名生成对应文件)
	 * @return true 转换成功； false 转换失败
	 */
	public boolean transfer_GBK_To_UTF8(String source, String destination) {

		File src = new File(source);
		if (!src.exists()) {
			System.out.println("文件源： " + source + " 不存在！");
			return false;
		}
		// 单个文件，则直接转换
		if (src.isFile()) {
			if (!destination.endsWith(File.separator))
				destination += File.separator;
			File dest = new File(destination);
			if (!dest.exists()) {
				dest.mkdirs();
			}
			try {
				this.transfer_GBK_to_UTF8(source, destination + src.getName());
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} else {// 如果是目录，则按照原来树形结构迭代进行转换和保存

			File[] files = src.listFiles();
			if (files == null) {
				System.out.println("文件源： " + source + " 下没有文件！");
				return false;
			}
			for (File temp : files) {
				if (!destination.endsWith(File.separator))
					destination += File.separator;
				if (temp.getName().equals(".svn")) {
					this.copyFile(temp.getAbsolutePath(), destination
							+ temp.getParentFile().getName());
					continue;
				}
				this.transfer_GBK_To_UTF8(temp.getAbsolutePath(), destination
						+ temp.getParentFile().getName());
			}

		}

		return true;
	}

	/**
	 * 转换格式：GBK to UTF-8
	 * 
	 * @param srcFileName
	 *            需要转换的GBK文件路径位置
	 * @param destFileName
	 *            转换成的UTF-8文件存放的路径位置
	 * @throws IOException
	 */
	private void transfer_GBK_to_UTF8(String srcFileName, String destFileName) throws IOException {
		String lineSeparator = System.getProperty("line.separator");
		FileInputStream fis = new FileInputStream(srcFileName);
		StringBuffer content = new StringBuffer();
		DataInputStream in = new DataInputStream(fis);
		BufferedReader d = new BufferedReader(new InputStreamReader(in, "GBK"));
		
		String line = null;
		while ((line = d.readLine()) != null)
			content.append(line + lineSeparator);
		
		d.close();
		in.close();
		fis.close();

		Writer ow = new OutputStreamWriter(new FileOutputStream(destFileName), "utf-8");
		ow.write(content.toString());
		ow.close();
	}

	/**
	 * 转换格式：UTF-8 to GBK
	 * 
	 * @param srcFileName
	 *            需要转换的UTF-8文件路径位置
	 * @param destFileName
	 *            转换成的GBK文件存放的路径位置
	 * @throws IOException
	 */
	private void transfer_UTF8_to_GBK(String srcFileName, String destFileName) throws IOException {
		String lineSeparator = System.getProperty("line.separator");
		FileInputStream fis = new FileInputStream(srcFileName);
		StringBuffer content = new StringBuffer();
		DataInputStream in = new DataInputStream(fis);
		BufferedReader d = new BufferedReader(new InputStreamReader(in, "utf-8"));
		String line = null;
		
		while ((line = d.readLine()) != null)
			content.append(line + lineSeparator);
		
		d.close();
		in.close();
		fis.close();

		Writer ow = new OutputStreamWriter(new FileOutputStream(destFileName), "GBK");
		ow.write(content.toString());
		ow.close();
	}

	/**
	 * 复制文件或文件夹，如果遇到同名的文件或文件夹，则覆盖
	 * 
	 * @param sourcePath
	 *            源文件或文件夹的完整路径名
	 * @param targetPath
	 *            目标文件夹的完整路径
	 * @return
	 */
	private boolean copyFile(String sourcePath, String targetPath) {

		// 返回值
		boolean error = true;

		// 执行过程
		try {
			if (!targetPath.endsWith("/") || !targetPath.endsWith("\\"))
				targetPath += "\\";
			File dir = new File(targetPath);
			File file = new File(sourcePath);

			if (file.exists()) {
				if (file.isFile()) {
					// 删除目标路径上的同名文件
					String dirPath = dir.getAbsolutePath();
					if (!dirPath.endsWith(File.separator))
						dirPath += File.separator;
					
					// 移动文件
					if (file.renameTo(new File(dir, file.getName()))) {
						error = error && true;
					} else {
						error = error && false;
					}
				} else if (file.isDirectory()) {
					// 删除目标路径上的同名文件夹
					String dirPath = dir.getAbsolutePath();
					if (!dirPath.endsWith(File.separator)) {
						dirPath += File.separator;
					}
					this.deleteFile(dirPath + file.getName());

					// 在目标路径上新建一个空的同名文件夹
					File newDir = new File(dirPath + file.getName());
					error = error && newDir.mkdirs();

					// 移动文件夹的内容
					boolean error_d = true;
					File[] files = file.listFiles();
					for (File tempFile : files) {
						error_d = error_d && this.copyFile(tempFile.getAbsolutePath(), dirPath + file.getName());
					}
					error = error && error_d;
				} else {
					error = false;
				}
			} else {
				error = false;
			}
		} catch (Exception e) {
			error = false;
		}

		// 返回
		return error;
	}

	/**
	 * 删除文件或文件夹
	 * 
	 * @param path
	 *            要删除文件的完整路径名，或文件夹的完整路径名
	 * @return
	 */
	private boolean deleteFile(String path) {

		// 返回值
		boolean error = true;

		// 执行过程
		try {
			File file = new File(path);
			if (file.exists()) {
				if (file.isDirectory()) {
					boolean error_d = true;

					// 删除文件夹的内容
					File[] files = file.listFiles();
					for (File tempFile : files) {
						error_d = error_d
								&& this.deleteFile(tempFile.getAbsolutePath());
					}

					// 删除空文件夹
					if (file.delete()) {
						error = error_d && true;
					} else {
						error = error_d && false;
					}
				} else if (file.isFile()) {
					if (file.delete()) {
					} else {
						error = false;
					}
				} else {
					error = false;
				}
			} else {
				error = false;
			}
		} catch (Exception e) {
			error = false;
		}

		// 返回
		return error;
	}

	public static void main(String[] args) {

		FormatInterchange fi = new FormatInterchange();
		// 将C:\test\src目录下的所有文件转换成UTF-8格式，存放到C:\test\UTF8目录下，文件目录树形结构保持不变（SVN的.svn目录则直接复制，不转换）
		fi.transfer_GBK_To_UTF8(
				"C:\\project\\webSecurity-master\\src\\main\\java\\org\\websecurity",
				"C:\\temp\\UTF8");
		// //将C:\test\UTF8\src目录下的所有文件转换成GBK格式，存放到C:\test目录下，文件目录树形结构保持不变（SVN的.svn目录则直接复制，不转换）
		// fi.transfer_UTF8_To_GBK("C:\\test\\UTF8\\src", "C:\\test");

	}

}