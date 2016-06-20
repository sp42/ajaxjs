/**
 * Copyright 2015 Frank Cheung
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
package com.ajaxjs.framework.model;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * POJO 实体文档说明
 * 
 * @author frank
 *
 */
@Target(value = { TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityDescription {
	/**
	 * 标记实体文档说明
	 * 
	 * @return 字段说明
	 */
	public String doc() default "";

	/**
	 * 其他部分的文档，指定一个 jsp 路径。
	 * 
	 * @return
	 */
	public String extraHTML_path() default "";
}
