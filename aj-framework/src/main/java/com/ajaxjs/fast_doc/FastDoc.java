package com.ajaxjs.fast_doc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.ajaxjs.fast_doc.Model.ArgInfo;
import com.ajaxjs.fast_doc.Model.ControllerInfo;
import com.ajaxjs.fast_doc.Model.Item;
import com.ajaxjs.fast_doc.annotation.CustomAnnotationParser;
import com.ajaxjs.fast_doc.doclet.DocModel.ClassDocInfo;
import com.ajaxjs.fast_doc.doclet.DocModel.MethodInfo;
import com.ajaxjs.fast_doc.doclet.DocModel.ParameterInfo;
import com.ajaxjs.fast_doc.doclet.JavaDocParser;
import com.ajaxjs.util.TestHelper;
import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.map.JsonHelper;

/**
 * FastDoc 主程序，单例
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class FastDoc {
	static Map<String, ControllerInfo> AnnotationResult = new HashMap<>();

	/**
	 * 用 JavaDocParser 得到 Bean 的注释文档
	 * 
	 * @param dir    源码磁盘目录
	 * @param clazzs Bean 类，其实任意 Java 类都可以，包括控制器
	 */
	static void loadBeans(String dir, Class<?>... clazzs) {
		Params params = new Params();
		params.sources = new ArrayList<>();

		for (Class<?> clz : clazzs) {
			String name = Util.className2JavaFileName(clz);
			params.sources.add(dir + name);
		}

		JavaDocParser.init(params);
	}

	/**
	 * 
	 * @param dir    源码磁盘目录
	 * @param clazzs 控制器类列表
	 */
	static void loadControllersDoc(String dir, Class<?>... clazzs) {
		loadBeans(dir, clazzs);

		for (Class<?> clz : clazzs) {
			String fullName = clz.getName();

			if (!AnnotationResult.containsKey(fullName)) {
				CustomAnnotationParser info = new CustomAnnotationParser(clz);
				info.setTakeBeanInfo((clz2, argInfo) -> {
					ClassDocInfo classDocInfo = JavaDocParser.CACHE.get(clz2.getName());

					if (classDocInfo != null) {
						argInfo.fields = classDocInfo.fields;

						if (StringUtils.hasText(argInfo.description))
							argInfo.description += classDocInfo.commentText;
						else
							argInfo.description = classDocInfo.commentText;
					}
				});

				info.setTakeReturnBeanInfo((clz2, returnInfo) -> {
					ClassDocInfo classDocInfo = JavaDocParser.CACHE.get(clz2.getName());

					if (classDocInfo != null)
						returnInfo.fields = classDocInfo.fields;

					if (StringUtils.hasText(returnInfo.description))
						returnInfo.description += " " + classDocInfo.commentText;
					else
						returnInfo.description = classDocInfo.commentText;
				});

				ControllerInfo controllerInfo = info.parse();
				AnnotationResult.put(fullName, controllerInfo);

				mix(fullName, controllerInfo);

				TestHelper.printJson(controllerInfo);
			}
		}
	}

	/**
	 * 基础信息来自于注解，然后加上来自于 JavaDoc 的信息，合二为一，得到最终结果
	 * 
	 * @param fullName       类全称
	 * @param controllerInfo 最终信息合并到这个对象
	 */
	private static void mix(String fullName, ControllerInfo controllerInfo) {
		ClassDocInfo javaDocInfo = JavaDocParser.CACHE.get(fullName);
		if (javaDocInfo == null)
			return;

		if (StringUtils.hasText(controllerInfo.description))
			controllerInfo.description += javaDocInfo.commentText;
		else
			controllerInfo.description = javaDocInfo.commentText;

		List<Item> methodItems = controllerInfo.items;

		for (Item item : methodItems) {
			String methodName = item.methodName;

			for (MethodInfo mJavaDoc : javaDocInfo.methods) {
				if (mJavaDoc.name.equals(methodName)) { // 方法名称匹配
					if (StringUtils.hasText(item.name))
						item.description = mJavaDoc.commentText;
					else
						item.name = mJavaDoc.commentText;

					for (ArgInfo argInfo : item.args) {// 参数列表的 mix
						for (ParameterInfo pInfo : mJavaDoc.parameters) {
							if (argInfo.name.equals(pInfo.name)) {

								if (StringUtils.hasText(argInfo.description))
									argInfo.description += " " + pInfo.commentText;
								else
									argInfo.description = pInfo.commentText;
								break;
							}
						}
					}

					break;
				}
			}
		}
	}

	public static String getJsonStr() {
		return JsonHelper.toJson(AnnotationResult);
	}

	public static void saveToDisk(String path) {
		String json = getJsonStr();

		FileHelper.saveText(path, "var DOC_JSON = " + json + ";");
	}
}
