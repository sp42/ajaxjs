package com.ajaxjs.hotswap.model;

import com.ajaxjs.hotswap.SmcHelper;

public class ConstructorModel {
	private final ClassModel classModel;
	private Class[] paramTypes;
	private String[] paramNames;
	private String body;

	public ConstructorModel(ClassModel classModel) {
		this.classModel = classModel;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String[] getParamNames() {
		return paramNames;
	}

	public void setParamNames(String... paramNames) {
		this.paramNames = paramNames;
	}

	public Class[] getParamTypes() {
		return paramTypes;
	}

	public void setParamTypes(Class... paramTypes) {
		this.paramTypes = paramTypes;
	}

	public String toString() {
		StringBuilder cache = new StringBuilder();
		cache.append("public ").append(classModel.getClassName());

		if (paramTypes == null || paramTypes.length == 0) {
			cache.append("()\r\n{");
		} else {
			Boolean miss = null;
			cache.append("(");

			for (int i = 0; i < paramTypes.length; i++) {
				cache.append(SmcHelper.getReferenceName(paramTypes[i], classModel)).append(" ");

				if (miss == null)
					miss = paramNames == null || paramNames.length != paramTypes.length;

				if (miss) {
					cache.append("$").append(i).append(",");
				} else {
					cache.append(paramNames[i]).append(",");
				}
			}

			cache.setLength(cache.length() - 1);
			cache.append(")\r\n{");
		}

		cache.append(body).append("}\r\n");

		return cache.toString();
	}
}