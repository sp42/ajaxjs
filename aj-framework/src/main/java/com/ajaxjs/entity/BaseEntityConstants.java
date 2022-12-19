package com.ajaxjs.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public interface BaseEntityConstants {
	public static final Map<Integer, String> STATE = new HashMap<Integer, String>() {
		private static final long serialVersionUID = -873485978038563365L;

		{
			put(0, "正常");
			put(1, "禁用");
			put(2, "已删除");
		}
	};

	public static final Date NULL_DATE = new Date(0);
}
