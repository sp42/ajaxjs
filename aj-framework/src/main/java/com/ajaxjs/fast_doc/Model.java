package com.ajaxjs.fast_doc;

import java.util.List;

public interface Model {
	/**
	 * 基础类
	 */
	static abstract class CommonValue {
		public String name;

		public String description;

		public String type;
	}

	public class BeanInfo extends CommonValue {
		public List<Value> values;

		/**
		 * 不是 simple value，是对象
		 */
		public List<BeanInfo> beans;
	}

	/**
	 * 
	 */
	public static class Value extends CommonValue {
		public boolean isRequired;
	}

	public static class Item {
		public String id;

		public String name;

		public String description="fooooooooooooo";

		public String methodName;

		public List<Arg> args;

		public Return returnValue;

		public String httpMethod;

		public String url;

		public String image;
	}

	public static class Arg extends CommonValue {
		public String position;

		public boolean isRequired;

		public String defaultValue;

		public String example;
	}

	public static class Return extends CommonValue {
		public String comment;

//		public Class<?> clz;
		public boolean isMany;

		public boolean isObject;

		public List<Value> values;

		/**
		 * 不是 simple value，是对象
		 */
		public List<BeanInfo> beans;

		/**
		 * 例子
		 */
		public String example;
	}
}
