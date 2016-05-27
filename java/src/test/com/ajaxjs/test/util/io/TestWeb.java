package com.ajaxjs.test.util.io;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import com.ajaxjs.util.IO.text;
import com.ajaxjs.util.stream.Img;
import static com.ajaxjs.util.stream.Web.*;


public class TestWeb {
	@Test
	public void testGetRemote2local() throws IOException  {
		String saveTo = "c:/temp/dsd.jpg";
		// img
		getRemote2local("http://nutzam.com/imgs/fe.png", saveTo);
		assertNotNull(Img.readImg(saveTo));
		
		// js
		saveTo = "c:/temp/js.js";
		getRemote2local("http://bdimg.share.baidu.com/static/api/js/base/tangram.js?v=37768233.js", saveTo);
		assertNotNull(text.readFile(saveTo));
	}
	
	@Test
	public void testGetSize(){
		URL urlObj = null;
		
		try {
			urlObj = new URL("http://puui.qpic.cn/tv/0/2124112_124090/0");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		int size = 0;
		try {
			size = getSize((HttpURLConnection)urlObj.openConnection(), urlObj.getHost());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(size);
		assertNotNull(size > 0);
	}
}
