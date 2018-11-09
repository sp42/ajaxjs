package com.ajaxjs.js;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.script.ScriptException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class TestJSEngineWrapper {
	@Test
	public void testEngineFactory() {
		assertNotNull(JsEngineWrapper.engineFactory());
	}

	JsEngineWrapper engine;

	@BeforeClass
	public static void init() {
	}

	@Before
	public void setUp() {
		engine = new JsEngineWrapper();
	}

	@Test
	public void testLoad() {
		Object obj;

		engine.load(TestJSEngineWrapper.class.getResource("test.js").getFile().toString());
		obj = engine.eval("foo");
		assertNotNull(obj);

		engine.load(TestJSEngineWrapper.class, "test.js");
		obj = engine.eval("foo");
		assertNotNull(obj);
	}

	@Test
	public void testPut() throws ScriptException {
		engine.put("a", 6);
		Object obj = engine.eval("a");

		assertNotNull(obj);
		assertEquals(obj, 6);
	}

	@Test
	public void testGet() {
		engine.eval("a = {b:{c:{d:1}}};");

		assertNotNull(engine.get("a"));
		assertNotNull(engine.get("a", "b", "c", "d"));
	}

	@Test
	public void testCall() throws ScriptException {
		engine.eval("function max_num(a, b){return (a > b) ? a : b;}");
		Object obj = engine.call("max_num", Object.class, null, 6, 4);

		assertEquals(6, obj);
	}

	@Test
	public void testEval() throws ScriptException {
		engine.eval("var foo ='Hello World!';");
		assertEquals(engine.eval("foo"), "Hello World!");
		assertEquals(engine.eval("foo = 'Nice day!';"), "Nice day!");
		assertEquals(engine.eval("foo", String.class), "Nice day!");

		engine.eval("bar = 111;");
		assertEquals(new Integer(111), engine.eval("bar;", Double.class));// js Number 于 Java 为 Double

		engine.eval("bar = false;");
		assertEquals(engine.eval("bar;", Boolean.class), false);
	}

}
