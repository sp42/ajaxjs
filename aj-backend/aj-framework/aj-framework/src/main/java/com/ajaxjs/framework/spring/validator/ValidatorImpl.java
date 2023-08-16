package com.ajaxjs.framework.spring.validator;


import com.ajaxjs.framework.spring.validator.model.ValidatorEnum;
import com.ajaxjs.framework.spring.validator.model.ValidatorException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author volicy.xu
 */
public class ValidatorImpl implements Validator {
    /**
     * 用于获取配置
     */
    private final ApplicationContext cxt;

    public ValidatorImpl(ApplicationContext cxt) {
        this.cxt = cxt;
    }

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
                    resolveAnnotations(field.getDeclaredAnnotations(), field.get(target));
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

    public void resolveAnnotations(Annotation[] annotations, Object value) {
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            String name = annotationType.getName();

            if (name.contains(DEFAULT_PACKAGE) || name.contains(AJ_PACKAGE)) {
//                Object required = AnnotationUtils.getValue(annotation, "required");
//
//                if (required != null && (boolean) required) {
                ValidatorEnum validConstant = ValidatorEnum.getInstance(annotationType.getSimpleName());

                if (validConstant != null) {
                    String message = getValue(annotation);

                    if (!StringUtils.hasText(message))
                        throw new ValidatorException("Correctly configure annotation message property");

                    validConstant.validated(value, message);
                } else
                    throw new ValidatorException("Correctly configure easyvalidator annotation");
//                }
            }
        }
    }

    private String getValue(Annotation annotation) {
        String message = (String) AnnotationUtils.getValue(annotation, "message");
        assert message != null;

        if (message.indexOf('{') > -1) {
            String value = cxt.getEnvironment().getProperty("spring.org.easyvalidator.name");

            if (value == null) {
                AbstractEnvironment env = (AbstractEnvironment) cxt.getEnvironment();
                EncodedResource encodedResource = new EncodedResource(new ClassPathResource("classpath:ValidationMessages.properties"), "utf-8");

                try {
                    env.getPropertySources().addFirst(new ResourcePropertySource("spring.org.easyvalidator.name", encodedResource));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return env.resolveRequiredPlaceholders(message);
            } else
                return null;
        }

        return message;
    }

}
