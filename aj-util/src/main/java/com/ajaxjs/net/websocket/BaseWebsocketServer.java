package com.ajaxjs.net.websocket;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.Session;

import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;

/**
 * WebSocket 服务端基类
 */
public abstract class BaseWebsocketServer {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseWebsocketServer.class);

	/**
	 * 已连接的客户端
	 */
	protected static final Set<WebSocketEntity> CONNECTIONS = new CopyOnWriteArraySet<>();

	/**
	 * 发送对象给客户端
	 * 
	 * @param obj
	 */
	public void sendMessageJson(Object obj) {
		sendMessage(JsonHelper.toJson(obj));
	}

	/**
	 * 发送文本消息给客户端
	 * 
	 * @param msg
	 */
	public void sendMessage(String msg) {
		for (WebSocketEntity clients : CONNECTIONS) {
			clients.sendText(msg);
		}
	}

	/**
	 * 连接关闭后触发的方法
	 */
	@OnClose
	public void onClose(Session session, CloseReason reason) {
		LOGGER.info("WebSocket 关闭");
		WebSocketEntity toRemove = null;

		for (WebSocketEntity e : CONNECTIONS) {
			if (e.getSession().equals(session)) {
//				LOGGER.info("找到被移除的 ws");
				toRemove = e;
				break;
			}
		}

		if (toRemove != null)
			CONNECTIONS.remove(toRemove);
	}

	/**
	 * 发生错误时触发的方法
	 */
	@OnError
	public void onError(Session session, Throwable e) {
		LOGGER.warning(session.getId() + " 连接发生错误 " + e.getMessage());
		e.printStackTrace();
	}
}