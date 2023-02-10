package com.ajaxjs.util;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.ajaxjs.util.template.SimpleTempletUtil;
import com.ajaxjs.util.template.Templ;

public class TestTemplate {
	@Test
	public void test() {
		Set<String> paramNames = SimpleTempletUtil.getParamNames("恭喜您:$$name$$注册成功人人app,您的账号为:$$code$$", "$$");
		System.out.println(paramNames);
		assertEquals("[code, name]", paramNames.toString());

		Map<String, String> context = new HashMap<>();
		context.put("name", "小王");
		context.put("code", "125284588");

		String render = SimpleTempletUtil.render("恭喜您:$$name$$注册成功人人app,您的账号为:$$code$$", context);

		assertEquals("恭喜您:小王注册成功人人app,您的账号为:125284588", render);
		System.out.println(render);
	}

	@Test
	public void test2() {
		String t1 = "恭喜您 $$name$$, 注册成功人人app,您的账号为:$$code$$";
		String t2 = "你好 #{name}, 您的验证码是:${code}";

		Map<String, String> param = new HashMap<>();
		param.put("name", "小王");
		param.put("code", "13252623145");

		for (int i = 0; i < 100; i++) {
			SimpleTempletUtil.render(t1, param);
			Templ.of(t2).render(param);
		}

		String result = "";
		long start = 0;

		start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			param.put("code", i + "");
			result = SimpleTempletUtil.render(t1, param);
		}

		System.out.println(System.currentTimeMillis() - start);
		System.out.println(result);

		start = System.currentTimeMillis();
		Templ templ = Templ.of(t2);

		for (int i = 0; i < 100000; i++) {
			param.put("code", i + "");
			result = templ.render(param);
		}

		System.out.println(System.currentTimeMillis() - start);
		System.out.println(result);

	}
}
