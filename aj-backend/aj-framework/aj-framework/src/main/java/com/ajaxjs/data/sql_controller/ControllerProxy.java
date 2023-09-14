package com.ajaxjs.data.sql_controller;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.IBaseModel;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.util.ObjectHelper;
import com.ajaxjs.util.reflect.Types;
import org.springframework.util.ReflectionUtils;

import com.ajaxjs.framework.spring.DiContextUtil;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通过动态代理执行控制器
 * 将控制器方法调用转发给服务方法，并处理控制器方法上定义的注解
 *
 * @author Frank Cheung sp42@qq.com
 */
public class ControllerProxy implements InvocationHandler {
    /**
     * 控制器接口类
     */
    private final Class<?> interfaceType;

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
    public static ThreadLocal<String> ACTION_COMMENT = new ThreadLocal<>();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> returnClz = method.getReturnType();

        if (returnClz == List.class) {
            Class<?> realReturnClz = Types.getGenericFirstReturnType(method);

            if (realReturnClz == Map.class) {

            } else {

            }
        } else if (returnClz == PageResult.class) {
            Class<?> realReturnClz = Types.getGenericFirstReturnType(method);

        } else if (returnClz == Map.class) {
            return ObjectHelper.hashMap("hi", 1);
        } else if (IBaseModel.class.isAssignableFrom(returnClz)) { // Java bean

        } else if (isSingleValue(returnClz)) {
            System.out.println("返回 single");

            // CRUD.queryOne

        } else { // Java bean

        }

        return null;
    }

    static boolean isSingleValue(Class<?> returnClz) {
        return returnClz == String.class || returnClz == Boolean.class || Number.class.isAssignableFrom(returnClz);
    }
}
