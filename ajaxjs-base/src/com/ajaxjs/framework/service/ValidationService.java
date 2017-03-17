package com.ajaxjs.framework.service;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.ajaxjs.framework.dao.IDao;
import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.framework.service.annotation.ValidIt;
import com.ajaxjs.framework.service.annotation.ValidObj;
import com.ajaxjs.util.aop.Aop;

public class ValidationService<T, ID extends Serializable, D extends IDao<T, ID>> extends Aop<IService<T, ID>> {
	@Override
	protected Object before(Method method, Object[] args) throws Throwable {
		if (!method.isAnnotationPresent(ValidIt.class) || args.length == 0) // 检查该方法上是否有LogInf注解
			return null;
		
		T bean = getBean(method, args); // 获取参数中的那个 bean

		if (bean == null)
			return null;

		Validator v = ModelAndView.getValidator();
		Set<ConstraintViolation<T>> results = v.validate(bean);

		if (!results.isEmpty()) {
			String str = "";
			for (ConstraintViolation<T> result : results) {
				str += result.getPropertyPath() + result.getMessage() + "\\n";
				// LOGGER.info(result.getPropertyPath() + ":" + result.getMessage());
			}

			throw new ServiceException("校验失败。具体原因：" + str);
		}

		return null;
	}
	
	/**
	 * 
	 * @param method
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private T getBean(Method method, Object[] args) {
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();// 获得参数注释
		
		for (int i = 0; i < parameterAnnotations.length; i++) {
			Annotation[] pa = parameterAnnotations[i];// 每个参数上所有的注解
			
			for (Annotation a : pa) {
				if (a.annotationType() == ValidObj.class) 
					return (T) args[i]; // 定位 bean
			}
		} 
		
		return null;
	}

	@Override
	protected void after(Method method, Object[] args, Object returnObj) {
	}
}
