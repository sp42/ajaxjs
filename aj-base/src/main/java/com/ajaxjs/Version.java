/**
 * Copyright sp42 frank@ajaxjs.com Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs;

import java.io.File;

import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 初始化，检测是否可以运行
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class Version {
	private static final LogHelper LOGGER = LogHelper.getLog(Version.class);

	/**
	 * 是否调试模式（开发模式）
	 */
	public static boolean isDebug;

	/**
	 * 源码磁盘目录
	 */
	public static final String srcFolder = new File(Version.class.getClassLoader().getResource("").getPath()).toString();

	/**
	 * 获取操作系统名称
	 */
	private static final String OS_NAME = System.getProperty("os.name").toLowerCase();

	/**
	 * 是否苹果操作系统
	 */
	public static final boolean isMac = OS_NAME.contains("mac");

	/**
	 * 是否视窗操作系统
	 */
	public static final boolean isWindows = OS_NAME.contains("window");

	/**
	 * 是否 Linux 操作系统
	 */
	public static final boolean isLinux = OS_NAME.contains("linux");

	static {
		System.setProperty("user.timezone", "GMT +08");// 设置中国时区

		// 仅仅支持 Oracle JVM
		String vendor = System.getProperty("java.vm.vendor");
		if (vendor.indexOf("Oracle") == -1 || vendor.contains("openJDK")) {
			LOGGER.warning("抱歉，本框架暂不支持 OpenJDK。 如果你是 Linux 系统，请把自带的 OpenJDK 卸载，改用 Oracle JVM");
			System.exit(1);
		}

		// 版本检测
		if (Double.parseDouble(System.getProperty("java.specification.version")) < 1.5) {/* or Major Version, get java.runtime.version */
			LOGGER.warning("请升级你的 JRE/JDK版本 >= 1.8");
			System.exit(1);
		}

		/*
		 * 有两种模式：本地模式和远程模式（自动判断） 返回 true 表示是非 linux 环境，为开发调试的环境，即 isDebug = true； 返回
		 * false 表示在部署的 linux 环境下。 Linux 的为远程模式
		 */
		isDebug = !(OS_NAME.indexOf("nix") >= 0 || OS_NAME.indexOf("nux") >= 0 || OS_NAME.indexOf("aix") > 0);

		LOGGER.infoYellow("\n     ___       _       ___  __    __      _   _____        _          __  _____   _____  \n"
				+ "     /   |     | |     /   | \\ \\  / /     | | /  ___/      | |        / / | ____| |  _  \\ \n"
				+ "    / /| |     | |    / /| |  \\ \\/ /      | | | |___       | |  __   / /  | |__   | |_| |  \n"
				+ "   / / | |  _  | |   / / | |   }  {    _  | | \\___  \\      | | /  | / /   |  __|  |  _  {  \n"
				+ "  / /  | | | |_| |  / /  | |  / /\\ \\  | |_| |  ___| |      | |/   |/ /    | |___  | |_| |  \n"
				+ " /_/   |_| \\_____/ /_/   |_| /_/  \\_\\ \\_____/ /_____/      |___/|___/     |_____| |_____/ \n");

		LOGGER.infoGreen("AJAXJS-Base 加载完毕，当前是[" + (isDebug ? "调试" : "生产环境") + "]模式");
	}

	/**
	 * 检测是否 tomcat 以及版本
	 * 
	 * @param serverInfo 字符串如 Tomcat/8
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
