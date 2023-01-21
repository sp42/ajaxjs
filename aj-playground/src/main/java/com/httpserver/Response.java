package com.httpserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by windwant on 2016/6/12.
 */
public class Response {
	private static final int BUFFER_SIZE = 1024;

	public void setRequest(Request request) {
		this.request = request;
	}

	Request request;

	OutputStream out;

	SocketChannel osc;

	public Response(OutputStream out) {
		this.out = out;
	}

	public Response(SocketChannel osc) {
		this.osc = osc;
	}

	public void response() {
		byte[] b = new byte[BUFFER_SIZE];
		File file = new File(HttpServer.WEB_ROOT, request.getUri());

		try {
			StringBuilder sb = new StringBuilder();
			if (file.exists()) {
				FileInputStream fi = new FileInputStream(file);
				int ch = 0;

				while ((ch = fi.read(b, 0, BUFFER_SIZE)) > 0)
					out.write(b, 0, ch);

				out.flush();
			} else {
				sb.append("HTTP/1.1 404 File Not Found \r\n");
				sb.append("Content-Type: text/html\r\n");
				sb.append("Content-Length: 24\r\n");
				sb.append("\r\n");
				sb.append("<h1>File Not Found!</h1>");
				out.write(sb.toString().getBytes());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void responseNIO() {
		byte[] b = new byte[BUFFER_SIZE];
		File file = new File(HttpServer.WEB_ROOT, request.getUri());

		try {
			StringBuilder sb = new StringBuilder();

			if (file != null && file.exists()) {
				FileInputStream fi = new FileInputStream(file);

				while (fi.read(b) > 0) {
					osc.write(ByteBuffer.wrap(b));
					b = new byte[BUFFER_SIZE];
				}
			} else {
				sb.append("HTTP/1.1 404 File Not Found \r\n");
				sb.append("Content-Type: text/html\r\n");
				sb.append("Content-Length: 24\r\n");
				sb.append("\r\n");
				sb.append("<h1>File Not Found!</h1>");
				osc.write(ByteBuffer.wrap(sb.toString().getBytes()));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}