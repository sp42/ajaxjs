package com.ajaxjs.user.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.ioc.Aop;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.user.model.User;
import com.ajaxjs.user.model.UserCommonAuth;
import com.ajaxjs.user.service.UserConstant;
import com.ajaxjs.user.service.UserService;
import com.ajaxjs.user.service.UserServiceImpl;
import com.ajaxjs.util.ReflectUtil;

public class TestUserService {
	@BeforeClass
	public static void initDb() {
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T initAop(Object beanInstance, Class<T> clz) {
		Bean annotation = clz.getAnnotation(Bean.class);
		for (Class<? extends Aop> clz2 : annotation.aop()) {
			beanInstance = ReflectUtil.newInstance(clz2).bind(beanInstance);
		}

		return (T) beanInstance;
	}

	@BeforeClass
	public static void intiGetIp() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("x-forwarded-for")).thenReturn(null);
		when(request.getHeader("Proxy-Client-IP")).thenReturn("127.0.0.1");

		MvcRequest.setHttpServletRequest(request);
	}

	@Test
	public void testByUserName() {
		UserService service = new UserServiceImpl();
		service = initAop(service, service.getClass());

		User user = new User();
		user.setName("ster443");
		UserCommonAuth password = new UserCommonAuth();
		password.setPassword("foo");

		try {
			Long id = service.create(user, password); // 注册
			assertNotNull(id);
			
			UserCommonAuth userNameLoign = new UserCommonAuth();
			userNameLoign.setPassword("foo");
			userNameLoign.setLoginType(UserConstant.loginByUserName);
			assertTrue(service.loginByPassword(user, userNameLoign)); // 登录
			
			service.delete(user); // 删除测试数据
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testByPhoneNo() {
		UserService service = new UserServiceImpl();
		service = initAop(service, service.getClass());
		
		User user = new User();
		user.setPhone("13352899283");
		UserCommonAuth password = new UserCommonAuth();
		password.setPassword("bar");
		
		try {
			Long id = service.create(user, password); // 注册
			assertNotNull(id);
			
			UserCommonAuth phoneLoign = new UserCommonAuth();
			phoneLoign.setPassword("bar");
			phoneLoign.setLoginType(UserConstant.loginByPhoneNumber);
			assertTrue(service.loginByPassword(user, phoneLoign)); // 登录		
			
			service.delete(user);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}



//	@Test
	public void testResetPasswordByEmail() {
		User user = new User();
		user.setEmail("Honorato@vitae.net");
		
		UserService service = new UserServiceImpl();
		service = initAop(service, service.getClass());
		
		try {
			String token = service.resetPasswordByEmail(user);
			assertNotNull(token);
			assertTrue(service.validResetPasswordByEmail(token, "Honorato@vitae.net"));
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
	}

	@AfterClass
	public static void closeDb() {
		try {
			JdbcConnection.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		JdbcConnection.clean();
	}
}
