/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com
 * 
 * 根据 2.0 版本 Apache 许可证("许可证")授权；
 * 根据本许可证，用户可以不使用此文件。
 * 用户可从下列网址获得许可证副本：
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
 */
package com.ajaxjs.framework.service.aop;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.bval.jsr.ApacheValidationProvider;

import com.ajaxjs.framework.service.IService;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.framework.service.ValidIt;
import com.ajaxjs.framework.service.ValidObj;
import com.ajaxjs.ioc.Aop;
import com.ajaxjs.util.logger.LogHelper;

public class ValidationService<T, ID extends Serializable, S extends IService<T, ID>> extends Aop<S> {
	private static final LogHelper LOGGER = LogHelper.getLog(ValidationService.class);


	@Override
	public Object before(S service, Method method, String methodName, Object[] args) throws Throwable {
		if (!method.isAnnotationPresent(ValidIt.class) || args.length == 0) // 检查该方法上是否有LogInf注解
			return null;

		T bean = getBean(method, args); // 获取参数中的那个 bean

		if (bean == null)
			return null;

		Validator v = getValidator();
		Set<ConstraintViolation<T>> results = v.validate(bean);

		if (!results.isEmpty()) {
			String str = "";
			for (ConstraintViolation<T> result : results) {
				str += result.getPropertyPath() + result.getMessage() + "\\n";
				LOGGER.info(result.getPropertyPath() + ":" + result.getMessage());
			}

			throw new ServiceException("校验失败。具体原因：" + str);
		}

		return null;
	}

	@Override
	public void after(S service, Method method, String methodName, Object[] args, Object returnObj) {

	}

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

	// BVal 与 JSR 接口结合，返回 ValidatorFactory 工厂
	private final static ValidatorFactory avf = Validation.byProvider(ApacheValidationProvider.class).configure().buildValidatorFactory();

	/**
	 * BVal 与 JSR 接口结合，返回 ValidatorFactory 工厂
	 * 
	 * @return 验证器
	 */
	public static Validator getValidator() {
		return avf.getValidator();
	}
}
