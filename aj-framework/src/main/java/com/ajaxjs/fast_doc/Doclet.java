package com.ajaxjs.fast_doc;

import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.util.ObjectUtils;

import com.ajaxjs.framework.PageResult;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import com.sun.tools.javadoc.Main;

public class Doclet implements Model {
	private static final LogHelper LOGGER = LogHelper.getLog(DocParser.class);

	public static class Params {
		public String root;

		public String sourcePath;

		public String classPath;

		public List<String> sources;
	}

	public static void parseFieldsOfOneBean(Params params, Class<?> clz, BeanInfo bean) {
		Objects.requireNonNull(clz);
		params.sources = new ArrayList<>();
		boolean isInnerClz = bean.type.contains("$");

		if (isInnerClz) {
//			String type = bean.type;
//			type = type.replaceAll("\\$\\w+$", "");
//			type = type.replaceAll("\\$", "#");
//			type = "C:\\code\\drone\\src\\main\\java\\com\\toway\\droneswarm\\detect\\model\\DetectDto.java";
//			params.sources.add(type);

//			System.out.println(">>>>>>>>>>>>" + type + ">>>" + bean.type);
			String type = handleInnerClass(bean.type);
			params.sources.add(params.root + Util.className2JavaFileName(type));
		} else
			params.sources.add(params.root + Util.className2JavaFileName(bean.type));

		init(params);

		// 附加父类的 fields
		BeanInfo parseComment = parseFieldsOfOneBean(bean); // 带注释的
		getSuperFields(clz, params, parseComment);

		bean.description = parseComment.description;
		bean.values = parseComment.values;
	}

	private static String handleInnerClass(String type) {
		type = type.replaceAll("\\$\\w+$", "");

		return type;
	}

	/**
	 * 解析某个 bean 的所有 fields(不包括父类的)
	 * 
	 * @param targetBean
	 * 
	 * @return
	 */
	private static BeanInfo parseFieldsOfOneBean(BeanInfo targetBean) {
		ClassDoc[] classes = root.classes();
		BeanInfo bean = null;

		if (classes.length > 1) { // maybe inner clz
			for (ClassDoc clzDoc : classes) {
				// qualifiedTypeName() 返回如 xxx.DetectDto.ResourcePlanResult
				// 按照 clz$innerClz 风格转换 xxx.DetectDto$ResourcePlanResult
				String fullType = clzDoc.qualifiedTypeName();
				String clzName = clzDoc.simpleTypeName();
				fullType = fullType.replace("." + clzName, "$" + clzName);
				LOGGER.info(fullType);

				if (targetBean != null && fullType.equals(targetBean.type))

					if (DocParser.CACHE.containsKey(fullType)) {
						continue; // 已经有
					} else {
						BeanInfo b = new BeanInfo();
						b.name = clzName;
						b.description = clzDoc.commentText();
						b.type = fullType;
						b.values = Util.makeListByArray(clzDoc.fields(false), fieldDoc -> {
							Value v = new Value();
							v.name = fieldDoc.name();
							v.type = fieldDoc.type().simpleTypeName();
							v.description = fieldDoc.commentText();

							return v;
						});

						DocParser.CACHE.put(fullType, b);
					}
			}

			if (targetBean != null)
				bean = DocParser.CACHE.get(targetBean.type);
		} else {
			ClassDoc classDoc = classes[0];
			bean = new BeanInfo();
			bean.name = classDoc.name();
			bean.description = classDoc.commentText();
			bean.values = Util.makeListByArray(classDoc.fields(false), fieldDoc -> {
				Value v = new Value();
				v.name = fieldDoc.name();
				v.type = fieldDoc.type().simpleTypeName();
				v.description = fieldDoc.commentText();

				return v;
			});
		}

		return bean;
	}

	/**
	 * Doclet 不能获取父类成员
	 * 
	 * @param real
	 * @param params
	 * @param parse
	 */
	private static void getSuperFields(Class<?> real, Params params, BeanInfo parse) {
		Class<?>[] allSuperClazz = ReflectUtil.getAllSuperClazz(real);

		if (!ObjectUtils.isEmpty(allSuperClazz)) {
			for (Class<?> clz : allSuperClazz) {
				if (clz == PageResult.class || clz == List.class || clz == ArrayList.class || clz == AbstractList.class
						|| clz == AbstractCollection.class)
					continue;

				Params p = new Params();
				p.root = params.root;
				p.classPath = params.classPath;
				p.sourcePath = params.sourcePath;
				p.sources = new ArrayList<>();

				String type = clz.getName();
				boolean isInnerClz = type.contains("$");

				if (isInnerClz) {
//					System.out.print(">>>>>>>>>>" + type);
//					type = type.replaceAll("\\$\\w+$", "");
//					type = type.replaceAll("\\$", "#");
//					type = "com\\toway\\droneswarm\\detect\\model\\DetectDto.java#CloseResPlan";
//					type = "C:\\code\\drone\\src\\main\\java\\com\\toway\\droneswarm\\detect\\model\\DetectDto.java";
//					params.sources.add(type);
					type = handleInnerClass(type);

					params.sources.add(params.root + Util.className2JavaFileName(type));
				} else
					p.sources.add(params.root + Util.className2JavaFileName(clz));

				init(p);
				BeanInfo parse2 = parseFieldsOfOneBean(parse);

				if (parse2 != null && parse.values != null)
					parse.values.addAll(parse2.values);
			}
		}
	}

	/**
	 * 初始化 Doclet 参数，包括 classpath 和 sourcepath -classpath 参数指定
	 * 源码文件及依赖库的class位置，不提供也可以执行，但无法获取到完整的注释信息(比如annotation)
	 * 
	 * @param params Doclet 参数
	 */
	private static void init(Params params) {
		init(params.sources, params.sourcePath, params.classPath);
	}

	private static void init(List<String> sources, String sourcePath, String classPath) {
		List<String> params = new ArrayList<>();
		params.add("-doclet");
		params.add(Doclet.class.getName());
		params.add("-docletpath");

		System.out.println(Doclet.class.getResource("/").getPath());
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
