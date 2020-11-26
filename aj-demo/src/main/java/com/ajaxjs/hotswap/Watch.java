package com.ajaxjs.hotswap;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;

import com.ajaxjs.hotswap.compiler.CompileHelper;

//import com.jfirer.mvc.core.ActionCenterBuilder;

public class Watch {
	private final FileChangeDetect detect;
	private final String reloadPath;
	private final String reloadPackages;
	private final String excludePackages;
	private ServletContext servletContext;

	public Watch(String monitorPath, String reloadPath, String reloadPackages, String excludePackages) {
		// logger.debug("使用热加载方式进行启动");
		List<File> roots = new LinkedList<>();
		for (String each : monitorPath.split(","))
			roots.add(new File(each));

		detect = new FileChangeDetect(roots.toArray(new File[roots.size()]));
		this.reloadPath = reloadPath;
		this.reloadPackages = reloadPackages;

		this.excludePackages = excludePackages;

	}

	public void preHandle() {
		if (true) {
			long t0 = System.currentTimeMillis();
			SimpleHotswapClassLoader classLoader = new SimpleHotswapClassLoader(reloadPath);
			classLoader.setReloadPackages(reloadPackages.split(","));

			if (excludePackages != null)
				classLoader.setExcludePackages(excludePackages.split(","));

			// actionCenter = ActionCenterBuilder.generate(classLoader, servletContext,
			// configClassName);

			StringBuilder sourceCode = new StringBuilder();
			sourceCode.append("package com.ajaxjs;\n");
			sourceCode.append("public class Foo {\n");
			sourceCode.append("   public int bar = 8888;\n");
			sourceCode.append("}");

			try {
				Class<?> clz = new CompileHelper(classLoader).compile(sourceCode.toString(), "file://C:/sp42/dev/"
						+ "eclipse-workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/ajaxjs-demo/WEB-INF/classes/com/ajaxjs/Foo.java", "Foo", "com.ajaxjs");

				try {
					Object obj = clz.newInstance();

					Field[] fields = clz.getDeclaredFields();

					// 获取实体字段集合
					for (Field f : fields) {// 通过反射获取该属性对应的值
						System.out.println(f.get(obj));
					}
					
					
				} catch (InstantiationException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}

			System.out.println("热部署,耗时:{}" + (System.currentTimeMillis() - t0));
			// logger.debug("热部署,耗时:{}", System.currentTimeMillis() - t0);
		}
	}
}