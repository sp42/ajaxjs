package com.ajaxjs.cms.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ajaxjs.Version;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 实时浏览 Tomcat 日志
 * 
 * 參考自 https://blog.csdn.net/xiao__gui/article/details/50041673 混合了 WebSocket
 * 
 * @author Frank Cheung
 *
 */
@ServerEndpoint("/tomcat_log")
@Path("/admin/tomcat-log")
public class TomcatLogController implements IController {
	private static final LogHelper LOGGER = LogHelper.getLog(TomcatLogController.class);

	@GET
	public String UI() {
		LOGGER.info("实时浏览 Tomcat 日志");

		return BaseController.jsp("admin/tomcat-log");
	}

	/**
	 * 
	 * @author Frank Cheung
	 *
	 */
	public static class TailLogThread extends Thread {
		private BufferedReader reader;
		private Session session;

		public TailLogThread(InputStream in, Session session) {
			this.reader = new BufferedReader(new InputStreamReader(in));
			this.session = session;
		}

		@Override
		public void run() {

			String line;
			try {
				if (Version.isLinux) {
					while ((line = reader.readLine()) != null) {
						// 将实时日志通过WebSocket发送给客户端，给每一行添加一个HTML换行
						session.getBasicRemote().sendText(line + "<br>");
					}
				} else {
					session.getBasicRemote().sendText("該功能只支持 Linux 操作系統。");
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private Process process;

	private InputStream inputStream;

	/**
	 * 新的WebSocket请求开启
	 */
	@OnOpen
	public void onOpen(Session session) {
		try {
			String today = CommonUtil.now(CommonUtil.commonDateFormat_shortest);
			String path = ConfigService.getValueAsString("System.tomcat_log") + String.format("catalina.%s.log", today);
			System.out.println(path);
			// 执行tail -f命令
			process = Runtime.getRuntime().exec("tail -f " + path);
			inputStream = process.getInputStream();

//			String logFile = "C:\\sp42\\dev\\eclipse-workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\logs\\localhost_access_log.2019-01-31.txt";
//			inputStream = new FileInputStream(new File(logFile));

			TailLogThread thread = new TailLogThread(inputStream, session);// 一定要启动新的线程，防止InputStream阻塞处理WebSocket的线程
			thread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * WebSocket请求关闭
	 */
	@OnClose
	public void onClose() {
		try {
			if (inputStream != null)
				inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (process != null)
			process.destroy();
	}

	@OnError
	public void onError(Throwable thr) {
		thr.printStackTrace();
	}

}
