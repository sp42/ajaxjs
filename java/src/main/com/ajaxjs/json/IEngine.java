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

import java.io.IOException;
import java.util.Map;

import javax.script.ScriptException;

/**
 * 抽象的 JS 引擎
 * @author Frank Cheung
 *
 */
public interface IEngine {
	/**
	 * 加载 JS 文件
	 * 
	 * @param paths
	 *            多个 JS 文件
	 */
	public void load(String[] paths);

	/**
	 * 加载单个 js 文件
	 * 
	 * @param path
	 *            JS 文件完整路徑
	 * @throws ScriptException
	 * @throws IOException
	 */
	public void load(String path);
	
	/**
	 * 加载 js 文件 从类相同目录的地方加载 js 文件。
	 * 
	 * @param cls
	 *            类所在
	 * @param js
	 *            JS 文件名
	 */
	public void load(Class<?> cls, String js);
	
	/**
	 * 运算输入的 JS 代码，该方法封装了异常，不抛出。输入 class 进行类型转换
	 * 
	 * @param code
	 *            JS 脚本代码
	 * @param clazz
	 *            目标类型
	 * @return JS 运算后的返回值，字符串类型的值，也可能是 null 没有返回
	 */
	public <T> T eval(String code, Class<T> clazz);
	
	/**
	 * 运算输入的 JS 代码，该方法封装了异常，不抛出
	 * 
	 * @param code
	 *            JS 脚本代码
	 * @return JS 运算后的返回值，也可能是 null 没有返回
	 */
	public Object eval(String code);
	
	/**
	 * 如果需要返回 {a:xx, b:xx} JSON 这样 JSON 的结构，要把这个 JSON 赋予一个全局变量
	 * 
	 * @param code
	 *            JS 脚本代码
	 * @return
	 */
	public Map<String, Object> eval_return_Map(String code);
	
	/**
	 * 调用脚本的方法
	 * 
	 * @param method
	 *            js 脚本代码
	 * @param clazz
	 *            目标类型
	 * @param binding
	 *            可以为 null，则表示调用全局方法
	 * @param args
	 *            参数列表
	 * @return JS 运算后的返回值，也可能是 null 没有返回
	 */
	public <T> T call(String method, Class<T> clazz, Object binding, Object... args);
	
	/**
	 * 写变量 在 Java 中向脚本引擎 (Script Engine) 传递变量，即脚本语言用 java 的变量。当然，使用 eval()
	 * 也可以了。注意可以直接赋值 Java 对象。
	 * 
	 * @param varName
	 *            变量名
	 * @param obj
	 *            变量值
	 */
	public void put(String varName, Object obj);
	
	/**
	 * 获取 js 的对象，如果最后一个不是对象，返回 Object，之前的为 NativeObject
	 * 
	 * @param namespace
	 *            JS MAP 对象的 key
	 * @return NativeObject 或 Object
	 */
	public Object get(String... namespace);
	
}
