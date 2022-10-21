package com.ajaxjs.fast_doc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.ajaxjs.util.ReflectUtil;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;
import com.sun.tools.javadoc.Main;

public class Doclet implements Model {
	/**
	 * 解析某个 bean 的所有 fields(不包括父类的)
	 * 
	 * @param sources
	 * @return
	 */
	public static BeanInfo parseFieldsOfOneBean(List<String> sources) {
		init(sources);
		ClassDoc[] classes = root.classes();

		ClassDoc classDoc = classes[0];
		BeanInfo bean = new BeanInfo();

		bean.name = classDoc.name();
		bean.comment = classDoc.commentText();
		bean.values = DocParser.makeListByArray(classDoc.fields(false), fieldDoc -> {
			Value v = new Value();
			v.name = fieldDoc.name();
			v.type = fieldDoc.type().simpleTypeName();
			v.desc = fieldDoc.commentText();

			return v;
		});

		return bean;
	}

	/**
	 * 附加父类的 fields
	 * 
	 * @param real
	 * @param bean
	 * @param sources
	 * @return
	 */
	public static void parseFieldsOfOneBean(String root, Class<?> real, BeanInfo bean, List<String> sources) {
		BeanInfo parseComment = parseFieldsOfOneBean(sources); // 带注释的
		getSuperFields(real, root, parseComment);
		bean.values = parseComment.values;
//		for (Value v : parseComment.values) {
//			for (Value v2 : bean.values) {
//				if (v.name.equals(v2.name)) {
//
//					v2.desc = v.desc;
//					break;
//				}
//			}
//		}
//		TestHelper.printJson(bean);
	}

	/**
	 * Doclet 不能获取父类成员
	 * 
	 * @param real
	 * @param root
	 * @param parse
	 */
	private static void getSuperFields(Class<?> real, String root, BeanInfo parse) {
		List<Value> superValues = new ArrayList<>();
		Class<?>[] allSuperClazz = ReflectUtil.getAllSuperClazz(real);

		if (!ObjectUtils.isEmpty(allSuperClazz)) {
			for (Class<?> clz : allSuperClazz) {
				List<String> sources = initDoclet(root);
				sources.add(root + className2JavaFileName(clz));

				BeanInfo parse2 = Doclet.parseFieldsOfOneBean(sources);

				if (parse2 != null)
					superValues.addAll(parse2.values);
			}
		}

		if (!CollectionUtils.isEmpty(superValues))
			parse.values.addAll(superValues);
	}

	/**
	 * 初始化 Doclet 参数，包括 classpath 和 sourcepath -classpath 参数指定
	 * 源码文件及依赖库的class位置，不提供也可以执行，但无法获取到完整的注释信息(比如annotation)
	 * 
	 * @param root
	 * @return Doclet 参数
	 */
	static List<String> initDoclet(String root) {
		String classpath = "C:\\project\\drone\\jar\\lbsalgo-0.0.1-SNAPSHOT.jar";

		List<String> params = new ArrayList<>();
		params.add("-classpath");
		params.add(classpath);
		params.add("-sourcepath");
		params.add(root + ";C:\\code\\aj-framework\\src\\main\\java;C:\\code\\aj-util\\src\\main\\java");

		return params;
	}

	private static void init(List<String> sources) {
		List<String> list = new ArrayList<>();
		list.add("-doclet");
		list.add(Doclet.class.getName());
		list.add("-docletpath");
		list.add(Doclet.class.getResource("/").getPath());
		list.add("-encoding");
		list.add("utf-8");
		list.addAll(sources);

		Main.execute(list.toArray(new String[list.size()]));
	}

	/** 文档根节点 */
	private static RootDoc root;

	/**
	 * javadoc调用入口
	 * 
	 * @param root
	 * @return
	 */
	public static boolean start(RootDoc root) {
		Doclet.root = root;

		return true;
	}

	/**
	 * 类全称转为 .java 磁盘路径
	 * 
	 * @param clzName
	 * @return
	 */
	public static String className2JavaFileName(String clzName) {
		return clzName.replaceAll("\\.", "\\\\") + ".java";
	}

	public static String className2JavaFileName(Class<?> clz) {
		return className2JavaFileName(clz.getName());
	}

	/**
	 * 打印类及其字段、方法的注释
	 *
	 * @param sources java源文件路径
	 */
	public static void println(List<String> sources) {
		init(sources);
		ClassDoc[] classes = root.classes();
		StringBuilder buffer = new StringBuilder();

		for (ClassDoc classDoc : classes) {
			buffer.append(classDoc.name()).append('\n');
			buffer.append('\t').append(classDoc.commentText()).append('\n');
			buffer.append('\t').append("字段").append('\n');

			FieldDoc[] fields = classDoc.fields(false);

			for (FieldDoc field : fields) {
				buffer.append('\t').append('\t').append(field.name()).append('\n');
				buffer.append('\t').append('\t').append('\t').append(field.commentText()).append('\n'); // 字段上的注释
			}

			buffer.append('\t').append("方法").append('\n');

			MethodDoc[] methods = classDoc.methods(false);
			for (MethodDoc method : methods) {
				buffer.append('\t').append('\t').append(method.name()).append('\n');
				buffer.append('\t').append('\t').append('\t').append(method.commentText()).append('\n'); // 方法上的注释
			}
		}

		System.out.println(buffer);
	}

}
