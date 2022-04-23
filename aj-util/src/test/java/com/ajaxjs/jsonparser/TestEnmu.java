package com.ajaxjs.jsonparser;

import org.junit.Test;

import com.ajaxjs.framework.BaseModel;

/**
 * 测试枚举的序列化
 * 
 * @author Frank Cheung
 *
 */
public class TestEnmu {
	class Bean extends BaseModel {
	}

	Bean bean = new Bean();

	@Test
	public void test() {
//		bean.setStat(Status.ONLINE);
//		System.out.println(JsonHelper.toJson(bean));
	}
}
