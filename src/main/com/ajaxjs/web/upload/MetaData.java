package com.ajaxjs.web.upload;

import com.ajaxjs.util.StringUtil;

public class MetaData {
	/**
	 * 表单字段名
	 */
	public String name;
	/**
	 * 文件名
	 */
	public String filename;

	/**
	 * 文件类型
	 */
	public String contentType;

	/**
	 * 文件大小
	 */
	public int contentLength;

	public void parseMeta(String dataStr) {
		name = StringUtil.regMatch("name=\"(\\w+)\"", dataStr, 1);
		if (name == null)
			throw new IllegalArgumentException("你的表单中没有设置一个 name，不能获取字段");

		filename = StringUtil.regMatch("filename=\"([\\w.]+)\"", dataStr, 1);
		contentType = StringUtil.regMatch("Content-Type:\\s?([\\w/]+)", dataStr, 1);
		System.out.println(contentType);
	}

	private final static byte[] b = "\n".getBytes();

	public static int get(byte[] dataBytes) {
		int skip = 0;

		for (int i = 0; i < dataBytes.length; i++) {
			int temp = i, j = 0;
			while (dataBytes[temp] == b[j]) {
				temp++;
				j++;
				if (j == b.length) {
					skip++;
					if (skip == 3)
						return i + 3;// why plus 3?

					break;
				}
			}
		}

		return 0;
	}
}
