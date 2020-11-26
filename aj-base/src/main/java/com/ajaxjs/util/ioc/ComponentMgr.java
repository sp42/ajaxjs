package com.ajaxjs.util.ioc;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.ajaxjs.framework.GetConfig;
import com.ajaxjs.framework.IComponent;
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

	/**
	 * 保存所有的组件
	 */
	public static Map<String, ComponentInfo> components = new HashMap<>();

	/**
	 * 不重复的每一个类，经过 Javaassit 初始化过的
	 */
	public static Set<Class<?>> clzs = new LinkedHashSet<>();

	/**
	 * 扫描指定的包。具体流程是 Javassist 添加 setter、类加载、依赖注射
	 * 
	 * @param packageNames 包名数组，会递归这个包下面所有的类
	 */
	public static void scan(String... packageNames) {
		for (String packageName : packageNames)
			scan(packageName);
	}

	/**
	 * 扫描指定的包。具体流程是 Javassist 添加 setter、类加载、依赖注射
	 * 
	 * @param packageName 包名，会递归这个包下面所有的类
	 */
	@SuppressWarnings("unchecked")
	public static void scan(String packageName) {
		LOGGER.debug("扫描[{0}]包下面所有的类", packageName);

		Set<Class<?>> clzs = new LinkedHashSet<>();

		new EveryClass().scan(packageName, resource -> {
			ClassPool cp = ClassPool.getDefault();
			// 类加载并添加 setter
			try {
				CtClass cc = cp.get(resource);
				CtField[] fields = cc.getDeclaredFields();

				for (CtField field : fields) {
					if (field.getAnnotation(Resource.class) != null) {
//						if ("com.ajaxjs.user.register.RegisterService".equals(resource))
//							LOGGER.debug(field.toString());

						String setMethodName = "set" + ReflectUtil.firstLetterUpper(field.getName());
						CtMethod setter;

						try {
							setter = cc.getDeclaredMethod(setMethodName, new CtClass[] { field.getType() });
						} catch (NotFoundException e) {
							// 另外一种写法
//							String tpl = "public void %s(%s %s) { this.%s = %s; }";
//							String m = String.format(tpl, setMethodName, field.getType().getName(), field.getName(), field.getName(), field.getName());
//							setter = CtNewMethod.make(m, cc);
//							e.printStackTrace();

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

				Class<?> clz = (Class<?>) cc.toClass();
				if (clz.isPrimitive() || Modifier.isAbstract(clz.getModifiers()) || clz.isAnnotation() || clz.isInterface() || clz.isArray() || clz.getName().indexOf("$") != -1) {
				} else {
					// 组件才享有优先类加载的权利
					if (IComponent.class.isAssignableFrom(clz))
						ReflectUtil.getClassByName(clz.getCanonicalName());

					clzs.add(clz);
				}
			} catch (CannotCompileException e) {
				Class<Object> clazz = (Class<Object>) ReflectUtil.getClassByName(resource);
				clzs.add(clazz);
			} catch (NotFoundException | ClassNotFoundException e) {
				LOGGER.warning(e);
			}
		});

		ComponentMgr.clzs.addAll(clzs);
	}

	public static void inject() {
		// 记录依赖关系
		Map<String, String> dependencies = new HashMap<>();

		for (Class<?> clz : clzs) {
			Component annotation = clz.getAnnotation(Component.class); // 查找匹配的注解

			if (annotation == null)
				continue; // 不是 bean 啥都不用做

			// 获取 Bean 的名称，如果没有则取类 SimpleName
			String alias = annotation.value();
			if (CommonUtil.isEmptyString(alias))
				alias = clz.getSimpleName();

			if (components.containsKey(alias))
				LOGGER.warning("相同的 组件名称（Alias）[{0}] 已经存在", alias);

//			if (clz.toString().contains("org.gdhdc.service.MyRegisterService")) {
//				LOGGER.info(clz.toString() + ":::::::::");
//			}

			register(alias, clz, true);

			// 记录依赖关系
			Class<?> superClz = clz;// 如果有继承的，查找其父类

			for (; superClz != Object.class; superClz = superClz.getSuperclass()) {
//				if (clz.toString().contains("org.gdhdc.service.MyRegisterService")) {
//					LOGGER.info(superClz.toString() + ":::::::::");
//				}

				for (Field field : superClz.getDeclaredFields()) {
					Resource res = field.getAnnotation(Resource.class);

					if (res == null)
						continue; // 没有要注入的字段，跳过

					/*
					 * 要查找哪一个 bean？就是说依赖啥对象？以什么为依据？我们说是那个 bean 的 id。首先你可以在 Resource
					 * 注解中指定，如果这觉得麻烦，可以不在注解指定，直接指定变量名即可（就算不通过注解指定，都可以利用 反射 获取字段名，作为依赖的凭据，效果一样）
					 */
					String dependenciObj_id = parseId(res, field);
//					LOGGER.info(alias + "." + field.getName() + ":::" + dependenciObj_id);

					// bean id ＋ 变量名称 ＝ 依赖关系的 key。
					dependencies.put(alias + "." + field.getName(), dependenciObj_id);
				}
			}
		}

		// 扫描依赖关系并注入 bean.
		dependencies.forEach((k, v) -> {
			String[] split = k.split("\\.");// 数组第一个值表示 bean 对象名称，第二个值为字段属性名称
			Object bean = get(split[0]), argBean = get(v);
			Objects.requireNonNull(bean, split[0] + "执行[" + split[1] + "]未发现类");

			if (argBean == null)
				LOGGER.warning("容器中找不到实例[{0}]。请确定是否为组件添加 @Component 注解?", v);
			else
				ReflectUtil.setProperty(bean, split[1], argBean);
		});
	}

	/**
	 * 可以从 JSON 配置文件读取依赖对象。这时以 autoWire: 开头指向配置内容，内容即具体 Bean 的 id。
	 * 
	 * @param res
	 * @param field
	 * @return
	 */
	private static String parseId(Resource res, Field field) {
		// 获取依赖的 bean 的名称,如果为 null, 则使用字段名称
		String resource = res.value();

		if (resource.startsWith("autoWire:") && ReflectUtil.getClassByName("com.ajaxjs.framework.config.ConfigService") != null) {
			GetConfig cfg = getByInterface(GetConfig.class);

			if (cfg == null) { // 强制加载配置
				ComponentMgr.register("ConfigService", ReflectUtil.getClassByName("com.ajaxjs.framework.config.ConfigService"), true);
				cfg = getByInterface(GetConfig.class);
			}

			String target = resource.replaceFirst("autoWire:", "");
			String extendedId = cfg.getString(target);

			// 没有扩展，读取默认的
			return extendedId == null ? field.getType().getSimpleName() : extendedId;
		}

		if (CommonUtil.isEmptyString(resource))
			resource = field.getType().getSimpleName(); // 此时 bean 的 id 一定要与 fieldName 一致

		return resource;
	}

	// -------------------------------------------------------------------------------------------------

	/**
	 * 根据类、接口查找组件
	 * 
	 * @param <T> 目标类型
	 * @param clz 组件类型、接口
	 * @return 目标组件
	 */
	public static <T> T get(Class<T> clz) {
		if (clz.isInterface())
			return getByInterface(clz);

		ComponentInfo compInfo = null;

		for (String alias : components.keySet()) { // 查询目标组件
			ComponentInfo _compInfo = components.get(alias);

			if (clz == _compInfo.clazz || (_compInfo.instance != null && clz.isInstance(_compInfo.instance)))
				compInfo = _compInfo;
		}

		return getInstance(compInfo, clz, clz);
	}

	/**
	 * 根据类、接口查找组件列表
	 * 
	 * @param <T> 目标组件类型
	 * @param clz 组件类型或接口
	 * @return 实例对象列表
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getAll(Class<T> clz) {
		if (clz.isInterface())
			return getAllByInterface(clz);

		List<T> list = new ArrayList<>();

		components.forEach((alias, compInfo) -> {
			if (clz == compInfo.clazz || (compInfo.instance != null && clz.isInstance(compInfo.instance)))
				list.add((T) compInfo.instance);
		});

		return list;
	}

	/**
	 * 根据命名空间、别名查找组件
	 * 
	 * @param aliasOrClz 命名空间或别名
	 * @return 目标组件，如果找不到返回 null
	 */
	public static Object get(String aliasOrClz) {
		return get(aliasOrClz, Object.class);
	}

	/**
	 * 根据命名空间、别名查找组件
	 * 
	 * @param aliasOrClz 命名空间或别名
	 * @param clz        为避免强类型转换，特意传入一个类型
	 * @return 目标组件，如果找不到返回 null
	 */
	public static <T> T get(String aliasOrClz, Class<T> clz) {
		if (aliasOrClz.contains(".")) // 命名空间
			for (String alias : components.keySet()) { // 查询目标组件
				ComponentInfo compInfo = components.get(alias);

				if (aliasOrClz.equals(compInfo.namespace))
					return getInstance(compInfo, clz, aliasOrClz);
			}
		else { // 别名
			ComponentInfo compInfo = components.get(aliasOrClz);
			return getInstance(compInfo, clz, aliasOrClz);
		}

		return null;
	}

	/**
	 * 返回组件。如果是非单例组件，则创建新的实例（无构造器的实例化）
	 * 
	 * @param <T>      目标组件类型
	 * @param compInfo 组件信息
	 * @param clz      为避免强类型转换，特意传入一个类型
	 * @param info     便于调试的相关信息
	 * @return 目标组件
	 */
	@SuppressWarnings("unchecked")
	private static <T> T getInstance(ComponentInfo compInfo, Class<T> clz, Object info) {
		Objects.requireNonNull(compInfo, "找不到[" + info + "]目标组件");

		if (compInfo.instance == null) // 不是单例，每次都创建新的实例
			return (T) ReflectUtil.newInstance(compInfo.clazz);
		else
			return (T) compInfo.instance;
	}

	// -------------------------------------------------------------------------------------------------

	/**
	 * 根据接口查找单个目标组件。该方法只会找到第一个匹配的结果。如果确定该接口之实例是唯一，可以使用这个方法。
	 * 
	 * @param <T>          目标组件接口类型
	 * @param interfaceClz 接口类
	 * @return 目标组件，如果找不到返回 null
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getByInterface(Class<T> interfaceClz) {
		for (String alias : components.keySet()) {
			Object instance = components.get(alias).instance;

			if (instance != null && interfaceClz.isAssignableFrom(instance.getClass()))
				return (T) instance;
		}

		return null;
	}

	/**
	 * 根据接口查找多个目标组件
	 * 
	 * @param <T>          目标组件类型
	 * @param interfaceClz 接口类
	 * @return 目标组件的集合，如果找不到返回一个空的 list
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getAllByInterface(Class<T> interfaceClz) {
		List<T> list = new ArrayList<>();

		components.forEach((alias, compInfo) -> {
			Object instance = components.get(alias).instance;

			if (instance != null && interfaceClz.isAssignableFrom(instance.getClass()))
				list.add((T) instance);
		});

		return list;
	}

	// -------------------------------------------------------------------------------------------------

	/**
	 * 注册一个组件
	 * 
	 * @param alias       组件索引
	 * @param clz         组件类引用
	 * @param isSingleton 是否单例，如果是马上创建实例
	 */
	public static void register(String alias, Class<?> clz, boolean isSingleton) {
		ComponentInfo compInfo = new ComponentInfo();
		compInfo.clazz = clz;
		compInfo.namespace = clz.getCanonicalName();

		if (isSingleton)
			compInfo.instance = ReflectUtil.newInstance(clz);

		if (alias == null)
			alias = clz.getCanonicalName();

		components.put(alias, compInfo);
	}
}
