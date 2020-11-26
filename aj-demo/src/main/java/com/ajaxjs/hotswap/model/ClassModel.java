package com.ajaxjs.hotswap.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ajaxjs.hotswap.SmcHelper;

public class ClassModel {
	private String packageName = "com.jfireframe.smc.output";
	private String className;
	private Map<String, FieldModel> fieldStore = new HashMap<String, FieldModel>();
	private Map<MethodModel.MethodModelKey, MethodModel> methodStore = new HashMap<MethodModel.MethodModelKey, MethodModel>();
	private Set<String> constructors = new HashSet<String>();
	private Set<Class<?>> imports = new HashSet<Class<?>>();
	private Set<Class<?>> interfaces = new HashSet<Class<?>>();
	private Set<String> classSimpleName = new HashSet<String>();
	private Class<?> parentClass;

	public ClassModel(String className) {
		this.className = className;
	}

	public ClassModel(String className, Class<?> parentClass, Class<?>... interCc) {
		this.className = className;
		this.parentClass = parentClass;
		for (Class<?> each : interCc) {
			interfaces.add(each);
		}
	}

	private String buildClassDefinition() {
		StringBuilder cache = new StringBuilder();
		if (parentClass == null || parentClass == Object.class) {
			cache.append("public class ").append(className);
		} else {
			cache.append("public class ").append(className).append(" extends ").append(SmcHelper.getReferenceName(parentClass, this));
		}
		if (interfaces.isEmpty() == false) {
			cache.append(" implements ");//
			boolean hasComma = false;
			for (Class<?> each : interfaces) {
				cache.append(SmcHelper.getReferenceName(each, this)).append(',');
				hasComma = true;
			}
			if (hasComma) {
				cache.setLength(cache.length() - 1);
			}
		}
		cache.append(" \r\n{\r\n");
		return cache.toString();
	}

	public void addInterface(Class<?> intercc) {
		addImport(intercc);
		interfaces.add(intercc);
	}

	/**
	 * 添加类型到类的导入列表。如果返回false意味着导入不成功，则后续不可以使用类的简单名称
	 *
	 * @param type
	 * @return
	 */
	public boolean addImport(Class<?> type) {
		// 如果是已经存在的，则等同于添加成功
		if (imports.contains(type)) {
			return true;
		}
		// 原始类型无需添加引用即可直接使用
		if (type.isPrimitive()) {
			return true;
		}
		if (type.isArray()) {
			while (type.isArray()) {
				type = type.getComponentType();
			}
			if (type.isPrimitive()) {
				return true;
			}
		}
		if (classSimpleName.add(type.getSimpleName()) == false) {
			return false;
		}
		imports.add(type);
		return true;
	}

	public void addConstructor(ConstructorModel... models) {
		for (ConstructorModel each : models) {
			constructors.add(each.toString());
		}
	}

	public void addConstructor(String initStr, Class<?>... params) {
		StringBuilder cache = new StringBuilder();
		cache.append("public ").append(className);
		if (params.length == 0) {
			cache.append("()\r\n{");
		} else {
			cache.append("(");
			for (int i = 0; i < params.length; i++) {
				cache.append(SmcHelper.getReferenceName(params[i], this)).append(" ");
				cache.append("$").append(i).append(",");
			}
			cache.setLength(cache.length() - 1);
			cache.append(")\r\n{");
		}
		cache.append(initStr).append("}\r\n");
		constructors.add(cache.toString());
	}

	public void putMethodModel(MethodModel methodModel) {
		methodStore.put(methodModel.generateKey(), methodModel);
	}

	public MethodModel getMethodModel(MethodModel.MethodModelKey key) {
		return methodStore.get(key);
	}

	public MethodModel removeMethodModel(MethodModel.MethodModelKey key) {
		return methodStore.remove(key);
	}

	public String fileName() {
		return className + ".java";
	}

	public String className() {
		return className;
	}

	public void addField(FieldModel... models) {
		for (FieldModel each : models) {
			fieldStore.put(each.getName(), each);
			addImport(each.getType());
		}
	}

	public Collection<MethodModel.MethodModelKey> methods() {
		return methodStore.keySet();
	}

	@Override
	public String toString() {
		StringBuilder cache = new StringBuilder();
		cache.append(buildClassDefinition());
		for (String constructor : constructors) {
			cache.append('\t').append(constructor);
		}
		for (FieldModel each : fieldStore.values()) {
			cache.append('\t').append(each.toString());
		}
		for (MethodModel each : methodStore.values()) {
			cache.append('\t').append(each.toString());
		}
		cache.append("}");
		String content = cache.toString();
		cache.setLength(0);
		cache.append("package ").append(packageName).append(';').append("\r\n");
		// 将import的部分放在最后，以防止属性或者方法在输出的时候又增加了新的导入项
		for (Class<?> each : imports) {
			cache.append("import ").append(getClassName(each)).append(";\r\n");
		}
		cache.append(content);
		return cache.toString();
	}

	private String getClassName(Class<?> type) {
		if (type.isArray() == false) {
			return type.getName().replace('$', '.');
		} else {
			StringBuilder cache = new StringBuilder();
			while (type.isArray()) {
				cache.append("[]");
				type = type.getComponentType();
			}
			return type.getName().replace('$', '.') + cache.toString();
		}
	}

	public String toStringWithLineNo() {
		String source = toString();
		String[] tmp = source.split("\r\n");
		StringBuilder cache = new StringBuilder(source.length());
		int no = 1;
		for (String each : tmp) {
			if (no < 10) {
				cache.append("/*   ").append(no).append(" */  ");
			} else if (no < 100) {
				cache.append("/*  ").append(no).append(" */  ");
			} else if (no < 1000) {
				cache.append("/* ").append(no).append(" */  ");
			} else {
				cache.append("/*").append(no).append(" */  ");
			}
			cache.append(each).append("\r\n");
			no += 1;
		}
		return cache.toString();
	}

	public void setParentClass(Class<?> ckass) {
		parentClass = ckass;
	}

	public String getClassName() {
		return className;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
}
