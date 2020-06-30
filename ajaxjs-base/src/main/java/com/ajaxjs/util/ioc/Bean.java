/**
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
package com.ajaxjs.util.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ajaxjs.ioc.aop.Aop;

/**
 * 表示可纳入在 IOC 里面管理的对象
 * 
 * @author sp42 frank@ajaxjs.com
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {
	/**
	 * 对象的标识，即 id
	 * 
	 * @return 对象的标识
	 */
	String value() default "";

	/**
	 * 关联 AOP 类
	 * 
	 * @return AOP 类列表
	 */
	@SuppressWarnings("rawtypes")
	Class<? extends Aop>[] aop() default {};
}