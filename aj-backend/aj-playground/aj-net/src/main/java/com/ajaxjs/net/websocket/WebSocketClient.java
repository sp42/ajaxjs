/**
 * Copyright Sp42 frank@ajaxjs.com Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.net.websocket;

import com.ajaxjs.util.ThreadUtil;
import com.ajaxjs.util.logger.LogHelper;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

/**
 * WebSocket 客户端
 *
 * @author xinzhang
 */
@ClientEndpoint
public class WebSocketClient {
    private static final LogHelper LOGGER = LogHelper.getLog(WebSocketClient.class);

    protected WebSocketContainer container;

    protected Session userSession;

    private String server;

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
        this.server = server;
        connect();
    }

    /**
     * 连接 WebSocket 服务器
     */
    public void connect() {
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
        tryReconnect.set(false);
        circlePing();
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        LOGGER.info("WebSocket 连接断开！code: {0}, reason: {1}", reason.getCloseCode(), reason.getReasonPhrase());

        if (end.get())
            return;

        needReconnect();
    }

    private BiConsumer<Session, String> onMessage;

    /**
     * 有消息推到的时候触发
     *
     * @param session 会话对象
     * @param msg     消息
     */
    @OnMessage
    public void onMessage(Session session, String msg) {
//		LOGGER.info(msg);

        if (onMessage != null)
            onMessage.accept(session, msg);
    }

    /**
     * 需要ping标识
     */
    private final AtomicBoolean needPing = new AtomicBoolean(true);

    /**
     * 尝试重连标识
     */
    private final AtomicBoolean tryReconnect = new AtomicBoolean(false);

    /**
     * 重连次数
     */
    private final AtomicInteger reConnectTimes = new AtomicInteger(0);

    /**
     * 连接结束标识
     */
    private final AtomicBoolean end = new AtomicBoolean(false);

    private static ByteBuffer PING_PAYLOAD = null;

    public void circlePing() {
        if (PING_PAYLOAD == null)
            PING_PAYLOAD = ByteBuffer.wrap("Ping".getBytes());

        new Thread(() -> {
            while (needPing.get()) {
                if (userSession != null && userSession.isOpen())
                    try {
                        userSession.getBasicRemote().sendPing(PING_PAYLOAD);
                    } catch (IllegalArgumentException | IOException e) {
                        LOGGER.warning(e);
                    }

                ThreadUtil.sleep(5, TimeUnit.SECONDS);
            }

            LOGGER.warning("[]Ping循环关闭");
        }).start();
    }

    /**
     * 重新连接
     */
    private void needReconnect() {
        ThreadUtil.sleep(3);
        int cul = reConnectTimes.incrementAndGet();

        if (cul > 3) {
            disconnect();// close("real stop");
            throw new NullPointerException("服务端断连，3次重连均失败");
        }

        LOGGER.warning("[{0}]第[{1}]次断开重连", cul);

        if (tryReconnect.get()) {
            LOGGER.warning("第[{0}]次断开重连结果 -> 连接正在重连，本次重连请求放弃", cul);
            needReconnect();

            return;
        }

        try {
            tryReconnect.set(true);

            if (userSession != null && userSession.isOpen()) {
                LOGGER.warning("[第[{0}]次断开重连，关闭旧连接", cul);
                disconnect();
            }

            container = ContainerProvider.getWebSocketContainer();
            connect();
        } catch (Exception exception) {
            LOGGER.warning("[第[{0}]次断开重连结果 -> 连接正在重连，重连异常:[{1}]", cul, exception.getMessage());
            needReconnect();
        } finally {
            tryReconnect.set(false);
        }
    }

    /**
     * 关闭链接
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