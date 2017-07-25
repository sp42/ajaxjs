package test.com.ajaxjs.util;

import static org.junit.Assert.*;

import java.util.Date;

import static com.ajaxjs.util.DateTools.*;

import org.junit.Test;

import com.ajaxjs.util.DateTools;

public class TestDateTools {
	@Test
	public void testNow() {
		assertNotNull(now()); // 返回当前时间的 YYYY-MM-dd HH:MM:ss 字符串类型
		assertNotNull(now(DateTools.commonDateFormat)); // 返回当前时间，并对当前时间进行格式化
	}

	@Test
	public void testFormat() {
		Date date = Objet2Date("2017-07-25 11:16:09");
		
		assertEquals(formatDate(date), formatDate(date, DateTools.commonDateFormat)); // 对输入的时间进行格式化，采用格式 yyyy-MM-dd HH:mm:ss
		assertEquals(date.toString(), "Tue Jul 25 11:16:09 CST 2017");

		assertEquals(Objet2Date(date).getTime(), date.getTime());
		assertEquals(Objet2Date(date.getTime()).getTime(), date.getTime());
		assertEquals(Objet2Date("2017-07-25 11:16:09").getTime(), date.getTime()); // 转换字符串类型的日期到 Date 类型
	}
}
