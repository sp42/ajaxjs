package com.ajaxjs.user.sign_perday;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;
import static com.ajaxjs.user.sign_perday.SignUtil.*;

public class TestUserUtil {
	@Test
	public void testGetCurrentWeek() {
		assertEquals(12, getWEEK_OF_YEAR());
	}

	@Test
	public void testGetDaysAgo() {
		System.out.println(Arrays.toString(getDaysAgo(2)));
		assertEquals(7, getDaysAgo().length);
	}

	@Test
	public void testGetWeeksByDate() {
		Integer[] weeks = getWeeksByDate(10);
		System.out.println(Arrays.toString(weeks));
		assertEquals(3, weeks.length);
	}

}
