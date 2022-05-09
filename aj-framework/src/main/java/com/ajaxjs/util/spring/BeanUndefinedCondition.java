package com.ajaxjs.util.spring;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * 如何在普通Spring项目中手动实现类似Spring Boot中有条件生成Bean？
 * 
 * 如果指定的Bean全部不存在，才注入某个Bean
 * 
 * @author zifangsky
 * @date 2018/12/5
 * @since 1.0.0
 */
public class BeanUndefinedCondition implements Condition {
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		Map<String, Object> map = metadata.getAnnotationAttributes(ConditionalOnBeanUndefined.class.getName());
		ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
		Class<?>[] clazzArray = (Class<?>[]) map.get("value");
		@SuppressWarnings("unused")
		Object obj = null;

		if (clazzArray != null && clazzArray.length > 0) {
			int count = 0;
			
			for (Class<?> clazz : clazzArray) {
				try {
					// 如果目标 Bean 不存在，则会抛出异常
					obj = beanFactory.getBean(clazz);
				} catch (Exception e) {
					// 计数
					count++;
				}

			}

			// 如果指定的 Bean 全部不存在，则返回 true
			return count == clazzArray.length;
		}

		return false;
	}
}