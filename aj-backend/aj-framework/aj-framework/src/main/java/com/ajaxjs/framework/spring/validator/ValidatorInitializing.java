package com.ajaxjs.framework.spring.validator;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 在 Spring 应用程序上下文初始化完成后设置验证器和参数解析器
 * 这个类的作用是在 Spring MVC 启动时，拦截并修改 RequestMappingHandlerAdapter 的行为。通过设置自定义的验证器和参数解析器，可以对路径变量进行验证
 */
public class ValidatorInitializing implements ApplicationContextAware, InitializingBean {
    private ApplicationContext cxt;

    @Override
    public void setApplicationContext(ApplicationContext cxt) throws BeansException {
        this.cxt = cxt;
    }

    @Override
    public void afterPropertiesSet() {
        /*
            在 afterPropertiesSet 方法中，我们从应用程序上下文中获取 RequestMappingHandlerAdapter 对象。
            然后，我们将自定义的验证器 ValidatorImpl 设置为 ConfigurableWebBindingInitializer 对象的验证器。
            接着，我们获取到当前的参数解析器列表，并排除了 PathVariableMethodArgumentResolver 类型的解析器。
            然后，我们将自定义的 PathVariableArgumentValidatorResolver 解析器添加到解析器列表的开头。最后，将更新后的解析器列表设置回 RequestMappingHandlerAdapter 对象
         */
        RequestMappingHandlerAdapter adapter = cxt.getBean(RequestMappingHandlerAdapter.class);
        ConfigurableWebBindingInitializer init = (ConfigurableWebBindingInitializer) adapter.getWebBindingInitializer();

        assert init != null;
        init.setValidator(new ValidatorImpl());
        List<HandlerMethodArgumentResolver> resolvers = Objects.requireNonNull(adapter.getArgumentResolvers())
                .stream().filter(r -> !(r.getClass().equals(PathVariableMethodArgumentResolver.class)))
                .collect(Collectors.toList());

        // 路径变量时进行参数验证
        resolvers.add(0, new PathVariableMethodArgumentResolver() {
            @Override
            protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
                Object value = super.resolveName(name, parameter, request);
                // validateIfApplicable
                new ValidatorImpl().resolveAnnotations(parameter.getParameterAnnotations(), value);

                return value;
            }
        });

        adapter.setArgumentResolvers(resolvers);
    }
}
