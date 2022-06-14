package com.ajaxjs.tomcat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.ajaxjs.util.logger.LogHelper;

/**
 * 通过 OS 的命令查询 Tomcat 状态
 * 
 * @author https://github.com/535404515/MYSQL-TOMCAT-MONITOR/blob/master/nlpms-task-monitor/src/main/java/com/nuoli/mysqlprotect/timer/TomcatServiceJob.java
 *
 */
public class SimpleMonitor {
	private static final LogHelper LOGGER = LogHelper.getLog(SimpleMonitor.class);

	private static String tomcatNames;

	public static void ping() {
		final Runtime runtime = Runtime.getRuntime();

		try {
			String[] tomcatName = tomcatNames.split(",");
			List<String> tomcatNames = new ArrayList<>();
			for (int i = 0; i < tomcatName.length; i++)
				tomcatNames.add(tomcatName[i]);

			Process netStart = runtime.exec("net start");

			try (InputStream in = netStart.getInputStream(); BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));) {
				String info = null;
				List<String> list = new ArrayList<>();

				while ((info = bufferedReader.readLine()) != null) {
					if (info.trim().contains("Apache Tomcat"))
						list.add(info.trim());
				}

				if (tomcatNames.size() != list.size()) {
					for (int i = 0; i < tomcatNames.size(); i++) {
						for (int j = 0; j < list.size(); j++) {
							if (tomcatNames.get(i).equals(list.get(j))) {
								tomcatNames.remove(i);
								netStart(runtime, tomcatNames);
							}
						}
					}
				}

				if (list.size() == 0)
					netStart(runtime, tomcatNames);
			}
		} catch (IOException e) {
			LOGGER.warning(e);
		}
	}

	private static void netStart(Runtime runtime, List<String> tomcatNames) {
		for (String tomcatServiceName : tomcatNames) {
			tomcatServiceName = tomcatServiceName.replace("Apache Tomcat 8.5", "");
			tomcatServiceName = tomcatServiceName.trim();

			try {
				runtime.exec("net start " + tomcatServiceName);
				LOGGER.info("找不到服务，重新启动服务:" + tomcatServiceName);
			} catch (IOException e) {
				LOGGER.warning(e);
			}
		}
	}
}
