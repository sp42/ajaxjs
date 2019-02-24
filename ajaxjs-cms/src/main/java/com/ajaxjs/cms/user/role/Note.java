package com.ajaxjs.cms.user.role;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Note {
	public String value();

	public boolean allowCRUD() default false;
}
