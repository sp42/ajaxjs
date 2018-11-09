package test.com.ajaxjs.mvc.controller;

import javax.servlet.ServletException;

import org.junit.BeforeClass;

import com.ajaxjs.web.mock.BaseControllerTest;

/**
 * 方便测试的基础类
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public abstract class BaseTest extends BaseControllerTest {
	@BeforeClass
	public static void init() throws ServletException {
		init("test.com.ajaxjs.mvc.controller");
	}
}
