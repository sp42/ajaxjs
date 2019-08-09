package com.ajaxjs.util.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Test;

import com.ajaxjs.util.io.image.ImageUtil;
import com.ajaxjs.util.resource.AbstractScanner;

public class TestImageUtil {
	String img = AbstractScanner.getResourceFilePath(TestIoHelper.class, "test.jpg");
	
	@Test
	public void 获取图片大小() {
		assertEquals(130, new ImageUtil().setFilePath(img).getSize().getWidth());
	}

	@Test
	public void 修改图片大小() {
		assertNotNull(new ImageUtil().setFilePath(img).setHeight(200).setWidth(150).resize().save());
	}

	@Test
	public void 图片裁切() {
		assertNotNull(new ImageUtil().setFilePath(img).cut(20, 150, 500, 500).save());
	}

	@Test
	public void 水印文字() {
		new ImageUtil().setFilePath(img).mark("我们并肩坐在冰凉的台阶上，有风吹过，有爱来过。却最终被我们，一起错过。 原来，时间真的像流水，走得悄无声息。很多时光，很多事情，很多人，永远只能存在记忆里。").save();
	}

//	@Test
	public void 水印图片() {
		new ImageUtil().setFilePath(img).mark(new File("c://temp//watermark.jpg")).save();
	}
}