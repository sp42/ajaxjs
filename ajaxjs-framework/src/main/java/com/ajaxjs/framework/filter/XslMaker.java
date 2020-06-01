package com.ajaxjs.framework.filter;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Objects;

import com.ajaxjs.framework.config.ConfigService;
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
		if (request.hasParameter("downloadXSL"))
			if (request.hasParameter("allRows") || ConfigService.getInt("entity.exportXslPage") == 2) {
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
			String fileName = args.model.get("shortName") + "_" + LocalDate.now().toString();

			try {
				fileName = new String(fileName.getBytes(), "iso8859-1");
			} catch (UnsupportedEncodingException e) {
				LOGGER.warning(e);
			}

			xsl += ".jsp";
			LOGGER.info(fileName + "导出 Excel 中 " + xsl);

			// 拦截正常流程，自己控制 repsonse 输出
			args.response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			args.response.setHeader("Content-Disposition", String.format("attachment; filename=%s.xls", fileName));
			args.response.setTemplate(xsl);
			args.response.go(args.request);

			return false;
		}

		return true;
	}
}
