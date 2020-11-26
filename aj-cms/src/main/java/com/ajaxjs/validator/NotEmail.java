package com.ajaxjs.validator;

import java.lang.annotation.*;

@Documented
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmail {

    public boolean email() default true;

    public String message() default "请注意规范";


}
