package com.ajaxjs.user;

import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.framework.MockTest;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.user.controller.VerifyEmail.VerifyEmailService;
import com.ajaxjs.user.token.ForgetPassword;

public class TestVerifyEmail {
	static VerifyEmailService service;

	@BeforeClass
	public static void initDb() {
		MockTest.loadSQLiteTest("C:\\project\\test-cms\\WebContent\\META-INF\\database.sqlite");
		service = new VerifyEmailService();
	}

	@AfterClass
	public static void closeDb() {
		JdbcConnection.closeDb();
	}

//	@Test
	public void testSaveToken() {
//		String token = VerifyEmailService.saveToken("frank@ajaxjs.com", 555745146268811260L);
		boolean is = VerifyEmailService.verifyCallBack("20dd961206dacd3a3a8bef01fcc95be9");
		assertTrue(is);
	}
	
	@Test
	public void testForgetPsw() {
		ForgetPassword.findByEmail("http://qq.com/sds", "frank@ajaxjs.com");
	}
}
