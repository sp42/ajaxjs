package com.ajaxjs;

import org.junit.Test;

public class TestVersion {
	@Test
	public void testVersion() {
		new Version();
		Version.tomcatVersionDetect("Tomcat/8");
	}
}
