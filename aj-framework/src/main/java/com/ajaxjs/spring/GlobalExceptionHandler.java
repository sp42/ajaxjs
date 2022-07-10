package com.ajaxjs.spring;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.ajaxjs.spring.response.ResponseResult;
import com.ajaxjs.spring.response.ReturnCode;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;

/**
 * 全局异常拦截器
 *
 * @author Frank Cheung
 */
public class GlobalExceptionHandler implements HandlerExceptionResolver {
	private static final LogHelper LOGGER = LogHelper.getLog(GlobalExceptionHandler.class);

	/**
	 * 判断是否期望 JSON 的结果
	 *
	 * @return true 表示为希望是 JSON
	 */
	public static boolean isJson(HttpServletRequest request) {
		String accept = request.getHeader("Accept");
		return accept != null && "application/json".equals(accept);
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse resp, Object handler, Exception ex) {
		LOGGER.warning(ex);

		Throwable _ex = ex.getCause() != null ? ex.getCause() : ex;
		String msg = _ex.getMessage();

		if (msg == null)
			msg = _ex.toString();

		resp.setCharacterEncoding("UTF-8"); // 避免乱码
		resp.setHeader("Cache-Control", "no-cache, must-revalidate");

		if (_ex instanceof SecurityException || _ex instanceof IllegalAccessError)// 设置状态码
			resp.setStatus(HttpStatus.UNAUTHORIZED.value());
		else
			resp.setStatus(HttpStatus.OK.value());

		if (req.getAttribute("SHOW_HTML_ERR") != null && true == ((boolean) req.getAttribute("SHOW_HTML_ERR"))) {
			try {
				resp.getWriter().write(msg);
			} catch (IOException e) {
				LOGGER.warning(e);
			}

			return new ModelAndView();
		} else {
			msg = JsonHelper.javaValue2jsonValue(JsonHelper.jsonString_covernt(msg));
			resp.setContentType(MediaType.APPLICATION_JSON_VALUE); // 设置 ContentType

			ResponseResult resultWarpper = new ResponseResult();

			if (_ex instanceof ICustomException) {
				int errCode = ((ICustomException) _ex).getErrCode();
				resultWarpper.setErrorCode(errCode + "");
				resp.setStatus(errCode);
			} else {
				resultWarpper.setErrorCode(ReturnCode.RC500.getCode() + "");
				resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			}

			resultWarpper.setMessage(msg);

			try {
				resp.getWriter().write(resultWarpper.toString());
			} catch (IOException e) {
				LOGGER.warning(e);
			}

			return new ModelAndView();
		}

//        return null;// 默认视图，跳转 jsp
	}
}