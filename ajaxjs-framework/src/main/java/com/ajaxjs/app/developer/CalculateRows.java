package com.ajaxjs.app.developer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import com.ajaxjs.util.CommonUtil;

/**
 * 代码行数统计
 * 
 * @author https://blog.csdn.net/u014800380/article/details/73770823
 *
 */
public class CalculateRows {
	static long classcount = 0; // Java类的数量
	static long normalLines = 0; // 空行
	static long commentLines = 0; // 注释行
	static long writeLines = 0; // 代码行
	static long allLines = 0; // 代码行

	public static void main(String[] args) {
		File f = new File("D:\\project\\ajaxjs-web\\src\\main\\java\\com\\ajaxjs\\web\\captcha\\CaptchaController.java"); // 目录
		String type = ".java";// 查找什么类型的代码，如".java"就是查找以java开发的代码量，".php"就是查找以PHP开发的代码量
//		treeFile(f, type);
		countFile(f);
		Logger.getGlobal().info("路径：" + f.getPath());
		Logger.getGlobal().info(type + "类数量：" + classcount);
		Logger.getGlobal().info("代码数量：" + writeLines);
		Logger.getGlobal().info("注释数量：" + commentLines);
		Logger.getGlobal().info("空行数量：" + normalLines);

		if (classcount == 0)
			Logger.getGlobal().info("代码平均数量:" + 0);
		else
			Logger.getGlobal().info("代码平均数量:" + writeLines / classcount);

		Logger.getGlobal().info("总 行数量：" + allLines);
	}

	/**
	 * 查找出一个目录下所有的.java文件
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