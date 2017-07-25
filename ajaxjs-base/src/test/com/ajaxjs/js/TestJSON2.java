package test.com.ajaxjs.js;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.js.json.JSONParser;
import com.ajaxjs.js.json.lexer.Lexer;
import com.ajaxjs.js.json.lexer.Token;
import com.ajaxjs.js.json.lexer.Tokens;

public class TestJSON2 {
	String str = "{foo:[],\n\ta:[1,-23333,-0.3,0.17,5.2,\"\\u82B1\\u6979~\"],\n\tb:[\"a\tbc\",\"12  3\",\"4,5\\\"6\",{\n\t\t\t\t\tx:1,\n\t\t\t\t\ty:\"cc\\ncc\"\n\t\t\t\t},4.56],\n\t\"text\":\"I'm OK~\",\n\t\"1-2\":234,\n\tmybool:false,\n\tmynull:null,\n\tmyreal:true\n}\n";
	
//	@Test
	public void testJSONLex() {
		Lexer jl = new Lexer(str);
		Token tk = null;
		while ((tk = jl.next()) != Tokens.EOF) {
//			System.out.println(tk);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testParser(){
		Object o;
		Map<String, Object> map;
		
		o = JSONParser.parse(str);
		System.out.println(o);
		
		map = (Map<String, Object>)o;
		assertTrue(map.get("text").toString().equals("I'm OK~"));
		assertEquals(((List)map.get("a")).get(0), 1.0);
//		System.out.println(((List)map.get("a")).get(5));
	}
}
