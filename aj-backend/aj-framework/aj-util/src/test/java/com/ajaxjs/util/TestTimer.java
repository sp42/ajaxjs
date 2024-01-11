package com.ajaxjs.util;

import org.junit.Test;

public class TestTimer {
	@Test
	public void testScheduledExecutor() throws InterruptedException {
		ObjectHelper.setTimeout(() -> {
			System.out.println("Hello");
		}, 5);

		// 原因是当开启新线程后，junit 在主线程运行后会关闭，子线程也就无法运行了
		// 主线程休眠
		Thread.sleep(100000);
	}
}
