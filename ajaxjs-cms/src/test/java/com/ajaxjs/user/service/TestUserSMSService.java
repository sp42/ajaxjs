package com.ajaxjs.user.service;

import static org.junit.Assert.*;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import com.ajaxjs.user.service.UserSMSService;

public class TestUserSMSService {
	@Test
	public void testCanSend() {
		assertTrue(UserSMSService.canSend("137"));
		assertFalse(UserSMSService.canSend("137"));
		
		final CountDownLatch c = new CountDownLatch(1);
		
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				assertTrue(UserSMSService.canSend("137"));
				c.countDown();
			}
		}, 4000);
		
		try {
			c.await(); // 阻塞 junit
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
