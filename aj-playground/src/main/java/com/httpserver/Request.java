package com.httpserver;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by windwant on 2016/6/12.
 */
public class Request {
	private InputStream in;

	public String getUri() {
		return uri;
	}

	private String uri;

	public Request() {
	}

	public Request(InputStream in) {
		this.in = in;
	}

	public void read() {
		StringBuffer sb = new StringBuffer();
		int i = 0;
		byte[] b = new byte[2048];

		try {
			i = in.read(b);

			for (int j = 0; j < i; j++)
				sb.append((char) b[j]);

			takeUri(sb);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void takeUri(StringBuffer sb) {
		int i = sb.indexOf(" ");

		if (i > 0) {
			int j = sb.indexOf(" ", i + 1);

			if (j > 0) {
				uri = sb.substring(i + 1, j).toString();
				System.out.println("http request uri: " + uri);

				if (!(uri.endsWith("/index.html") || uri.endsWith("/test.jpg"))) {
					uri = "/404.html";
					System.out.println("http request uri rewrite: " + uri);
				}
			}
		}
	}

}