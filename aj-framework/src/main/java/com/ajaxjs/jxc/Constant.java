package com.ajaxjs.jxc;

import com.ajaxjs.util.ReflectUtil;

public class Constant {
	public static class DataDict {
		public static final int 计量单位主文件 = 1;
		public static final int 货币主文件 = 2;
		public static final int 暂停原因 = 3;
	}

	public static void main(String[] args) {
		System.out.println(ReflectUtil.getConstantsInt(Constant.DataDict.class));
	}
}
