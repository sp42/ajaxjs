package com.ajaxjs.workflow.model;

import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Test;

import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.workflow.BaseTest;
import com.ajaxjs.workflow.process.ProcessDefinition;

public class TestToJsonHelper extends BaseTest {
	@Test
	public void testRead() {
		ProcessDefinition def = service.findById(240L);
		assertNotNull(def);
		String json = ToJsonHelper.getModelJson(def.getModel());
		System.out.println(json);
		Map<String, Object> map = JsonHelper.parseMap(json);

		System.out.println(map.get("states"));
	}
}
