package com.ajaxjs.test.util.io;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.ajaxjs.util.IO.text;
import com.ajaxjs.util.stream.Image;
import com.ajaxjs.util.stream.Web;


public class TestWeb {
	@Test
	public void testGetRemote2local() throws IOException  {
		String saveTo = "c:/temp/dsd.jpg";
		// img
		Web.getRemote2local("http://nutzam.com/imgs/fe.png", saveTo);
		assertNotNull(Image.readImg(saveTo));
		
		// js
		saveTo = "c:/temp/js.js";
		Web.getRemote2local("http://bdimg.share.baidu.com/static/api/js/base/tangram.js?v=37768233.js", saveTo);
		assertNotNull(text.readFile(saveTo));
	}
}
