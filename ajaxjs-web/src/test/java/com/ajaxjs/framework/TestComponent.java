package com.ajaxjs.framework;

import java.util.function.Consumer;

import javax.servlet.ServletContextEvent;

import org.junit.Test;

import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.EveryClass;

public class TestComponent {
	public class Foo implements Component {
		Consumer<ServletContextEvent> onServletStartUp = e -> {

		};
	}

	@Test
	public void test() {
		EveryClass scan = new EveryClass();
		scan.scan((resource, packageName) -> {
			if (resource.contains("SetStartupCtx"))
				ReflectUtil.getClassByName(resource);
		}, "com");
	}
}
