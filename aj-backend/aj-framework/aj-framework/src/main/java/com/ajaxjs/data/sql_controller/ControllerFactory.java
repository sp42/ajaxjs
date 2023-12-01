package com.ajaxjs.data.sql_controller;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Proxy;

/**
 * 接口实例工厂，这里主要是用于提供接口的实例对象
 *
 * @author Frank Cheung sp42@qq.com
 */
public class ControllerFactory implements FactoryBean<Object> {
    /**
     * 控制器接口类
     */
    private final Class<RestController> interfaceType;

    /**
     * 创建一个 ControllerFactory
     *
     * @param interfaceType 控制器接口类
     */
    public ControllerFactory(Class<RestController> interfaceType) {
        this.interfaceType = interfaceType;
    }

    @Override
    public Object getObject() {
        // 这里主要是创建接口对应的实例，便于注入到 spring 容器中
        return Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[]{interfaceType}, new ControllerProxy(interfaceType));
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceType;
    }
}
