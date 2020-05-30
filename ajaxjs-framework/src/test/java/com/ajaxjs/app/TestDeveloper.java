package com.ajaxjs.app;

import org.junit.Test;

import com.ajaxjs.app.developer.TomcatLogController.LogFileTailer;

public class TestDeveloper {
	@Test
	public void testBackup() {
		String d = "C:/sp42/dev/eclipse-workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/ajaxjs-demo/WEB-INF/lib";
		System.out.println(d.replaceAll("/", "\\\\"));
	}

	@Test
	public void testTomcatLog() {
		LogFileTailer tailer = new LogFileTailer("C:\\temp\\bar.txt", 1000, true);
		tailer.setTailing(true);
		tailer.addListener(System.out::println);
		tailer.start();
	}
}
