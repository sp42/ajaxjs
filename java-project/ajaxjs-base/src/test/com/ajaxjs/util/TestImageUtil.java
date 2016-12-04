package test.com.ajaxjs.util;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.ajaxjs.util.ImageUtil;

public class TestImageUtil {
	@Test
	public void 修改图片大小() {
		new ImageUtil().setFilePath("c://temp//XMM.jpg").setHeight(200).setWidth(150).resize().save();
	}

	@Test
	public void 图片裁切() {
		new ImageUtil().setFilePath("c://temp//XMM.jpg").cut(20, 150, 500, 500).save();
	}

	@Test
	public void 水印文字() {
		new ImageUtil().setFilePath("c://temp//XMM.jpg").mark("我们并肩坐在冰凉的台阶上，有风吹过，有爱来过。却最终被我们，一起错过。 原来，时间真的像流水，走得悄无声息。很多时光，很多事情，很多人，永远只能存在记忆里。").save();
	}

	@Test
	public void 水印图片() {
		new ImageUtil().setFilePath("c://temp//XMM.jpg").mark(new File("c://temp//watermark.jpg")).save();

	}

	@Test
	public void 截屏() {
		// Image.webscreenCut("c://temp//webscreenCut.jpg");
		assertNotNull(true);
	}
}