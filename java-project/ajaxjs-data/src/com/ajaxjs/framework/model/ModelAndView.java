package com.ajaxjs.framework.model;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.bval.jsr.ApacheValidationProvider;

public class ModelAndView extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	/**
	 * 保存到 request
	 * 
	 * @param request
	 */
	public void saveToReuqest(HttpServletRequest request) {
		for (String key : keySet()) {
			request.setAttribute(key, get(key));
		}
	}
	
	// BVal 与 JSR 接口结合，返回 ValidatorFactory 工厂
	private final static ValidatorFactory avf = Validation.byProvider(ApacheValidationProvider.class).configure()
			.buildValidatorFactory();

	public static Validator getValidator() {
		return avf.getValidator();
	}

}
