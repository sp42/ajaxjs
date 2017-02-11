package com.ajaxjs.net.ftp;

import static org.junit.Assert.*;

import java.io.IOException;

import com.ajaxjs.net.ftp.UploadFtp;

public class Test {
	@org.junit.Test
	public void testSend(){
		UploadFtp client = null;
		
		try {
			client = new UploadFtp("192.168.61.83", 21);
			assertNotNull(client);
			
			client.login("upup", "upup@123");
//			client.PutFile("D:\\code.jar", "/test/1344439.jar");
			client.upload("E:\\aa.mp4", "/temp/zzz.mp4");
			client.closeServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
