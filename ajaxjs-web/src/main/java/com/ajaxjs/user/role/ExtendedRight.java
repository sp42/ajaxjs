package com.ajaxjs.user.role;

/**
 * 扩展的权限列表，通过 getRightInfos() 返回列表数组
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public interface ExtendedRight {
	public RightInfo[] getRightInfos();

	/**
	 * 权限信息
	 * 
	 * @author sp42 frank@ajaxjs.com
	 *
	 */
	public static class RightInfo {
		private int value;
		private String description;

		/**
		 * 创建一个权限信息
		 * 
		 * @param value       权限值
		 * @param description 权限描述
		 */
		public RightInfo(int value, String description) {
			this.value = value;
			this.description = description;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}
}
