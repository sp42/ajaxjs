package com.ajaxjs.cms.common.developer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.ajaxjs.util.CommonUtil;

/**
 * 代码行数统计
 * 
 * @author https://blog.csdn.net/u014800380/article/details/73770823
 *
 */
public class CalculateRows {
	public static long classcount = 0; // Java类的数量
	public static long normalLines = 0; // 空行
	public static long commentLines = 0; // 注释行
	public static long writeLines = 0; // 代码行
	public static long allLines = 0; // 代码行

	/**
	 * 查找出一个目录下所有的.java文件
	 * 
	 * @param f    指定的文件目录
	 * @param type
	 */
	public static void treeFile(File f, String type) {
		File[] childs = f.listFiles();

		if (CommonUtil.isNull(childs))
			return;

		for (File file : childs) {
			if (!file.isDirectory()) {
				if (file.getName().endsWith(type))
					countFile(file);
			} else
				treeFile(file, type);
		}
	}

	/**
	 * 
	 * @param file
	 */
	public static void countFile(File file) {
		classcount++;
		boolean comment = false;

		try (BufferedReader br = new BufferedReader(new FileReader(file));) {
			String line = "";

			while ((line = br.readLine()) != null) {
				allLines++;
				line = line.trim();

				if (line.matches("^[//s&&[^//n]]*$")) {// 这一行匹配以空格开头，但不是以回车符开头，但以回车符结尾
					normalLines++;
				} else if (line.startsWith("/*") && !line.endsWith("*/")) {// 匹配以/*......*/括住的多行注释
					commentLines++;
					comment = true;
				} else if (true == comment) {
					commentLines++;

					if (line.endsWith("*/"))
						comment = false;
					// 匹配以//开头的单行注释，及以/*......*/括住的单行注释
				} else if (line.startsWith("//") || (line.startsWith("/*") && line.endsWith("*/")))
					commentLines++;
				else // 其他的就是代码行
					writeLines++;
			}
		} catch (IOException e) {

		}
	}

}