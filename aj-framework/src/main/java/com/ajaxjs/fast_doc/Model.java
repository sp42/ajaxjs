package com.ajaxjs.fast_doc;

import java.util.List;

import com.ajaxjs.fast_doc.doclet.DocModel.FieldInfo;

public interface Model {
	/**
	 * 基础类
	 */
	static abstract class CommonValue {
		public String name;

		public String description;

		public String type;
	}

	/**
	 * 一个完整的控制器信息
	 * 
	 * @author Frank Cheung<sp42@qq.com>
	 *
	 */
	public static class ControllerInfo extends CommonValue {
		/**
		 * 各个控制器方法的信息
		 */
		public List<Item> items;
	}

	/**
	 * 控制器方法的信息
	 * 
	 * @author Frank Cheung<sp42@qq.com>
	 *
	 */
	public static class Item implements Comparable<Item> {
		public String id;

		public String name;

		public String description;

		public String methodName;

		public String httpMethod;

		public String url;

		public String image;

		public List<ArgInfo> args;

		public Return returnValue;

		@Override
		public int compareTo(Item o) {
			return url.compareTo(o.url);
		}
	}

	/**
	 * 参数信息
	 * 
	 * @author Frank Cheung<sp42@qq.com>
	 *
	 */
	public static class ArgInfo extends CommonValue {
		/**
		 * 参数的位置，也就是参数的类型
		 */
		public String position;

		/**
		 * 是否必填
		 */
		public boolean isRequired;

		/**
		 * 默认值
		 */
		public String defaultValue;

		/**
		 * 例子
		 */
		public String example;

		/**
		 * 如果参数是一个 bean，这里说明 bean 的各个字段
		 */
		public FieldInfo[] fields;
	}

	/**
	 * 返回值信息
	 * 
	 * @author Frank Cheung<sp42@qq.com>
	 *
	 */
	public static class Return extends CommonValue {
		/**
		 * 返回值是否 List或 Array
		 */
		public boolean isMany;

		/**
		 * true = JavaBean/false = String/Boolean/Number
		 */
		public boolean isObject;

		/**
		 * 例子
		 */
		public String example;

		/**
		 * 如果返回值是一个 bean，这里说明 bean 的各个字段
		 */
		public FieldInfo[] fields;
	}
}
