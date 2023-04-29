package com.httpserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by windwant on 2016/6/12.
 */
public class HttpServer {
	public static final String WEB_ROOT = System.getProperty("user.dir") + "\\src\\test\\resources\\webroot";
	public static final int SERVER_PORT = 8888;
	public static final String SERVER_IP = "127.0.0.1";

	public static void main(String[] args) {
		HttpServer httpServer = new HttpServer();
		httpServer.await();
	}

	public void await() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(SERVER_PORT, 1, InetAddress.getByName(SERVER_IP));
			
			while (true) {
				Socket socket = serverSocket.accept();
				InputStream in = socket.getInputStream();
				OutputStream out = socket.getOutputStream();
				Request request = new Request(in);
				request.read();

				Response response = new Response(out);
				response.setRequest(request);
				response.response();
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}