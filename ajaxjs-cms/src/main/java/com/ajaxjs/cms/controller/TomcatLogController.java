package com.ajaxjs.cms.controller;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.util.logger.LogHelper;

@ServerEndpoint("/tomcat_log")
@Path("/admin/tomcat-log")
public class TomcatLogController implements IController {
	private static final LogHelper LOGGER = LogHelper.getLog(TomcatLogController.class);

	private LogFileTailer tailer;

	@GET
	public String UI() {
		LOGGER.info("实时浏览 Tomcat 日志");
		return BaseController.jsp("admin/tomcat-log");
	}

	/**
	 * 新的WebSocket请求开启
	 */
	@OnOpen
	public void onOpen(Session session) {
		tailer = new LogFileTailer("C:\\temp\\bar.txt", 1000, true);
		tailer.setTailing(true);
		tailer.addListener(log -> {
			try {
				session.getBasicRemote().sendText(log + "<br />");
			} catch (IOException e) {
				LOGGER.warning(e);
			}
		});
		tailer.start();
	}

	/**
	 * WebSocket请求关闭
	 */
	@OnClose
	public void onClose() {
		tailer.setTailing(false);
	}

	@OnError
	public void onError(Throwable thr) {
		thr.printStackTrace();
	}
}
