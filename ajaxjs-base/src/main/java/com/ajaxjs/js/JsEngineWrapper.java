/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com
 * 
 * 根据 2.0 版本 Apache 许可证("许可证")授权；
 * 根据本许可证，用户可以不使用此文件。
 * 用户可从下列网址获得许可证副本：
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
 */
package com.ajaxjs.js;

import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.ajaxjs.keyvalue.MappingValue;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.io.FileUtil;
import com.ajaxjs.util.io.StreamUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * JS 引擎的包装器
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class JsEngineWrapper {
	private static final LogHelper LOGGER = LogHelper.getLog(JsEngineWrapper.class);

	public JsEngineWrapper() {
		this(engineFactory());
	}

	/**
	 * 必须依赖一个原生 engine 对象
	 * 
	 * @param engine JVM 脚本引擎
	 */
	public JsEngineWrapper(ScriptEngine engine) {
		this.engine = engine;
	}

	/**
	 * js 引擎
	 */
	private ScriptEngine engine;

	public ScriptEngine getEngine() {
		return engine;
	}

	public void setEngine(ScriptEngine engine) {
		this.engine = engine;
	}

	/**
	 * 创建 js 引擎工厂，支持 java 6/7 的 rhino 和 java 8 的 nashorn
	 * 
	 * @return js 引擎
	 */
	public static ScriptEngine engineFactory() {
		return new ScriptEngineManager().getEngineByName(System.getProperty("java.version").contains("1.8.") ? "nashorn" : "rhino");
	}

	/**
	 * 加载 js 文件，可以链式调用这个方法加载多个 js 文件
	 * 
	 * @param path js 文件完整路徑
	 * @return 当前实例，可以链式调用这个方法加载多个 js 文件
	 */
	public JsEngineWrapper load(String path) {
		LOGGER.info("加载 js: {0} 文件", path);
		eval(FileUtil.openAsText(path));

		return this;
	}

	/**
	 * 加载某个类下面的 js 文件，可以链式调用这个方法加载多个 js 文件
	 * 
	 * @param clazz    该类目录下面必须有目标 js 文件
	 * @param fileName js 文件名
	 * @return 当前实例，可以链式调用这个方法加载多个 js 文件
	 */
	public JsEngineWrapper load(Class<?> clazz, String fileName) {
		String code = new StreamUtil().setIn(clazz.getResourceAsStream(fileName)).byteStream2stringStream().close().getContent();
		eval(code);

		return this;
	}

	/**
	 * 调用脚本的方法
	 * 
	 * @param method  js 脚本代码
	 * @param clazz   目标类型
	 * @param binding 可以为 null，则表示调用全局方法
	 * @param args    参数列表
	 * @return JS 运算后的返回值，也可能是 null 没有返回
	 */
	public <T> T call(String method, Class<T> clazz, Object binding, Object... args) {
		Invocable inv = (Invocable) engine; // Invocable 接口是 ScriptEngine可选实现的接口。（多态）
		Object result = null;

		try {
			result = binding != null ? inv.invokeMethod(binding, method, args) : inv.invokeFunction(method, args);
		} catch (NoSuchMethodException e) {
			LOGGER.warning(e, "脚本引擎没有 {0}() 这个方法", method);
		} catch (ScriptException e) {
			LOGGER.warning(e, "向脚本引擎调用脚本方法异常！方法名称:" + method);
		}

		return MappingValue.TypeConvert(result, clazz);
	}

	/**
	 * 在 Java 中向脚本引擎 (Script Engine) 传递变量，即脚本语言中可以得到来自 java 的变量。 当然，使用 eval()
	 * 也可达到同样之效果。另外亦可以直接传递 Java 对象。
	 * 
	 * @param varName 变量名
	 * @param obj     变量值
	 */
	public void put(String varName, Object obj) {
		engine.put(varName, obj);
	}

	/**
	 * 获取 js 的对象，如果最后一个不是对象，返回 Object，之前的为 NativeObject
	 * 
	 * @param namespace JS 对象的 key
	 * @return NativeObject 或 Object
	 */
	@SuppressWarnings("rawtypes")
	public Object get(String... namespace) {
		Map obj = (Map) engine.get(namespace[0]);

		for (int i = 1; i < namespace.length; i++) {
			try {
				obj = (Map) obj.get(namespace[i]);
			} catch (ClassCastException e) {
				return obj.get(namespace[i]);
			}
		}

		return obj;
	}

	/**
	 * 执行 js 代码
	 * 
	 * @param code  任意 js 代码
	 * @param clazz 返回的类型。当 clazz ＝ null 时永远返回 null，表示只是执行，不要求返回结果。可理解为 return
	 *              void。如果想有返回值，至少有个 clazz = Object.class
	 * @return 执行结果
	 */
	public <T> T eval(String code, Class<T> clazz) {
		if (CommonUtil.isEmptyString(code))
			throw new UnsupportedOperationException("JS 代码不能为空！");

		Object obj = null;

		try {
			obj = engine.eval(code);
		} catch (ScriptException e) {
			LOGGER.warning(e, "脚本 eval() 运算发生异常！eval 代码：" + code);
		}

		if (obj != null && clazz != null) {
			// return Util.TypeConvert(js.eval(code), clazz); // 为什么要执行多次？
			T _obj = MappingValue.TypeConvert(obj, clazz);
			return _obj;
		} else
			return null;
	}

	/**
	 * 执行 js 代码，不作类型转换（返回 Object）
	 * 
	 * @param code 任意 js 代码
	 * @return 执行结果，null 可能表示返回 null，又或者可能没有返回（即 void）
	 */
	public Object eval(String code) {
		return eval(code, Object.class);
	}
}
