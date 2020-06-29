package com.ajaxjs.util;

import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Test;


public class TestXmlHelper {
	@Test
	public void test() {
		String xml = "C:\\project\\doctor\\WebContent\\META-INF\\context.xml";
		Map<String, String> map = XmlHelper.nodeAsMap(xml, "//Resource[@name='jdbc/mys2ql']");
		assertNotNull(map);
	}
}
