package com.ajaxjs.fast_doc;

import java.io.File;
import java.io.FileFilter;
import java.util.*;

import com.ajaxjs.fast_doc.annotation.SpringMvcAnnotationParser;
import org.springframework.util.StringUtils;

import com.ajaxjs.fast_doc.Model.ArgInfo;
import com.ajaxjs.fast_doc.Model.CommonValue;
import com.ajaxjs.fast_doc.Model.ControllerInfo;
import com.ajaxjs.fast_doc.Model.Item;
import com.ajaxjs.fast_doc.annotation.CustomAnnotationParser;
import com.ajaxjs.fast_doc.doclet.DocModel.ClassDocInfo;
import com.ajaxjs.fast_doc.doclet.DocModel.MethodInfo;
import com.ajaxjs.fast_doc.doclet.DocModel.ParameterInfo;
import com.ajaxjs.fast_doc.doclet.DocModel.WithComment;
import com.ajaxjs.fast_doc.doclet.JavaDocParser;
import com.ajaxjs.util.TestHelper;
import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.map.JsonHelper;

/**
 * FastDoc 主程序，单例
 *
 * @author Frank Cheung<sp42@qq.com>
 */
public class FastDoc {
	static Map<String, ControllerInfo> AnnotationResult = new HashMap<>();

	/**
	 * 用 JavaDocParser 得到 Bean 的注释文档
	 *
	 * @param dir    源码磁盘目录
	 * @param clazzs Bean 类，其实任意 Java 类都可以，包括控制器
	 */
	public static void loadBeans(String dir, Class<?>... clazzs) {
		Params params = new Params();
		params.sources = new ArrayList<>();

		for (Class<?> clz : clazzs) {
			String name = Util.className2JavaFileName(clz);
			params.sources.add(dir + name);
		}

		JavaDocParser.init(params);
	}

	/**
	 * @param dir    源码磁盘目录
	 * @param clazzs 控制器类列表
	 */
	public static void loadControllersDoc(String dir, Class<?>... clazzs) {
		loadControllersDoc(CustomAnnotationParser.class, dir, clazzs);
	}

	public static void loadControllersDoc(Class<? extends SpringMvcAnnotationParser> parserClz, String dir, Class<?>... clazzs) {
		loadBeans(dir, clazzs);

		for (Class<?> clz : clazzs) {
			String fullName = clz.getName();

			if (!AnnotationResult.containsKey(fullName)) {
				SpringMvcAnnotationParser info = null;

				try {
					info = parserClz.newInstance();
				} catch (IllegalAccessException | InstantiationException e) {
					e.printStackTrace();
				}

				if (info == null)
					info = new CustomAnnotationParser();

				info.setClz(clz);
				info.setTakeBeanInfo((clz2, argInfo) -> {
					String fullArgClzName = getInnerClzFullName(clz2);
					ClassDocInfo classDocInfo = JavaDocParser.CACHE.get(fullArgClzName);

					if (classDocInfo != null) {
						argInfo.fields = classDocInfo.fields;
						setDescByComment(argInfo, classDocInfo);
					}
				});

				info.setTakeReturnBeanInfo((clz2, returnInfo) -> {
					String fullReturnName = getInnerClzFullName(clz2);
					ClassDocInfo classDocInfo = JavaDocParser.CACHE.get(fullReturnName);

					if (classDocInfo != null)
						returnInfo.fields = classDocInfo.fields;

					setDescByComment(returnInfo, classDocInfo);
				});

				ControllerInfo controllerInfo = info.parse();
				AnnotationResult.put(fullName, controllerInfo);

				mix(fullName, controllerInfo);

				TestHelper.printJson(controllerInfo);
			}
		}
	}

	/**
	 * JavaDoc 对于内部类也可以正常解析。但类全称不是 $ 区分的，而是 xxx.yyy，于是这里要统一一下
	 *
	 * @param clz2
	 * @return
	 */
	private static String getInnerClzFullName(Class<?> clz2) {
		String fullReturnName = clz2.getName();

		if (fullReturnName.contains("$"))
			fullReturnName = fullReturnName.replaceAll("\\$", ".");

		return fullReturnName;
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
								setDescByComment(argInfo, pInfo);
								break;
							}
						}
					}

					break;
				}
			}
		}
	}

	private static void setDescByComment(CommonValue info, WithComment info2) {
		if (info2 == null)
			return;

		if (StringUtils.hasText(info.description))
			info.description += " " + info2.commentText;
		else
			info.description = info2.commentText;
	}

	public static String getJsonStr() {
		return JsonHelper.toJson(AnnotationResult);
	}

	public static void saveToDisk(String path) {
		String json = getJsonStr();

		FileHelper.saveText(path, "var DOC_JSON = " + json + ";");
	}

	public static Class<?>[] findAndAddClassesInPackageByFile(String packageName, String packagePath) {
		Set<Class<?>> classes = new LinkedHashSet<>();
		findAndAddClassesInPackageByFile(packageName, packagePath, true, classes);

		Class<?>[] arr = new Class[classes.size()];

		int i = 0;
		for (Class<?> clz : classes)
			arr[i++] = clz;

		return arr;
	}

	/**
	 * 以文件的形式来获取包下的所有Class
	 *
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, boolean recursive, Set<Class<?>> classes) {
		// 获取此包的目录 建立一个File
		File dir = new File(packagePath);
		// 如果不存在或者 也不是目录就直接返回
		if (!dir.exists() || !dir.isDirectory()) {
//            log.warn("用户定义包名 " + packageName + " 下没有任何文件");
			return;
		}

		// 如果存在 就获取包下的所有文件 包括目录
		File[] dirfiles = dir.listFiles(new FileFilter() {
			// 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
			public boolean accept(File file) {
				return (recursive && file.isDirectory()) || (file.getName().endsWith(".java"));
			}
		});
		// 循环所有文件
		for (File file : dirfiles) {
			// 如果是目录 则继续扫描
			if (file.isDirectory()) {
				findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
			} else {
				// 如果是java类文件 去掉后面的.class 只留下类名
				String className = file.getName().substring(0, file.getName().length() - 5);
				try {
					// 添加到集合中去
					classes.add(Class.forName(packageName + '.' + className));
				} catch (ClassNotFoundException e) {
//                    log.error("添加用户自定义视图类错误 找不到此类的.class文件");
					e.printStackTrace();
				}
			}
		}
	}
}
