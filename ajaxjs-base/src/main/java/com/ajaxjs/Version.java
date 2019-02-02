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
package com.ajaxjs;

import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 初始化，检测是否可以运行
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class Version {
	private static final LogHelper LOGGER = LogHelper.getLog(Version.class);

	/**
	 * 是否调试模式（开发模式）
	 */
	public final static boolean isDebug;

	/**
	 * 源码磁盘目录
	 */
	public static final String srcFolder = Version.class.getClassLoader().getResource("").getPath();

	/**
	 * 是否苹果操作系统
	 */
	public static final boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");

	/**
	 * 是否视窗操作系统
	 */
	public static final boolean isWindows = System.getProperty("os.name").toLowerCase().contains("window");
	
	/**
	 * 是否 Linux 操作系统
	 */
	public static final boolean isLinux = System.getProperty("os.name").toLowerCase().contains("linux");

	static {
		// System.setProperty("user.timezone", "GMT +08");// 微软云设置时区
		if (System.getProperty("java.vm.vendor").indexOf("Oracle") == -1
				|| System.getProperty("java.vm.vendor").contains("openJDK")) {
			LOGGER.warning("本框架不支持 OpenJDK!如果你是 Linux 系统，请把自带的 OpenJDK 卸载，改用 Oracle JVM");
			System.exit(1);
		}

		// 版本检测
		if (System.getProperty("java.version").contains("1.8.")) {
		} else {
			LOGGER.warning("请升级你的 JRE/JDK版本 >= 1.8");
			System.exit(1);
		}

		/*
		 * 有两种模式：本地模式和远程模式（自动判断） 返回 true 表示是非 linux 环境，为开发调试的环境，即 isDebug = true； 返回
		 * false 表示在部署的 linux 环境下。 Linux 的为远程模式
		 */
		final String OS = System.getProperty("os.name").toLowerCase();
		isDebug = !(OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);

		LOGGER.infoGreen("AJAXJS-Base 加载完毕，当前是" + (isDebug ? "调试" : "生产环境") + "模式");
	}

	/**
	 * 检测是否 tomcat 以及版本
	 * 
	 * @param serverInfo 字符串如 Tomcat/7
	 */
	public static void tomcatVersionDetect(String serverInfo) {
		String result = CommonUtil.regMatch("(?<=Tomcat/)(\\d)", serverInfo);

		if (result != null) {
			try {
				if (Integer.parseInt(result) < 8)
					throw new UnsupportedOperationException("不支持低于 Tomcat 8 以下的版本！");
			} catch (Throwable e) {
				if (e instanceof UnsupportedOperationException)
					throw e;

				LOGGER.warning(e);// 忽略其他异常，如正则的
			}
		} else {
			// 不是 tomcat
		}
	}
}
