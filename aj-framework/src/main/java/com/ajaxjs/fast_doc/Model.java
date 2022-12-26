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

	public static class ControllerInfo extends CommonValue {
		public List<Item> items;
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

	public static class Item implements Comparable<Item> {
		public String id;

		public String name;

		public String description;

		public String methodName;

		public List<ArgInfo> args;

		public Return returnValue;

		public String httpMethod;

		public String url;

		public String image;

		@Override
		public int compareTo(Item o) {
			return url.compareTo(o.url);
		}
	}

	public static class ArgInfo extends CommonValue {
		public String position;

		public boolean isRequired;

		public String defaultValue;

		public String example;

		/**
		 * 如果参数是一个 bean，这里说明 bean 的各个字段
		 */
		public BeanInfo bean;
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
