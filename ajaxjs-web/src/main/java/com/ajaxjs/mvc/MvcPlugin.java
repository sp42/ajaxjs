package com.ajaxjs.mvc;

import java.util.function.Consumer;

import javax.servlet.ServletContext;

/**
 * MVC 框架的插件
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public abstract class MvcPlugin {
	private Consumer<ServletContext> onServletStartUp;

	public Consumer<ServletContext> getOnServletStartUp() {
		return onServletStartUp;
	}

	public void setOnServletStartUp(Consumer<ServletContext> onServletStartUp) {
		this.onServletStartUp = onServletStartUp;
	}
}
