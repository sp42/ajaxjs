package com.ajaxjs.net.websocket;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.BiConsumer;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.ajaxjs.util.logger.LogHelper;

/**
 * WebSocket 客户端
 * 
 * @author xinzhang
 *
 */
@ClientEndpoint
public class WebSocketClient {
	private static final LogHelper LOGGER = LogHelper.getLog(WebSocketClient.class);

	protected WebSocketContainer container;

	protected Session userSession;

	/**
	 * 创建 WebSocket 客户端
	 */
	public WebSocketClient() {
		container = ContainerProvider.getWebSocketContainer();
	}

	/**
	 * 连接 WebSocket 服务器
	 * 
	 * @param server 服务器地址
	 */
	public void connect(String server) {
		try {
			userSession = container.connectToServer(this, new URI(server));
		} catch (DeploymentException | URISyntaxException | IOException e) {
			LOGGER.warning("WS 地址： " + server);
			LOGGER.warning(e);
		}
	}

	/**
	 * 发送信息
	 * 
	 * @param msg 信息
	 * @throws IOException
	 */
	public void sendMessage(String msg) {
		try {
			userSession.getBasicRemote().sendText(msg);
		} catch (IOException e) {
			LOGGER.warning(e);
		}
	}

	@OnOpen
	public void onOpen(Session session) {
		LOGGER.info("WebSocket Connected");
	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
	}

	private BiConsumer<Session, String> onMessage;

	/**
	 * 有消息推到的时候触发
	 * 
	 * @param session
	 * @param msg
	 */
	@OnMessage
	public void onMessage(Session session, String msg) {
		LOGGER.info(msg);

		if (onMessage != null)
			onMessage.accept(session, msg);
	}

	/**
	 * 关闭链接
	 * 
	 * @throws IOException
	 */
	public void disconnect() {
		try {
			userSession.close();
		} catch (IOException e) {
			LOGGER.warning(e);
		}
	}

	public BiConsumer<Session, String> getOnMessage() {
		return onMessage;
	}

	public void setOnMessage(BiConsumer<Session, String> onMessage) {
		this.onMessage = onMessage;
	}
}