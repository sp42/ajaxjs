/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.ioc;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;

import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.io.resource.AbstractScanner;
import com.ajaxjs.util.io.resource.ScanClass;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

/**
 * Speical for Java Class Scanning
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class BeanLoader extends AbstractScanner<Class<Object>> {
	@Override
	public FileFilter getFileFilter() {
		return ScanClass.fileFilter;
	}

	@Override
	public void onFileAdding(Set<Class<Object>> target, File resourceFile, String packageJavaName) {
		String resourcePath = ScanClass.getClassName(resourceFile, packageJavaName);
		onJarAdding(target, resourcePath);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onJarAdding(Set<Class<Object>> target, String resourcePath) {
		ClassPool cp = ClassPool.getDefault();

		try {
			CtClass cc = cp.get(resourcePath);
			doAop(cc);
			makeSetter(cc);
//			if ("com.ajaxjs.ioc.Hi".equals(resourcePath)) {
//				CtField f1 = new CtField(cp.get("com.ajaxjs.ioc.Person"), "person", cc);
//				cc.addMethod(CtNewMethod.setter("setPerson", f1));
//			}

			Class<Object> clazz = (Class<Object>) cc.toClass();
			target.add(clazz);
		} catch (CannotCompileException e) {
			Class<Object> clazz = (Class<Object>) ReflectUtil.getClassByName(resourcePath);
			target.add(clazz);
		} catch (NotFoundException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void makeSetter(CtClass cc) throws ClassNotFoundException, CannotCompileException, NotFoundException {
		CtField[] fields = cc.getDeclaredFields();
		for (CtField field : fields) {
			if (field.getAnnotation(Resource.class) != null) {
				String setMethodName = "set" + ReflectUtil.firstLetterUpper(field.getName());
				CtMethod setter;

				try {
					setter = cc.getDeclaredMethod(setMethodName, new CtClass[] { field.getType() });
				} catch (NotFoundException e) {
					// 另外一种写法
//					String tpl = "public void %s(%s %s) { this.%s = %s; }";
//					String m = String.format(tpl, setMethodName, field.getType().getName(), field.getName(), field.getName(), field.getName());
//					setter = CtNewMethod.make(m, cc);
//					System.out.println(setMethodName);

					CtField f1 = new CtField(field.getType(), field.getName(), cc);
					setter = CtNewMethod.setter(setMethodName, f1);
					cc.addMethod(setter);
				}
			}
		}
	}

	/**
	 * 
	 * @param cc
	 * @throws ClassNotFoundException
	 * @throws CannotCompileException
	 */
	private static void doAop(CtClass cc) throws ClassNotFoundException, CannotCompileException {
		CtMethod[] methods = cc.getMethods();
		if (methods != null && methods.length > 0) {
			
			for (CtMethod method : methods) {
				if (method.getAnnotation(Before.class) != null) {
					Before before = (Before) method.getAnnotation(Before.class);
					String beforeMethod = before.value().getName() + "." + before.methodName(); // static method only
					method.insertBefore(String.format("{ $args = %s($args); }", beforeMethod));
				}

				if (method.getAnnotation(After.class) != null) {
					After after = (After) method.getAnnotation(After.class);
					String afterMethod = after.value().getName() + "." + after.methodName(); // static method only
					method.insertAfter(String.format("{ return %s($_); }", afterMethod));
				}
			}
		}
	}

	/**
	 * 输入包名，获取所有的 classs
	 * 
	 * @param packageJavaName 包名
	 * @return 结果
	 */
	public static Set<Class<Object>> scanClass(String packageJavaName) {
		return new BeanLoader().scan(packageJavaName);
	}

	/**
	 * 输入多个包名，获取所有的 class。多个 set 可以用 addAll 合并之
	 * 
	 * @param packageJavaNames 包名
	 * @return 结果
	 */
	public static Set<Class<Object>> scanClass(String... packageJavaNames) {
		Set<Class<Object>> classes = null;
		BeanLoader scanner = new BeanLoader();

		for (String packageJavaName : packageJavaNames) {
			if (classes == null) {
				classes = scanner.scan(packageJavaName);
			} else {
				classes.addAll(classes);
			}
		}

		return classes;
	}

}
