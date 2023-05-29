package com.ajaxjs.test;

import com.ajaxjs.framework.spring.BaseWebInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableWebMvc
public class AjFrameworkTestInit extends BaseWebInitializer {
	@ComponentScan({ "com.ajaxjs.test" })
	public static class ScanComponent {
	}


}
