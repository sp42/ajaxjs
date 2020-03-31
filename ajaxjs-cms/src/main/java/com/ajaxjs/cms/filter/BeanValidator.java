package com.ajaxjs.cms.filter;

import java.lang.reflect.Method;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.bval.jsr.ApacheValidationProvider;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.MvcOutput;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.FilterAction;
import com.ajaxjs.mvc.filter.FilterAfterArgs;

/**
 * 后台 Bean 的数据校验 https://blog.csdn.net/zhangxin09/article/details/50600575
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class BeanValidator implements FilterAction {
	// BVal 与 JSR 接口结合，返回 ValidatorFactory 工厂
	public static ValidatorFactory AVF = Validation.byProvider(ApacheValidationProvider.class).configure()
			.buildValidatorFactory();

	@Override
	public boolean before(ModelAndView model, MvcRequest request, MvcOutput response, Method method, Object[] args) {
		for (Object obj : args) {
			if (obj instanceof BaseModel) {
				Validator v = AVF.getValidator();
				Set<ConstraintViolation<Object>> result = v.validate(obj);

				if (!result.isEmpty()) { // 有错误
					String msg = "";

					for (ConstraintViolation<Object> r : result) {
						// 哪个字段错？ 什么错？
						msg += "提交的字段错误 " + r.getPropertyPath() + ":" + r.getMessage();
					}

					model.put(NOT_LOG_EXCEPTION, true);
					throw new IllegalArgumentException(msg);
				}
			}
		}

		return true;
	}

	@Override
	public boolean after(FilterAfterArgs argsHolder) {
		return true;
	}

}
