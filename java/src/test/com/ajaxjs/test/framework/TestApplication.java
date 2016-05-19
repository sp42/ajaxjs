package com.ajaxjs.test.framework;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import java.io.IOException;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.framework.RhinoApplication;

public class TestApplication {
	private RhinoApplication app;
	private ServletContext sc;
	
	@Before
	public void setUp() throws Exception {
		try {
			MockRequest.initDBConnection(); // 创建数据库连接
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		sc = mock(ServletContext.class);
		// 指定类似 Tomcat 的虚拟目录，若设置为 "" 表示 Root 根目录
		when(sc.getContextPath()).thenReturn("/new_test"); 
		// 设置项目真实的目录，当前是 返回 一个特定的 目录，你可以不执行该步
		when(sc.getRealPath(anyString())).thenReturn("f:\\project\\new_test\\WebContent");
		// 设置 /META-INF 目录，当前使用该目录来保存 配置
		when(sc.getRealPath("/META-INF")).thenReturn("f:\\project\\new_test\\WebContent\\META-INF");
		
		app = new RhinoApplication(); 
		RhinoApplication.isDebug = false; // 模拟真实状态
	}
	
	@Test
	public void testContextInitialized() throws IOException, ServletException {
		ServletContextEvent sce = mock(ServletContextEvent.class);
		when(sce.getServletContext()).thenReturn(sc);
		app.contextInitialized(sce);
		assertNotNull(sce);
		assertTrue("App started OK!", RhinoApplication.isConfig_Loaded);
	}
}
