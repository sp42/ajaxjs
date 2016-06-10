package test.com.ajaxjs.util;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Test;

import com.ajaxjs.util.StringUtil;

public class TestStringUtil {
	@Test
	public void testIsEmptyString() {
		assertTrue(StringUtil.isEmptyString(""));
		assertTrue(StringUtil.isEmptyString(" "));
		assertTrue(StringUtil.isEmptyString(null));
	}
	
	@Test
	public void testUnicodeHex() {
	    String s = "简介";   
        String tt = StringUtil.gbEncoding(s);   
        tt = "你好，我想给你说一个事情";  
        System.out.println(StringUtil.decodeUnicode("\\u7b80\\u4ecb"));   
        System.out.println(StringUtil.decodeUnicode(tt));   
//        System.out.println(HTMLDecoder.decode("中国"));  
        tt = "\u7b80\u4ecb";  
        System.out.println(s.indexOf("\\")); 
        assertNull(s);
	}
	
	@Test
	public void testUTF8mb4() {
		System.out.println("test string=" + "😄walmart öbama 👽💔");
		String url = "jdbc:mysql://localhost:3306/foo?useUnicode=true&characterEncoding=UTF-8";
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection c = DriverManager.getConnection(url, "root", "123123");
			c.prepareStatement("SET NAMES 'utf8mb4'").execute();
			c.prepareStatement("insert into foo (name) values('😄walmart öbama 👽💔')").execute();
			PreparedStatement p = c.prepareStatement("select * from foo");
			p.execute();
			ResultSet rs = p.getResultSet();
			while (!rs.isLast()) {
				rs.next();
				String retrieved = rs.getString(1);
				System.out.println("retrieved=\"" + retrieved + "\"");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
