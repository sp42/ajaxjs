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
package com.ajaxjs.json;

/**
 * Js 里面的类型转换到 Java 的类型
 * @author frank
 *
 */
public interface ToJsType {
	/**
	 * 序列化 JSON
	 * 
	 * @param code
	 *            JS 代碼
	 * @return JSON
	 */
	String JSON_Stringify(String code);
	
	/**
	 * 借助 Rhino 序列化
	 * 
	 * @param obj
	 *            NativeArray | NativeObject 均可
	 * @return JSON 字符串
	 */
	String JSON_Stringify(Object obj);
}
