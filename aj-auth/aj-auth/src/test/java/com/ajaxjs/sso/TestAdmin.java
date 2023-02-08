package com.ajaxjs.sso;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ajaxjs.auth.controller.DataServiceApiController;
import com.ajaxjs.sso.model.ClientDetails;

@ContextConfiguration(locations = { "classpath*:applicationContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestAdmin {
	@Autowired
	DataServiceApiController apiController;

	MockMvc mockMvc;

	@Autowired
	WebApplicationContext wac;

	@Before
	public void init() {
//		apiController.init();
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void testCreateClient() throws Exception {
		ClientDetails client = new ClientDetails();
		client.setName("测试客户端");

//		assertEquals("{\"isOk\": true, \"msg\" : \"操作成功！\"}", new OauthController().clientRegister(client));
	}
}
