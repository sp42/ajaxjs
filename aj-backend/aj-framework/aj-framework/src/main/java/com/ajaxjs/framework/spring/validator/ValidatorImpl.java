package com.ajaxjs.framework.spring.validator;


import com.ajaxjs.framework.spring.CustomPropertySources;
import com.ajaxjs.framework.spring.DiContextUtil;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author volicy.xu
 */
public class ValidatorImpl implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Field[] declaredFields = target.getClass().getDeclaredFields();

        try {
            for (Field field : declaredFields) {
                if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())) {// isPrivate
                    field.setAccessible(true);
                    resolveAnnotations(field.getDeclaredAnnotations(), field.get(target), field.getName());
                }
            }
        } catch (Exception e) {
            if (e instanceof ValidatorException)
                throw (ValidatorException) e;

            throw new ValidatorException(e);
        }
    }

    private static final String DEFAULT_PACKAGE = "javax.validation.constraints";
    private static final String AJ_PACKAGE = "com.ajaxjs.framework.spring.validator";

    public void resolveAnnotations(Annotation[] annotations, Object value, String fieldName) {
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            String name = annotationType.getName();

            if (name.contains(DEFAULT_PACKAGE) || name.contains(AJ_PACKAGE)) {
                ValidatorEnum validConstant = ValidatorEnum.getInstance(annotationType.getSimpleName());

                if (validConstant != null) {
                    String message = getValue(annotation);

                    if (!StringUtils.hasText(message))
                        throw new ValidatorException("Correctly configure annotation message property");

                    validConstant.validated(value, fieldName + " " + message);
                } else
                    throw new ValidatorException("Correctly configure easy validator annotation");
            }
        }
    }

    /**
     * 从注解上获取错误信息，如果没有则从默认的 YAML 配置获取
     */
    private String getValue(Annotation annotation) {
        String message = (String) AnnotationUtils.getValue(annotation, "message");
        assert message != null;

        if (message.indexOf('{') > -1) { // 注解上没设置 message，要读取配置
            CustomPropertySources bean = DiContextUtil.getBean(CustomPropertySources.class);
            assert bean != null;
            String key = "javax-validation." + message.replaceAll("^\\{|}$", "");
            Object o = bean.getLocalProperties().get(key);

            if (o != null)
                message = o.toString();
        }

        return message;
    }

}
