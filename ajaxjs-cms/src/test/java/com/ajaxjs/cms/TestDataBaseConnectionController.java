package com.ajaxjs.cms;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.cms.controller.DataBaseShowStruController;

public class TestDataBaseConnectionController {
	@Test
	public void testService() {
		List<Map<String, String>> list = DataBaseShowStruController.getConnectionConfig("C:\\project\\zyjf_admin\\WebContent\\META-INF\\context.xml");
		
		assertEquals("jdbc/mysql", list.get(0).get("name"));
	}
}
