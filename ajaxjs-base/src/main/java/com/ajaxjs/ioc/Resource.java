/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.ioc;

import static java.lang.annotation.ElementType.*;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示可注入的资源
 * 
 * @author Sp42 frank@ajaxjs.com
 */
@Target({ FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Resource {
	/**
	 * 依赖进来对象的标识，即 id
	 * 
	 * @return 对象的标识
	 */
	String value() default "";

	/**
	 * 是否创建新实例
	 * 
	 * @return 是否创建新实例
	 */
	boolean isNewInstance() default false;
}