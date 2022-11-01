package com.ajaxjs.fast_doc;

import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.ajaxjs.framework.PageResult;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Type;
import com.sun.tools.javadoc.Main;

public class Doclet implements Model {
	private static final LogHelper LOGGER = LogHelper.getLog(Doclet.class);

	/**
	 * Docket 程序入口
	 * 
	 * @param params
	 * @param clz
	 * @return
	 */
	public static BeanInfo parseFieldsOfOneBean(Params params, Class<?> clz) {
		init(params, clz);
		BeanInfo bean = parseFieldsOfOneBean(clz); // 带注释的
		bean.values.addAll(getSuperFields(clz, params));// 附加父类的 fields

		return bean;
	}

	/**
	 * 解析某个 bean 的所有 fields(不包括父类的)
	 * 
	 * @param clz
	 * 
	 * @return
	 */
	private static BeanInfo parseFieldsOfOneBean(Class<?> clz) {
		BeanInfo beanInfo = new BeanInfo();
		beanInfo.name = clz.getSimpleName();
		beanInfo.type = clz.getName();
		beanInfo.values = new ArrayList<>();

		ClassDoc[] classes = root.classes();

		if (classes.length == 1) {
			ClassDoc classDoc = classes[0];
			beanInfo.description = classDoc.commentText();
			beanInfo.values = Util.makeListByArray(classDoc.fields(false), fieldDoc -> {
				Value v = new Value();
				v.name = fieldDoc.name();
				v.type = fieldDoc.type().simpleTypeName();
				v.description = fieldDoc.commentText();

				return v;
			});

			Class<?>[] interfaces = clz.getInterfaces();
			for (Class<?> _interface : interfaces) {

				LOGGER.info(_interface);
			}

			MethodDoc[] methods = classDoc.methods();
			for (MethodDoc methodDoc : methods) {
				LOGGER.info(methodDoc.commentText());

			}
		} else if (classes.length > 1) { // maybe inner clz
			for (ClassDoc clzDoc : classes) {
				/*
				 * qualifiedTypeName() 返回如 xxx.DetectDto.ResourcePlanResult 按照 clz$innerClz 风格转换
				 * xxx.DetectDto$ResourcePlanResult
				 */
				String fullType = clzDoc.qualifiedTypeName();
				String clzName = clzDoc.simpleTypeName();
				fullType = fullType.replace("." + clzName, "$" + clzName);
//				LOGGER.info(fullType);

				if (beanInfo != null && fullType.equals(beanInfo.type)) {
					if (BeanParser.CACHE.containsKey(fullType)) {
						continue; // 已经有
					} else {
						BeanInfo b = new BeanInfo();
						b.name = clzName;
						b.description = clzDoc.commentText();
						b.type = fullType;

						List<Value> simpleValues = new ArrayList<>();
						List<BeanInfo> beans = new ArrayList<>();

						Util.makeListByArray(clzDoc.fields(false), fieldDoc -> {
							Type type = fieldDoc.type();

							Value v = new Value();
							v.name = fieldDoc.name();
							v.type = type.simpleTypeName();
							v.description = fieldDoc.commentText();

							simpleValues.add(v);
							// TODO 递归 内嵌对象。没有 List<?> 真实 Class 引用
//							if (ifSimpleValue(type))
//								simpleValues.add(v);
//							else {
//								BeanInfo bi = new BeanInfo();
//								bi.name = v.type;
//								bi.type = type.toString();
//								bi.description = v.description;
//								LOGGER.info(b.name + ">>>>>>>>>" + v.type);
//								
//								Class<?> clz = ReflectUtil.getClassByName(bi.type);
//
//								if (!"Object".equals(v.type)) // Object 字段无法解析
//									parseFieldsOfOneBean(tempParams, clz, bi);
//
//								beans.add(bi);
//							}

							return v;
						});

						b.values = simpleValues;

						if (!CollectionUtils.isEmpty(beans))
							b.beans = beans;

						BeanParser.CACHE.put(fullType, b);
					}
				}
			}

			BeanInfo beanInCache = BeanParser.CACHE.get(beanInfo.type); // 还是要返回期待的那个

			if (beanInCache != null) {
				beanInfo.description = beanInCache.description;
				beanInfo.values = beanInCache.values;
				beanInfo.beans = beanInCache.beans;
			}
		} else
			LOGGER.warning("Doclet 没解析任何类");

		return beanInfo;
	}

	private final static String[] types = { "int", "java.lang.String", "java.lang.Integer", "java.lang.Boolean", "java.lang.Long" };

	/**
	 * 
	 * @param type
	 * @return
	 */
	public static boolean ifSimpleValue(Type type) {
		String t = type.toString();
//		LOGGER.info(t);
		for (String _t : types) {
			if (_t.equals(t))
				return true;
		}

		return false;
	}

	/**
	 * Doclet 不能获取父类成员
	 * 
	 * @param clazz
	 * @param params
	 * @return 只返回所有父类的 fields
	 */
	private static List<Value> getSuperFields(Class<?> clazz, Params params) {
		Class<?>[] allSuperClazz = ReflectUtil.getAllSuperClazz(clazz);
		List<Value> list = new ArrayList<>();

		if (!ObjectUtils.isEmpty(allSuperClazz)) {
			for (Class<?> clz : allSuperClazz) {
				if (clz == PageResult.class || clz == List.class || clz == ArrayList.class || clz == AbstractList.class || clz == AbstractCollection.class)
					continue;

				Params p = new Params();
				p.root = params.root;
				p.classPath = params.classPath;
				p.sourcePath = params.sourcePath;
				init(p, clz);
				BeanInfo superBeanInfo = parseFieldsOfOneBean(clz);// 父类的信息

				if (!CollectionUtils.isEmpty(superBeanInfo.values))
					list.addAll(superBeanInfo.values);
			}
		}

		return list;
	}

	private static void init(Params params, Class<?> clz) {
		params.sources = new ArrayList<>();

		String clzName = clz.getName();
		boolean isInnerClz = clzName.contains("$");

		if (isInnerClz) {
			clzName = clzName.replaceAll("\\$\\w+$", "");

			params.sources.add(params.root + Util.className2JavaFileName(clzName));
		} else
			params.sources.add(params.root + Util.className2JavaFileName(clz));

		init(params);
	}

	/**
	 * 初始化 Doclet 参数，包括 classpath 和 sourcepath -classpath 参数指定 源码文件及依赖库的 class
	 * 位置，不提供也可以执行，但无法获取到完整的注释信息(比如 annotation)
	 * 
	 * @param params Doclet 参数
	 */
	private static void init(Params params) {
		LOGGER.info("初始化 Doclet");
		init(params.sources, params.sourcePath, params.classPath);
	}

	private static void init(List<String> sources, String sourcePath, String classPath) {
		List<String> params = new ArrayList<>();
		params.add("-doclet");
		params.add(Doclet.class.getName());
		params.add("-docletpath");

		params.add(Doclet.class.getResource("/").getPath());
		params.add("-encoding");
		params.add("utf-8");

		if (sourcePath != null) {
			params.add("-sourcepath");
			params.add(sourcePath);
		}

		if (classPath != null) {
			params.add("-classpath");
			params.add(classPath);
		}

		params.addAll(sources);

		Main.execute(params.toArray(new String[params.size()]));
	}

	/** 文档根节点 */
	private static RootDoc root;

	/**
	 * javadoc 调用入口
	 * 
	 * @param root
	 * @return
	 */
	public static boolean start(RootDoc root) {
		Doclet.root = root;

		return true;
	}
}
