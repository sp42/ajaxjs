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

import com.ajaxjs.util.io.Resources;
import com.ajaxjs.util.logger.LogHelper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;

/**
 * 初始化，检测是否可以运行
 */
public class Version {
    private static final LogHelper LOGGER = LogHelper.getLog(Version.class);

    /**
     * 获取操作系统名称
     */
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();

    /**
     * 是否调试模式（开发模式）
     */
    public static boolean isDebug;

    static {
        if (!"Asia/Shanghai".equals(System.getProperty("user.timezone")))
            LOGGER.warning("当前 JVM 非中国大陆时区");

        // 版本检测
        if (Double.parseDouble(System.getProperty("java.specification.version")) < 1.5) {/* or Major Version, get java.runtime.version */
            LOGGER.warning("请升级你的 JRE/JDK版本 >= 1.8");
//			System.exit(1);
        }

        /*
         * 有两种模式：本地模式和远程模式（自动判断） 返回 true 表示是非 linux 环境，为开发调试的环境，即 isDebug = true； 返回
         * false 表示在部署的 linux 环境下。 Linux 的为远程模式
         */
        isDebug = !(OS_NAME.contains("nix") || OS_NAME.contains("nux") || OS_NAME.indexOf("aix") > 0);

//		if (!TestHelper.isRunningTest()) {
//			LOGGER.infoYellow("\n     ___       _       ___  __    __      _   _____        _          __  _____   _____  \n"
//					+ "     /   |     | |     /   | \\ \\  / /     | | /  ___/      | |        / / | ____| |  _  \\ \n"
//					+ "    / /| |     | |    / /| |  \\ \\/ /      | | | |___       | |  __   / /  | |__   | |_| |  \n"
//					+ "   / / | |  _  | |   / / | |   }  {    _  | | \\___  \\      | | /  | / /   |  __|  |  _  {  \n"
//					+ "  / /  | | | |_| |  / /  | |  / /\\ \\  | |_| |  ___| |      | |/   |/ /    | |___  | |_| |  \n"
//					+ " /_/   |_| \\_____/ /_/   |_| /_/  \\_\\ \\_____/ /_____/      |___/|___/     |_____| |_____/ \n");

//			LOGGER.infoGreen("Util 加载完毕，当前是[" + (isDebug ? "调试" : "生产环境") + "]模式");
//		}
    }
}
