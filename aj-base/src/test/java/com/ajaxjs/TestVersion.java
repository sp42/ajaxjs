package com.ajaxjs;

import org.junit.Test;

import com.ajaxjs.util.Timer;

public class TestVersion {
	@Test
	public void testVersion() {
		Timer.set(1);
		new Version();
		Timer.set(2);
		Version.tomcatVersionDetect("Tomcat/8");
		Timer.print();
	}
}
