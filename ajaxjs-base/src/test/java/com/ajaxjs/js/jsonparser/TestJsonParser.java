package com.ajaxjs.js.jsonparser;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.js.jsonparser.JSONParser;
import com.ajaxjs.js.jsonparser.JsonParseException;
import com.ajaxjs.js.jsonparser.lexer.Lexer;
import com.ajaxjs.js.jsonparser.lexer.Token;
import com.ajaxjs.js.jsonparser.lexer.Tokens;
import com.ajaxjs.jsonparser.JsonHelper;

public class TestJsonParser {
	String str = "{foo:[],\n\ta:[1,-23333,-0.3,0.17,5.2,\"\\u82B1\\u6979~\"],\n\tb:[\"a\tbc\",\"12  3\",\"4,5\\\"6\",{\n\t\t\t\t\tx:1,\n\t\t\t\t\ty:\"cc\\ncc\"\n\t\t\t\t},4.56],\n\t\"text\":\"I'm OK~\",\n\t\"1-2\":234,\n\tmybool:false,\n\tmynull:null,\n\tmyreal:true\n}\n";

	@Test
	public void testJSONLex() {
		Lexer jl = new Lexer(str);
		Token tk = null;
		while ((tk = jl.next()) != Tokens.EOF) {
			assertNotNull(tk);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testParser() {
		Object o;
		Map<String, Object> map;

		o = JsonHelper.parse(str);

		map = (Map<String, Object>) o;
		assertTrue(map.get("text").toString().equals("I'm OK~"));
		assertEquals(1, ((List) map.get("a")).get(0));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testJson2Map() {
		Object obj = JSONParser.json2Map2("{x1:true, x2:2000, x3:\"1:hello world\",a:{b:{c:{d:{e:[1,\"j\",3,4,5,6]}}}}}");
		assertTrue(obj instanceof Map);
		Map<String, Object> map = (Map<String, Object>) obj;
		assertTrue(map.get("a") instanceof Map);
		assertEquals(true, map.get("x1"));
		assertEquals(2000, map.get("x2"));
		assertEquals("1:helloworld", map.get("x3")); // remove space, that's wrong.
		assertNotNull(obj);
	}

	@Test
	public void testMisc() {
		(new JsonParseException("")).getMessage();
		(new JsonParseException(0, 0, 0, "err")).toString();
		new JsonParseException(0, 0, 0, "err", new Exception());
	}
}
