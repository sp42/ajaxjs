package com.ajaxjs.util.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.File;
import org.junit.Test;
import com.ajaxjs.util.resource.AbstractScanner;

public class TestImageHelper {
	String img = AbstractScanner.getResourcesFromClass(TestIoHelper.class, "test.jpg");

	@Test
	public void 获取图片大小() {
		assertEquals(406, new ImageHelper(img).width);
	}

	@Test
	public void 修改图片大小() {
		assertNotNull(ImageHelper.resizeImg(ImageHelper.getImg(img), 200, 150));
	}

	@Test
	public void 图片裁切() {
		assertNotNull(ImageHelper.cut(img, 20, 150, 500, 500));
	}

	@Test
	public void 水印文字() {
		assertNotNull(ImageHelper.mark(ImageHelper.getImg(img),
				"我们并肩坐在冰凉的台阶上，有风吹过，有爱来过。却最终被我们，一起错过。 原来，时间真的像流水，走得悄无声息。很多时光，很多事情，很多人，永远只能存在记忆里。"));
	}

	// @Test
	public void 水印图片() {
		assertNotNull(ImageHelper.mark(ImageHelper.getImg(img), new File("c://temp//watermark.jpg")));
	}
}