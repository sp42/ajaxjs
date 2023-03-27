package com.ajaxjs.spring.response;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ajaxjs.web.WebHelper;

/**
 * 后端表单、数据校验的异常捕获
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler {
	static final String TPL = "输入字段 [%s] 未通过校验，原因 [%s]，输入值为 [%s]，请检查后再提交。";

	@ExceptionHandler(value = BindException.class)
	public void exceptionHandler(HttpServletRequest req, HttpServletResponse resp, BindException e) {
		String msg = "";
		List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

		for (FieldError err : fieldErrors) {
			msg += String.format(TPL, err.getField(), err.getDefaultMessage(), err.getRejectedValue());
		}

		ResponseResult result = new ResponseResult();
		result.setErrorCode("400");
		result.setMessage(msg);

		WebHelper.outputJson(resp, result.toString());
	}
}
