package com.ajaxjs.framework.embeded_tomcat;

import lombok.Data;

import java.io.*;
import java.net.Socket;

/**
 * a TCP socket wrapper on client side, easy to use.
 *
 * @author zollty
 * @since 2017-1-12
 */
@Data
public class SocketClientImpl {
    private String distHost;

    private int distPort;

    public String send(String msg) {
        try {
            return send(msg, distHost, distPort);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String send(String msg, String host, int port) throws IOException {
        try (Socket socket = new Socket(host, port)) {
            write(msg, socket);

            String ackMsg = socketRead(socket);
            socket.shutdownInput();
            System.out.println("[" + System.currentTimeMillis() + "] Reply from server " + host + ":" + port + ": ");
            System.out.println("\t" + ackMsg);
            return ackMsg;
        }
    }

    public String socketRead(Socket socket) throws IOException {
        socket.setSoTimeout(5000);

        int byteCount = 0;
        char[] buffer = new char[4096];
        int bytesRead;

        try (InputStreamReader in = new InputStreamReader(socket.getInputStream()); StringWriter out = new StringWriter()) {
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                byteCount += bytesRead;
            }
//            out.flush();
            return out.toString();
        }
    }

    void write(String msg, Socket socket) throws IOException {
        BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
        out.write(msg.getBytes());
        out.flush();

        socket.shutdownOutput();
    }
}