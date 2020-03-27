package com.ajaxjs.framework.filter;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Objects;

import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.MvcOutput;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.FilterAction;
import com.ajaxjs.mvc.filter.FilterAfterArgs;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 输出 Excel XSL 格式文件
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class XslMaker implements FilterAction {
	private static final LogHelper LOGGER = LogHelper.getLog(XslMaker.class);

	@Override
	public boolean before(ModelAndView model, MvcRequest request, MvcOutput response, Method method, Object[] args) {
		if (request.hasParameter("downloadXSL") && request.hasParameter("allRows")) {
			// 通常是第一和第二的分页参数
			args[0] = 0;
			args[1] = 999999;
		}

		return true;
	}

	/**
	 * 用于输出 Excel XSL 格式文件的时候，说明模板是哪个文件名
	 */
	public static final String XSL_TEMPLATE_PATH = "XSL_TEMPLATE_PATH";

	@Override
	public boolean after(FilterAfterArgs args) {
		if (args.request.hasParameter("downloadXSL")) {
			String xsl = (String) args.model.get(XSL_TEMPLATE_PATH);
			Objects.requireNonNull(xsl, "必须在控制器里面设置 XSL_TEMPLATE_PATH 模板路径");
			String fileName = "xsl-" + LocalDate.now().toString();

			try {
				fileName = new String(fileName.getBytes(), "iso8859-1");
			} catch (UnsupportedEncodingException e) {
				LOGGER.warning(e);
			}

			// <base href="<%=basePath%>">
			// String basePath = request.getScheme() + "://" + request.getServerName() + ":"
			// + request.getServerPort() + request.getContextPath() + "/";
			// <%@ page pageEncoding="utf-8" contentType="application/msexcel"%>
			// response.setHeader("Content-disposition","inline; filename=videos.xls");
			// 以上这行设定传送到前端浏览器时的档名为test.xls 就是靠这一行，让前端浏览器以为接收到一个excel档

			args.response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			args.response.setHeader("Content-Disposition", String.format("attachment; filename=%s.xls", fileName));

			args.response.setTemplate(xsl);
			args.response.go(args.request);

			return false;
		}

		return true;
	}
}
