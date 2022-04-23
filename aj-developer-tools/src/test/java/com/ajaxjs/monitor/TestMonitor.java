package com.ajaxjs.monitor;

import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.Test;

import com.ajaxjs.util.map.JsonHelper;

public class TestMonitor {
	public static String prettyJson(String json) {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine scriptEngine = manager.getEngineByName("JavaScript");
		scriptEngine.put("jsonString", json);

		try {
			scriptEngine.eval("result = JSON.stringify(JSON.parse(jsonString), null, 2)");
		} catch (ScriptException e) {
			e.printStackTrace();
		}

		String prettyPrintedJson = (String) scriptEngine.get("result");
		return prettyPrintedJson;
	}

	@Test
	public void testMonitor() {
		Map<String, Object> map = OshiMonitor.get();
		String json = JsonHelper.toJson(map);

		System.out.println(prettyJson(json));
	}

	@Test
	public void t() {
		OshiMonitorExtra oshiMonitorExtra = new OshiMonitorExtra();
		System.out.println(oshiMonitorExtra.getComputerSystem());
		oshiMonitorExtra.getOtherHardware();
//		System.out.println(oshiMonitorExtra.getProcessList());
//		System.out.println(oshiMonitorExtra.getProcessList2());
		System.out.println(TomcatInfo.getTomcatPort());
	}
}
