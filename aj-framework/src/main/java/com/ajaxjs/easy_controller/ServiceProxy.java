package com.ajaxjs.easy_controller;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;

/**
 * 动态代理
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 * @param <T>
 */
public class ServiceProxy<T> implements InvocationHandler {
	private Class<T> interfaceType;

	private ApplicationContext applicationContext;

	public ServiceProxy(Class<T> interfaceType, ApplicationContext applicationContext) {
		this.interfaceType = interfaceType;
		this.applicationContext = applicationContext;
	}

	/**
	 * 操作的说明，保存于此
	 */
	public static ThreadLocal<String> ACTION_COMMNET = new ThreadLocal<>();

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//		System.out.println("调用前，args = " + args);
		ControllerMethod annotation = method.getAnnotation(ControllerMethod.class);

		if (annotation != null) {
			Class<?> serviceClass = annotation.serviceClass();

			if (serviceClass.equals(Object.class)) {
				// 读取类上的配置
				InterfaceBasedController clzAnn = interfaceType.getAnnotation(InterfaceBasedController.class);
				serviceClass = clzAnn.serviceClass();
			}

			Object serviceBean = applicationContext.getBean(serviceClass);
			String serviceMethod = annotation.methodName();

			if ("".equals(serviceMethod))
				serviceMethod = method.getName(); // 如果没设置方法，则与控制器的一致

			String comment = annotation.value(); // 处理说明
			if (!"".equals(serviceMethod)) {
				ACTION_COMMNET.remove();
				ACTION_COMMNET.set(comment);
			}

			Method beanMethod = ReflectionUtils.findMethod(serviceBean.getClass(), serviceMethod, method.getParameterTypes());
			
			if (beanMethod == null)
				throw new NullPointerException("是否绑定 Service 类错误？找不到" + serviceBean.getClass() + "目标方法");
			
			beanMethod.setAccessible(true);
			Object result = ReflectionUtils.invokeMethod(beanMethod, serviceBean, args);
//			System.out.println("调用后，result = " + result);

			return result;
		} else
			return ReflectionUtils.invokeMethod(method, this, args);
	}

}
