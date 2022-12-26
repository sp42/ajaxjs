package com.ajaxjs.fast_doc.doclet;

public interface DocModel {
	public static class ClassDocInfo {
		public String name;
		public String fullName;
		public String commentText;
		public MethodInfo[] methods;
		public FieldInfo[] fields;
	}

	/**
	 * 
	 *
	 */
	public static class MethodInfo {
		public String name;
		public String commentText;
		public String returnType;
		public String returnTypeFullName;
		public String returnComment;
		public ParameterInfo[] parameters;
	}

	abstract static class BaseInfo {
		public String name;
		public String commentText;
		public String type;
		public String typeFullName;

	}

	public static class ParameterInfo extends BaseInfo {
	}

	public static class FieldInfo extends BaseInfo {

	}
}
