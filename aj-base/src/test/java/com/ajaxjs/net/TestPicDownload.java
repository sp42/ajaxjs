package com.ajaxjs.net;

import org.junit.Test;

import com.ajaxjs.net.http.PicDownload;

public class TestPicDownload {

	@Test
	public void test() {
		String[] testArr = { "https://bbsimage.res.meizu.com/forum/2019/01/23/153122zrz85kuvubbiibbs.jpg", "http://531.yishu1000.com/201906/004/1.jpg",
				"http://531.yishu1000.com/201906/004/2.jpg", "http://531.yishu1000.com/201906/004/3.jpg" };

		new PicDownload(testArr, "c:/temp", null).start();
	}
}
