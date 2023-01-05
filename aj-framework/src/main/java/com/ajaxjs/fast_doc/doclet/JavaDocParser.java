package com.ajaxjs.fast_doc.doclet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.ajaxjs.fast_doc.Params;
import com.ajaxjs.util.ListUtils;
import com.ajaxjs.util.TestHelper;
import com.ajaxjs.util.logger.LogHelper;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;
//import com.sun.tools.javadoc.Main;

/**
 * Java 标准文档的提取器 当前只提取 Field 和 Method 两种成员
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class JavaDocParser extends Doclet implements DocModel {
	private static final LogHelper LOGGER = LogHelper.getLog(JavaDocParser.class);

	/**
	 * 提取结果
	 */
	public static ClassDocInfo[] ClassDocInfoResult;

	public static Map<String, ClassDocInfo> CACHE = new HashMap<>();

	/**
	 * javadoc 调用入口
	 * 
	 * @param root 文档根节点
	 * @return
	 */
	public static boolean start(RootDoc root) {
		ClassDoc[] classDocs = root.classes();

		if (ObjectUtils.isEmpty(classDocs)) {
			LOGGER.warning("没有找到任何结果");
			return false;
		}

		ClassDocInfoResult = new ClassDocInfo[classDocs.length];

		int i = 0;
		for (ClassDoc doc : classDocs) {
			LOGGER.info("返回结果 " + doc.name());
			ClassDocInfo classInfo = getClassDocInfo(doc, null);
			ClassDocInfoResult[i++] = classInfo;
		}

		return true;
	}

	private static ClassDocInfo getClassDocInfo(ClassDoc superDoc, ClassDocInfo son) {
		ClassDocInfo classInfo = new ClassDocInfo();
		classInfo.name = superDoc.name();
		classInfo.commentText = superDoc.commentText();
		classInfo.fullName = superDoc.qualifiedName();
		classInfo.methods = getMethod(superDoc);
		classInfo.fields = getFields(superDoc);

		if (son != null) {
			if (!ObjectUtils.isEmpty(classInfo.fields))
				son.fields = ListUtils.concat(classInfo.fields, son.fields);
			if (!ObjectUtils.isEmpty(classInfo.methods))
				son.methods = ListUtils.concat(classInfo.methods, son.methods);
		}

		ClassDoc _superDoc = superDoc.superclass();
		if (_superDoc != null && !"java.lang.Object".equals(_superDoc.toString()))
			getClassDocInfo(_superDoc, classInfo);

		TestHelper.printJson(classInfo);

		if (!CACHE.containsKey(classInfo.fullName))
			CACHE.put(classInfo.fullName, classInfo);

		return classInfo;
	}

	/**
	 * 提取字段
	 * 
	 * @param clzDoc
	 * @return
	 */
	private static FieldInfo[] getFields(ClassDoc clzDoc) {
		FieldDoc[] fieldDocs = clzDoc.fields(false);

		if (ObjectUtils.isEmpty(fieldDocs))
			return null;

		FieldInfo[] infos = new FieldInfo[fieldDocs.length];

		int i = 0;
		for (FieldDoc doc : fieldDocs) {
			FieldInfo info = new FieldInfo();
			info.name = doc.name();
			info.commentText = doc.commentText();
			info.type = doc.type().simpleTypeName();
			info.typeFullName = doc.type().qualifiedTypeName();

			infos[i++] = info;
		}

		return infos;
	}

	/**
	 * 提取方法
	 * 
	 * @param clzDoc
	 * @return
	 */
	private static MethodInfo[] getMethod(ClassDoc clzDoc) {
		MethodDoc[] methods = clzDoc.methods();

		if (ObjectUtils.isEmpty(methods)) {
//			LOGGER.warning("没有找到任何方法");
			return null;
		}

		List<MethodInfo> list = new ArrayList<>();

		for (MethodDoc method : methods) {
			if (!StringUtils.hasText(method.getRawCommentText()))// 没有注释就忽略
				continue;
			MethodInfo info = new MethodInfo();
			info.name = method.name();
			info.commentText = method.commentText();
			info.parameters = getParameters(method);
			info.returnType = method.returnType().typeName();
			info.returnTypeFullName = method.returnType().qualifiedTypeName();
			info.returnComment = getReturnComment(method);

			list.add(info);
		}

		return list.toArray(new MethodInfo[list.size()]);
	}

	/**
	 * 提取参数列表内容
	 * 
	 * @param method
	 * @return
	 */
	private static ParameterInfo[] getParameters(MethodDoc method) {
		Parameter[] parameters = method.parameters();

		if (ObjectUtils.isEmpty(parameters))
			return null;

		ParameterInfo[] parameterInfos = new ParameterInfo[parameters.length];
		ParamTag[] paramTags = method.paramTags();

		int i = 0;
		for (Parameter p : parameters) {
			ParameterInfo info = new ParameterInfo();
			info.name = p.name();
			info.commentText = getParameComment(paramTags, i);
			info.type = p.typeName();
			info.typeFullName = p.type().qualifiedTypeName();
			parameterInfos[i++] = info;
		}

		return parameterInfos;
	}

	private static String getParameComment(ParamTag[] paramTags, int i) {
		if (paramTags == null)
			return null;

		try {
			if (paramTags[i] == null || !StringUtils.hasText(paramTags[i].parameterComment()))
				return null;
			return paramTags[i].parameterComment();
		} catch (ArrayIndexOutOfBoundsException e) {
			// 有时文档写不全，则 paramTags 不能获取
		}

		return null;
	}

	final static String RETURN_TAG = "@return";

	/**
	 * 提取 return 标签
	 * 
	 * @param method
	 * @return
	 */
	private static String getReturnComment(MethodDoc method) {
		Tag[] tags = method.tags(RETURN_TAG);

		if (!ObjectUtils.isEmpty(tags) && tags.length > 0 && StringUtils.hasLength(tags[0].text()))
			return tags[0].text();
		else
			return null;
	}

	/**
	 * 初始化 Doclet 参数，包括 classpath 和 sourcepath -classpath 参数指定 源码文件及依赖库的 class
	 * 位置，不提供也可以执行，但无法获取到完整的注释信息(比如 annotation)
	 * 
	 * @param params Doclet 参数
	 */
	public static void init(Params params) {
		LOGGER.info("初始化 Doclet");
		init(params.sources, params.sourcePath, params.classPath);
	}

	/**
	 * 
	 * @param sources    Java 源码列表，需要解析的 .java 文件的绝对路径
	 * @param sourcePath 你所有项目的源码目录，用分号隔开，例如
	 *                   <code>C:\\code\\oba\\src\\main\\java\\;C:\\code\\aj\\aj-framework\\src\\main\\java;C:\\code\\aj\\aj-util\\src\\main\\java
	 * @param classPath  依赖库的 class 或者 jar 包目录，例如
	 *                   <code>C:\\sp42\\profile\\eclipse\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\aj-sso\\WEB-INF\\lib
	 */
	private static void init(List<String> sources, String sourcePath, String classPath) {
		List<String> params = new ArrayList<>();
		params.add("-doclet");
		params.add(JavaDocParser.class.getName());
		params.add("-docletpath");
		params.add(JavaDocParser.class.getResource("/").getPath());
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

		String[] array = params.toArray(new String[params.size()]);

		// 反射调用 JavaDoc，避免强依赖
		try {
			Class<?> clz = Class.forName("com.sun.tools.javadoc.Main");

			try {
				Method method = clz.getMethod("execute", String[].class);
				method.invoke(null, new Object[] { array });
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				LOGGER.warning(e);
			}
		} catch (ClassNotFoundException e) {
			LOGGER.warning("找不到类 com.sun.tools.javadoc.Main，请保证 Maven 依赖");
		}
	}
}
