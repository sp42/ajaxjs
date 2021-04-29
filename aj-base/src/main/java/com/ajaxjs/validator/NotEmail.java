package com.ajaxjs.validator;

import java.lang.annotation.*;

@Documented
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmail {
	/**
	 * 
	 * @return
	 */
    public boolean email() default true;

    /**
     * 
     * @return
     */
    public String message() default "请注意规范";
}
