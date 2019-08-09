package com.ajaxjs.cms.utils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.MapTool;
import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.io.IoHelper;

/**
 * MySQL 数据库定时自动备份，仅支持 Centos
 *
 */
@WebListener
public class MySqlBackup implements ServletContextListener {
	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent e) {
		String configFile = MvcRequest.mappath(e.getServletContext(), "/META-INF/context.xml");
		new BackupTask(configFile);
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	public static void main(String[] args) {
		Map<String, String> map = BackupTask.loadConfig("C:\\project\\register\\WebContent\\META-INF\\context.xml", "jdbc/mysql_deploy");
		System.out.println(map);
	}

	public static class BackupTask extends TimerTask {
		public BackupTask(String configFile) {
			boolean isEnable = ConfigService.getValueAsBool("");
			if (!isEnable)
				return;

			loadConfig(configFile, "jdbc/mysql_deploy");

			// 获取并处理配置文件中的时间
			String backuptime = "";
			String[] time = backuptime.split(":");
			int hours = Integer.parseInt(time[0]);
			int minute = Integer.parseInt(time[1]);
			int second = Integer.parseInt(time[2]);

			Calendar calendar = Calendar.getInstance();

			/*** 定制每日2:00执行方法 ***/
			calendar.set(Calendar.HOUR_OF_DAY, hours);
			calendar.set(Calendar.MINUTE, minute);
			calendar.set(Calendar.SECOND, second);

			Date date = calendar.getTime(); // 第一次执行定时任务的时间

			// 如果第一次执行定时任务的时间 小于 当前的时间。此时要在 第一次执行定时任务的时间 加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
			if (date.before(new Date()))
				date = addDay(date, 1);

			// 安排指定的任务在指定的时间开始进行重复的固定延迟执行。
			new Timer().schedule(this, date, PERIOD_DAY);
		}

		public static Map<String, String> loadConfig(String configFile, String name) {
			String xml = FileHelper.readFile(configFile);
			// 多个 resources 节点组成
			String[] results = CommonUtil.regMatchAll("(?<=<Resource)[^>]+(?<!/)", xml);

			if (results != null) {
				for (String result : results) {
					Map<String, Object> map = MapTool.toMap(result.trim().split("\\s+"), null);
					if (("\"" + name + "\"").equals(map.get("name").toString())) {
						Map<String, String> _map = new HashMap<>();
						_map = MapTool.as(map, v -> v == null ? null : v.toString().replaceAll("^\"|\"$", ""));
						_map.put("host", CommonUtil.regMatch("(?<=mysql://)[^/]+", _map.get("url")));
						_map.put("databaseName", CommonUtil.regMatch("\\w+(?=\\?)", _map.get("url")));
						System.out.println(_map.get("databaseName"));
						return _map;
					}
				}
			}

			return null;
		}

		// 时间间隔 一天时间
		private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;

		/**
		 * 增加或减少天数
		 * 
		 * @param date
		 * @param num
		 * @return
		 */
		private static Date addDay(Date date, int num) {
			Calendar start = Calendar.getInstance();
			start.setTime(date);
			start.add(Calendar.DAY_OF_MONTH, num);

			return start.getTime();
		}

		private String hostIP = "47.106.105.193", userName = "root", password = "root123abc", databaseName = "ng_register";

		@Override
		public void run() {
			try {
				Process process = Runtime.getRuntime().exec("C:\\Program Files\\MySQL\\MySQL Workbench 6.3 CE\\mysqldump -h" + hostIP + " -u" + userName + " -p" + password + " --set-charset=UTF8 " + databaseName);
				String sql = IoHelper.byteStream2string(process.getInputStream());
				FileHelper.save("c:/temp", CommonUtil.now("yyyy-MM-dd_HH-mm-ss") + ".sql", sql);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
