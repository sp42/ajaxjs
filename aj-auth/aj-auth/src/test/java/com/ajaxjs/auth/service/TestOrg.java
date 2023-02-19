package com.ajaxjs.auth.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ajaxjs.TestConfig;
import com.ajaxjs.auth.service.user.OrgService;

@ContextConfiguration(classes = TestConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestOrg {

	@Autowired
	private OrgService orgService;

	@Test
	public void testDel() {
		assertNotNull(orgService);

		assertTrue(orgService.delete(1l));
	}
}
