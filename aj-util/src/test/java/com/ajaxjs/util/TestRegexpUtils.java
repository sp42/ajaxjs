package com.ajaxjs.util;

import java.util.Map;

import org.junit.Test;

import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.util.regexp.RegexpUtils;

public class TestRegexpUtils {
	static String json = "{\"magic\":254,\"len\":28,\"sysid\":3,\"compid\":1,\"payload\":{\"pitchspeed\":-2.2399216E-4,"
			+ "\"roll\":2.8490436,\"pitch\":0.9943852,\"rollspeed\":-5.5177254E-5,\"yawspeed\":0.0013602356,"
			+ "\"time_boot_ms\":268235,\"yaw\":0.6807801},\"checksum\":33597,\"msgid\":30,\"seq\":176}";

	@Test
	public void testCallback() {
		String j = RegexpUtils.kexuejishu(json);
		System.out.println(j);
		Map<String, Object> map = JsonHelper.parseMap(j);
		@SuppressWarnings("unchecked")
		Map<String, Object> payload = (Map<String, Object> )map.get("payload");
		System.out.println(payload.get("pitchspeed"));
	}
}
