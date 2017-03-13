package com.ajaxjs.framework.service;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.ajaxjs.framework.dao.IDao;
import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.util.aop.Aop;

public class ValidationService <T, ID extends Serializable, D extends IDao<T, ID>> extends Aop<IService<T, ID>> {

	@Override
	protected Object before(Method method, Object[] args) {

//		if (result.hasErrors()) {
//		List<ObjectError> errors = result.getAllErrors();
//	
//		String str = "";
//		for(ObjectError err : errors) {
//			str += err.getCodes()[0] + err.getObjectName() + "\\n";
//			LOGGER.info(err.getCode() + ":" + err.getObjectName() + err.getDefaultMessage());
//		}
//		
//		model.put("errMsg", str);
//	}

		
		Validator v = ModelAndView.getValidator();
		Set<ConstraintViolation<T>> results = v.validate(entity);

		if (!results.isEmpty()) {
			String str = "";
			for (ConstraintViolation<T> result : results) {
				str += result.getPropertyPath() + result.getMessage() + "\\n";
				//LOGGER.info(result.getPropertyPath() + ":" + result.getMessage());
			}
		}	
		
		return null;
	}

	@Override
	protected void after(Method method, Object[] args, Object returnObj) {
		
	}

}
