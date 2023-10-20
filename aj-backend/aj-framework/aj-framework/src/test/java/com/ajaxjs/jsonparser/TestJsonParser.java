package com.ajaxjs.jsonparser;

import com.ajaxjs.jsonparser.lexer.Lexer;
import com.ajaxjs.jsonparser.lexer.Token;
import com.ajaxjs.jsonparser.lexer.Tokens;
import com.ajaxjs.jsonparser.syntax.FMS;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class TestJsonParser {
    String str = "{foo:[],\n\ta:[1,-23333,-0.3,0.17,5.2,\"\\u82B1\\u6979~\"],\n\tb:[\"a\tbc\",\"12  3\",\"4,5\\\"6\",{\n\t\t\t\t\tx:1,\n\t\t\t\t\ty:\"cc\\ncc\"\n\t\t\t\t},4.56],\n\t\"text\":\"I'm OK~\",\n\t\"1-2\":234,\n\tmybool:false,\n\tmynull:null,\n\tmyreal:true\n}\n";

    @Test
    public void testJSONLex() {
        Lexer lexer = new Lexer(str);
        Token tk;
        int i = 0;

        while ((tk = lexer.next()) != Tokens.EOF) {
            i++;
            assertNotNull(tk);
        }

        assertEquals(i, 65);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void testParser() {
        Object o;
        Map<String, Object> map;

        o = new FMS(str).parse();

        map = (Map<String, Object>) o;
        assertEquals("I'm OK~", map.get("text").toString());
        assertEquals(1, ((List) map.get("a")).get(0));
    }
}
