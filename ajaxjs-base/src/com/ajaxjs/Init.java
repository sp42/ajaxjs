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
package com.ajaxjs;

/**
 * 初始化，检测是否可以运行
 * 
 * @author frank
 *
 */
public class Init {
	/**
	 * 是否调试模式（开发模式）
	 */
	public static boolean isDebug;

	/**
	 * 源码磁盘目录
	 */
	public static final String srcFolder = Init.class.getClassLoader().getResource("").getPath();

	/**
	 * 是否苹果系统
	 */
	public static final boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");

	/**
	 * 为控制台显示更清晰些，设定一个分割线
	 */
	public static final String ConsoleDiver = System.getProperty("line.separator") + "---------------------------------";
	
	public static final String lineFeet = "\r\n";
	public final static char newline = '\n';

	static {
		System.out.println("Starting up....Ajaxjs-base 初始化" + ConsoleDiver);
//		System.setProperty("user.timezone", "GMT +08");// 微软云设置时区

		if (System.getProperty("java.vm.vendor").indexOf("Oracle") == -1 || System.getProperty("java.vm.vendor").contains("openJDK")) {
			System.err.println("本框架不支持 OpenJDK!如果你是 Linux 系统，请把自带的 OpenJDK 卸载，改用 Oracle JVM");
			System.exit(1);
		}

		// 版本检测
		if (System.getProperty("java.version").contains("1.7.") || System.getProperty("java.version").contains("1.8.")) {
		} else {
			System.err.println("请升级你的 JRE/JDK版本 >= 1.7");
			System.exit(1);
		}

		/*
		 * 有两种模式：本地模式和远程模式（自动判断） 返回 true 表示是非 linux 环境，为开发调试的环境，即 isDebug =
		 * true； 返回 false 表示在部署的 linux 环境下。 Linux 的为远程模式
		 */
		final String OS = System.getProperty("os.name").toLowerCase();
		isDebug = !(OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);

		if (isMac)
			System.out.println("亲，你运行着 Mac！" + ConsoleDiver);
	}
}
