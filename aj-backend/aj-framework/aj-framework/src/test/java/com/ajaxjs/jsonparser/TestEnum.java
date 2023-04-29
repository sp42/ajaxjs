package com.ajaxjs.jsonparser;

import org.junit.Test;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.util.map.JsonHelper;

/**
 * 测试枚举的序列化
 * 
 * @author Frank Cheung
 *
 */
public class TestEnum {
	public static enum State {
		ONLINE, OFFLINE
	}

	public class Bean extends BaseModel {
		private State state;

		public State getState() {
			return state;
		}

		public void setState(State state) {
			this.state = state;
		}
	}

	Bean bean = new Bean();

	@Test
	public void test() {
		bean.setState(State.ONLINE);
		System.out.println(JsonHelper.toJson(bean));
	}
}
