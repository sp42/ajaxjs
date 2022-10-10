package com.ajaxjs.net.websocket;

import javax.websocket.Session;

/**
 * 已连接的客户端
 */
public class WebSocketEntity {
	private Session session;

	public WebSocketEntity(Session session) {
		this.session = session;
	}

	public void sendText(String message) {
		session.getAsyncRemote().sendText(message);
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
}
