package com.ajaxjs.fast_doc;

import java.util.List;

public interface Model {
	/**
	 * 
	 */
	public static class Value {
		public String name;
		public String desc;
		public String type;
		public boolean isRequired;
	}

	public static class Item {
		public String methodName;

		public List<Arg> args;

		public Return returnValue;
	}

	public static class Arg {
		public String name;
	}

	public static class Return {
		public String name;

		public String comment;

		public String fullName;
//		public Class<?> clz;
		public boolean isMany;

		public boolean isObject;

		public List<Value> values;
	}
}
