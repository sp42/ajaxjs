package test.com.ajaxjs;

import org.junit.Test;

import com.ajaxjs.Version;

public class TestVersion {
	@Test
	public void testVersion() {
		new Version();
		Version.tomcatVersionDetect("Tomcat/8");
	}
}
