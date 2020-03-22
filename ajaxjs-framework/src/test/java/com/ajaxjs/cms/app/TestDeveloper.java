package com.ajaxjs.cms.app;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.cms.app.developer.DataBaseStruController;
import com.ajaxjs.cms.app.developer.TomcatLogController.LogFileTailer;

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
	
	@Test
	public void testService() {
		List<Map<String, String>> list = DataBaseStruController.getConnectionConfig("C:\\project\\zyjf_admin\\WebContent\\META-INF\\context.xml");
		
		assertEquals("jdbc/mysql", list.get(0).get("name"));
	}
}
