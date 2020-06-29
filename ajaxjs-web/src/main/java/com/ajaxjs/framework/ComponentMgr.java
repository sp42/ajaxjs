package com.ajaxjs.framework;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.ajaxjs.util.ReflectUtil;

/**
 * 组件管理器，单例
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class ComponentMgr {
	public static Map<String, ComponentInfo> components = new HashMap<>();

	public static <T> T get(Class<T> clz) {
		ComponentInfo compInfo = null;

		for (String alias : components.keySet()) { // 查询目标组件
			ComponentInfo _compInfo = components.get(alias);
			if (_compInfo.clazz == clz) {
				compInfo = _compInfo;
			}
		}

		return getInstance(compInfo, clz);
	}

	@SuppressWarnings("unchecked")
	private static <T> T getInstance(ComponentInfo compInfo, Class<T> clz) {
		Objects.requireNonNull(compInfo, "找不到目标组件");

		if (compInfo.instance == null)
			return (T) ReflectUtil.newInstance(compInfo.clazz);
		else
			return (T) compInfo.instance;
	}

	public static <T> T get(String aliasOrClz, Class<T> clz) {
		if (!aliasOrClz.contains(".")) // 别名
			for (String alias : components.keySet()) { // 查询目标组件
				if (alias == aliasOrClz) {
					ComponentInfo compInfo = components.get(alias);
					return getInstance(compInfo, clz);
				}
			}
		else { // 命名空间
			for (String alias : components.keySet()) { // 查询目标组件
				ComponentInfo compInfo = components.get(alias);
				if (compInfo.namespace == aliasOrClz) {
					return getInstance(compInfo, clz);
				}
			}
		}

		return null;
	}

	public static void register(String alias, Class<?> clz, boolean isSingleton) {
		ComponentInfo compInfo = new ComponentInfo();
		compInfo.clazz = clz;

		if (isSingleton)
			compInfo.instance = ReflectUtil.newInstance(clz);

		if (alias == null)
			alias = clz.getCanonicalName();
		
		components.put(alias, compInfo);
	}
}
