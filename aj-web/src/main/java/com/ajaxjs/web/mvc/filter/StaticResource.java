package com.ajaxjs.web.mvc.filter;

import java.util.function.BiFunction;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.framework.Application;
import com.ajaxjs.framework.IComponent;

/**
 * 检查是否静态资源。Check the url if there is static asset.
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class StaticResource implements IComponent {
	/**
	 * 字符串判断是否静态文件
	 */
	private static final Pattern p = Pattern.compile("\\.jpg|\\.png|\\.gif|\\.js|\\.css|\\.less|\\.ico|\\.jpeg|\\.htm|\\.swf|\\.txt|\\.mp4|\\.flv");

	private static final BiFunction<HttpServletRequest, FilterChain, Boolean> filter = (req, chain) -> p.matcher(req.getRequestURI()).find();

	static {
		Application.onRequest2.add(0, filter);
	}
}
