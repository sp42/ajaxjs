package com.ajaxjs.spring.easy_controller;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.springframework.util.ReflectionUtils;

import com.ajaxjs.spring.DiContextUtil;

/**
 * 通过动态代理执行控制器
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class ControllerProxy implements InvocationHandler {
	/**
	 * 控制器接口类
	 */
	private Class<?> interfaceType;

	/**
	 * 创建一个 ServiceProxy
	 * 
	 * @param interfaceType 控制器接口类
	 */
	public ControllerProxy(Class<?> interfaceType) {
		this.interfaceType = interfaceType;
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

//			Object serviceBean = ctx.getBean(serviceClass);
			Object serviceBean = DiContextUtil.getBean(serviceClass);
			if (serviceBean == null)
				throw new NullPointerException("Spring IoC 中找不到对应的 Bean：" + serviceClass + "，是否已经加入 Bean 扫描，或者添加 @Service 的注解？");

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
		} else {
//			System.err.println("接口方法 " + method + " 没配置 ControllerMethod 注解");
			return ReflectionUtils.invokeMethod(method, this, args);
		}
	}
}
