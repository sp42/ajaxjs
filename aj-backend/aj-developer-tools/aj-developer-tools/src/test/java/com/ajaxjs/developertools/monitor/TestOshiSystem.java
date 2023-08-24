package com.ajaxjs.developertools.monitor;

import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.ajaxjs.developertools.monitor.oshi.OshiSystemMonitor;
import org.junit.Test;

import com.ajaxjs.util.map.JsonHelper;

public class TestOshiSystem {
    public static String prettyJson(String json) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine scriptEngine = manager.getEngineByName("JavaScript");
        scriptEngine.put("jsonString", json);

        try {
            scriptEngine.eval("result = JSON.stringify(JSON.parse(jsonString), null, 2)");
        } catch (ScriptException e) {
            e.printStackTrace();
        }

        return (String) scriptEngine.get("result");
    }

    @Test
    public void testMonitor() {
        Map<String, Object> map = OshiSystemMonitor.get();
        String json = JsonHelper.toJson(map);

        System.out.println(prettyJson(json));
    }

    @Test
    public void t() {
        OshiSystemMonitor oshiMonitorExtra = new OshiSystemMonitor();
        System.out.println(oshiMonitorExtra.getComputerSystem());
        oshiMonitorExtra.getOtherHardware();
//		System.out.println(oshiMonitorExtra.getProcessList());
//		System.out.println(oshiMonitorExtra.getProcessList2());
    }
}
