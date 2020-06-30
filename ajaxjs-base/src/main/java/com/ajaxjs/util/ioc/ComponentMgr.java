package com.ajaxjs.util.ioc;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.logger.LogHelper;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

/**
 * 组件管理器，单例
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class ComponentMgr {
	private static final LogHelper LOGGER = LogHelper.getLog(ComponentMgr.class);

	public static Map<String, ComponentInfo> components = new HashMap<>();

	/**
	 * 记录依赖关系
	 */
	private static Map<String, String> dependencies = new HashMap<>();

	@SuppressWarnings("unchecked")
	public static void scan() {
		Set<Class<Object>> clzs = new LinkedHashSet<>();

		new EveryClass().scan((resource, packageName) -> {
//			System.out.println(resource);
			ClassPool cp = ClassPool.getDefault();

			// 类加载并添加 setter
			try {
				CtClass cc = cp.get(resource);

				CtField[] fields = cc.getDeclaredFields();

				for (CtField field : fields) {
					if (field.getAnnotation(Resource.class) != null) {
						String setMethodName = "set" + ReflectUtil.firstLetterUpper(field.getName());
						CtMethod setter;

						try {
							setter = cc.getDeclaredMethod(setMethodName, new CtClass[] { field.getType() });
						} catch (NotFoundException e) {
							// 另外一种写法
//							String tpl = "public void %s(%s %s) { this.%s = %s; }";
//							String m = String.format(tpl, setMethodName, field.getType().getName(), field.getName(), field.getName(), field.getName());
//							setter = CtNewMethod.make(m, cc);

							CtField f1 = new CtField(field.getType(), field.getName(), cc);
							setter = CtNewMethod.setter(setMethodName, f1);
							cc.addMethod(setter);
						}
					}
				}

//				if ("com.ajaxjs.ioc.Hi".equals(resourcePath)) {
//					CtField f1 = new CtField(cp.get("com.ajaxjs.ioc.Person"), "person", cc);
//					cc.addMethod(CtNewMethod.setter("setPerson", f1));
//				}

				Class<Object> clazz = (Class<Object>) cc.toClass();
				clzs.add(clazz);
			} catch (CannotCompileException e) {
				Class<Object> clazz = (Class<Object>) ReflectUtil.getClassByName(resource);
				clzs.add(clazz);
			} catch (NotFoundException | ClassNotFoundException e) {
				LOGGER.warning(e);
			}
		}, "com.ajaxjs");

		for (Class<Object> item : clzs) {
			Bean annotation = item.getAnnotation(Bean.class); // 查找匹配的注解
			Named namedAnno = item.getAnnotation(Named.class);

			if (annotation == null && namedAnno == null)
				continue; // 不是 bean 啥都不用做

			String alias = getAlias(annotation, namedAnno, item);

			if (components.containsKey(alias))
				LOGGER.warning("相同的 bean name 已经存在" + alias);

			register(alias, item, true);

			// 记录依赖关系
			for (Field field : item.getDeclaredFields()) {
				Resource res = field.getAnnotation(Resource.class);
				Inject inject = field.getAnnotation(Inject.class);

				if (inject == null && res == null)
					continue; // 没有要注入的字段，跳过
				else {
					/*
					 * 要查找哪一个 bean？就是说依赖啥对象？以什么为依据？我们说是那个 bean 的 id。首先你可以在 Resource
					 * 注解中指定，如果这觉得麻烦，可以不在注解指定，直接指定变量名即可（就算不通过注解指定，都可以利用 反射 获取字段名，作为依赖的凭据，效果一样）
					 */
					// 获取依赖的 bean 的名称,如果为 null, 则使用字段名称
					String dependenciObj_id = res == null ? field.getAnnotation(Named.class).value() : res.value();
					dependenciObj_id = parseId(dependenciObj_id);

					if (CommonUtil.isEmptyString(dependenciObj_id))
						dependenciObj_id = field.getName(); // 此时 bean 的 id 一定要与 fieldName 一致

					// bean id ＋ 变量名称 ＝ 依赖关系的 key。

					dependencies.put(alias + "." + field.getName(), dependenciObj_id);

					// 不能马上执行 setter 注入，因为有可能相关组件还未实例化
//					String setMethodName = "set" + ReflectUtil.firstLetterUpper(field.getName());
//					ReflectUtil.getMethod(item, setMethodName, field.getType());
				}
			}
		}

		// 扫描依赖关系并注入 bean.
		dependencies.forEach((k, v) -> {
//			String value = dependencies.get(key);// 依赖对象的值
			String[] split = k.split("\\.");// 数组第一个值表示 bean 对象名称，第二个值为字段属性名称

			Object bean = get(split[0]), argBean = get(v);

			Objects.requireNonNull(bean, split[0] + "执行[" + split[1] + "]未发现类");

			if (argBean == null)
				LOGGER.warning("容器中找不到实例[{0}]。请确定是否为组件添加 @Bean 注解?", v);
			else
				ReflectUtil.setProperty(bean, split[1], argBean);
		});
	}

	// TODO: remove Bean
	private static String getAlias(Bean annotation, Named namedAnno, Class<Object> clz) {
		String value = null;

		if (annotation != null) // 获取 Bean 的名称，如果没有则取类 SimpleName
			value = annotation.value();
		else if (namedAnno != null) // 如果有 Named 注解则读取它的值
			value = namedAnno.value();

		return CommonUtil.isEmptyString(value) ? clz.getSimpleName() : value;
	}

	/**
	 * 可以从 JSON 配置文件读取依赖对象。这时以 autoWire: 开头指向配置内容，内容即具体 Bean 的 id。
	 * 
	 * @param dependenciObj_id
	 * @return
	 */
	private static String parseId(String dependenciObj_id) {
		if (dependenciObj_id.startsWith("autoWire:")) {
			String str = dependenciObj_id.replaceFirst("autoWire:", "");
			String[] arr = str.split("\\|");
//			String extendedId = ConfigService.getValueAsString(arr[0]);
			String extendedId = "";

			// 没有扩展，读取默认的
			return extendedId == null ? arr[1] : extendedId;
		}

		return dependenciObj_id;
	}

	public static Object get(String alias) {
		ComponentInfo compInfo = components.get(alias);
		Objects.requireNonNull(compInfo, "找不到组件 [" + alias + "]");
		return compInfo.instance;
	}

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

	/**
	 * 
	 * @param aliasOrClz
	 * @param clz
	 * @return
	 */
	public static Object get(String aliasOrClz, Class<?> clz) {
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

	/**
	 * 根据接口查找目标对象
	 * 
	 * @param interfaceClz 接口类
	 * @return 目标对象的集合
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getAllByInterface(Class<T> interfaceClz) {
		List<T> list = new ArrayList<>();

		components.forEach((alias, compInfo) -> {
			if (interfaceClz.isAssignableFrom(compInfo.instance.getClass()))
				list.add((T) compInfo.instance);
		});

		return list;
	}

	/**
	 * 根据类查找实例列表
	 * 
	 * @param <T> 目标类型
	 * @param clz 类引用
	 * @return 实例对象列表
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getAll(Class<T> clz) {
		List<T> list = new ArrayList<>();

		components.forEach((alias, compInfo) -> {
			if (clz.isInstance(compInfo.instance))
				list.add((T) compInfo.instance);
		});

		return list;
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
