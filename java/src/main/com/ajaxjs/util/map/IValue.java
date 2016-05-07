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
package com.ajaxjs.util.map;

public interface IValue {
	/**
	 * 保存最开始输入的类型
	 * @param rawVlaue
	 */
	void setRawValue(Object rawVlaue);
	
	/**
	 * 返回最开始输入的类型
	 * @return
	 */
	Object getRawValue();
	
	/**
	 * 尝试转换到可以变化的真实类型
	 * @param clz
	 * @return
	 */
	<T> T valueOf(Class<T> clz);
	
	/**
	 * 返回可以变化的真实类型
	 * @return
	 */
	Object getValueOf();

	/**
	 * 转换到 JDBC 类型
	 * @return
	 */
	String toSqlType();
	
	
	String getSqlType();
	
	/**
	 * 转换到 JSON 类型
	 * @return
	 */
	String toJSONType();
}
