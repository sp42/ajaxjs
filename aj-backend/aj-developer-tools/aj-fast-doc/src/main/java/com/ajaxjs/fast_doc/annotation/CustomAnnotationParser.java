package com.ajaxjs.fast_doc.annotation;

import com.ajaxjs.fast_doc.model.ArgInfo;
import com.ajaxjs.fast_doc.model.Item;
import com.ajaxjs.fast_doc.model.Return;
import com.ajaxjs.framework.spring.easy_controller.anno.ControllerMethod;
import com.ajaxjs.framework.spring.easy_controller.anno.Example;
import com.ajaxjs.framework.spring.easy_controller.anno.PathForDoc;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 自定义注解的提取器
 *
 * @author Frank Cheung sp42@qq.com
 */
public class CustomAnnotationParser extends SpringMvcAnnotationParser {
    public CustomAnnotationParser() {

    }

    public CustomAnnotationParser(Class<?> clz) {
        super(clz);
    }

    @Override
    String getRootUrlIfRequestMappingNull() {
        PathForDoc pd = getClz().getAnnotation(PathForDoc.class);

        return pd == null ? null : pd.value();
    }

    @Override
    public void getMethodInfo(Item item, Method method) {
        ControllerMethod cm = method.getAnnotation(ControllerMethod.class);

        if (cm != null) { // 方法的一些信息，读取注解
            item.name = cm.value();
            item.description = cm.description();
            item.image = cm.image();
        }
    }

    @Override
    void getReturnType(Item item, Method method, Return r) {
        Example eg = method.getAnnotation(Example.class);
        if (eg != null)
            r.example = eg.value();
    }

    @Override
    public void getArgs(Item item, Method method, Parameter param, ArgInfo arg) {
        Example eg = param.getAnnotation(Example.class);
        if (eg != null)
            arg.example = eg.value();
    }

}
