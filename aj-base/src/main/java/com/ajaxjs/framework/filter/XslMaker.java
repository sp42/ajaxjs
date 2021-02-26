package com.ajaxjs.framework.filter;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Objects;

import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.mvc.filter.FilterAction;
import com.ajaxjs.web.mvc.filter.FilterAfterArgs;
import com.ajaxjs.web.mvc.filter.FilterContext;

/**
 * 输出 Excel XSL 格式文件
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class XslMaker implements FilterAction {
	private static final LogHelper LOGGER = LogHelper.getLog(XslMaker.class);

	@Override
	public boolean before(FilterContext ctx) {
		if (ctx.request.hasParameter("downloadXSL"))
			if (ctx.request.hasParameter("allRows") || ConfigService.getInt("entity.exportXslPage") == 2) {
				// 通常是第一和第二的分页参数
				ctx.args[0] = 0;
				ctx.args[1] = 999999;
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