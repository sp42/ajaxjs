package com.ajaxjs.fast_doc.doclet;

public interface DocModel {
	/**
	 * 带有 commentText 的对象
	 * 
	 * @author Frank Cheung sp42@qq.com
	 *
	 */
	abstract class WithComment {
		public String commentText;
	}

	public static class ClassDocInfo extends WithComment {
		public String name;
		public String fullName;
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

	abstract static class BaseInfo extends WithComment {
		public String name;
		public String type;
		public String typeFullName;
	}

	public static class ParameterInfo extends BaseInfo {
	}

	public static class FieldInfo extends BaseInfo {

	}
}
