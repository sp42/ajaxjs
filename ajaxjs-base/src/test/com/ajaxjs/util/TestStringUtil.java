package test.com.ajaxjs.util;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.junit.Test;

import static com.ajaxjs.util.StringUtil.*;

public class TestStringUtil {
	@Test
	public void testIsEmptyString() {
		assertTrue(isEmptyString(""));
		assertTrue(isEmptyString(" "));
		assertTrue(isEmptyString(null));
	}
	
	@Test
	public void testStringJoin() {
		assertEquals(stringJoin(new String[]{"a", "b", "c"}, ","), "a,b,c");
		assertEquals(stringJoin(new ArrayList<String>(){

			private static final long serialVersionUID = 1L;
			{
				add("a");
				add("b");
				add("c");
			}
		}, ","), "a,b,c");
	}
	
	@Test
	public void testRepeatStr() {
		assertEquals(repeatStr("Hi", ",", 3), "Hi,Hi,Hi");
	}	
	
	@Test
	public void testContainsIgnoreCase() {
		assertTrue(containsIgnoreCase("abc", "A"));
	}	
	@Test
	public void testRegMatch() {
		assertEquals(regMatch("^a", "abc"), "a");
		assertEquals(regMatch("^a", "abc", 0), "a");
		assertEquals(regMatch("^a(b)", "abc", 1), "b");
	}	
	
	static String str = "中国";
	
	@Test
	public void testUrlDecode() {
		assertEquals(urlDecode(urlEncode(str)), str);
	}
	
	@Test
	public void testMisc(){
		assertEquals(base64Decode(base64Encode(str)), str);
		assertEquals(md5("123123"), "4297F44B13955235245B2497399D7A93");
		System.out.println(passwordGenerator(8));
		assertTrue(isUUID(getUUID()));
	}
	
	@Test
	public void testUnicodeHex() {
		assertTrue(str.equals(decodeUnicode(encodeUnicode(str))));
	}
	
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
