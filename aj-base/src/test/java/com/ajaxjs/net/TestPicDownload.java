package com.ajaxjs.net;

import org.junit.Test;

import com.ajaxjs.net.http.PicDownload;

public class TestPicDownload {

	@Test
	public void test() {
		String[] testArr = { "https://bbsimage.res.meizu.com/forum/2019/01/23/153122zrz85kuvubbiibbs.jpg", "https://static.oschina.net/uploads/img/202011/16133302_u9TD.png",
				 "https://file.ituring.com.cn/SmallCover/20080d81a4fb8a268c40" };

		new PicDownload(testArr, "c:/temp", null).start();
	}
}
