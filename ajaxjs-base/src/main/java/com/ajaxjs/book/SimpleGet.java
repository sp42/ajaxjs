package com.ajaxjs.book;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class SimpleGet {
	static String url = "https://www.baidu.com";

	public static void main(String[] args) throws Exception {
		URL link = new URL(url);
		try (InputStream is = link.openStream(); // Java 7 autoClose 语法
				BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));) {
			StringBuilder strBuilder = new StringBuilder();
			String sLine = null;

			while ((sLine = br.readLine()) != null) {
				strBuilder.append(sLine);
				strBuilder.append("\r\n");
			}

			String content = strBuilder.toString();
			System.out.println(content);
		}
	}
}