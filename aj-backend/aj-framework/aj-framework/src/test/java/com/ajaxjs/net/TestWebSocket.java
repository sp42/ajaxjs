package com.ajaxjs.net;

import java.io.IOException;

import org.junit.Test;

import com.ajaxjs.net.websocket.WebSocketClient;

public class TestWebSocket {
	@Test
	public void start() throws InterruptedException, IOException {
		WebSocketClient wsc = new WebSocketClient();
		wsc.connect("ws://localhost:8080/WebSocketServer/anno/chat");
		Thread.sleep(1000);
		wsc.disconnect();
	}
}
