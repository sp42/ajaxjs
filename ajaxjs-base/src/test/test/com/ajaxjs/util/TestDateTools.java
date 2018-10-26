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
		
		assertEquals("Tue Jul 25 11:16:09 GMT+08:00 2017", date.toString());
		assertEquals(formatDate(date), formatDate(date, DateTools.commonDateFormat)); // 对输入的时间进行格式化，采用格式 yyyy-MM-dd HH:mm:ss
		assertEquals("2017-07-25 11:16", formatDateShorter(date));
		assertEquals("2017-07-25",       formatDateShortest(date)); 

		assertEquals(date.getTime(), Objet2Date(date).getTime());
		assertEquals(date.getTime(), Objet2Date(date.getTime()).getTime());
		assertEquals(date.getTime(), Objet2Date("2017-07-25 11:16:09").getTime()); // 转换字符串类型的日期到 Date 类型
	}
}
