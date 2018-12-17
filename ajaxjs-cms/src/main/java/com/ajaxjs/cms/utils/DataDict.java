package com.ajaxjs.cms.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据字典，常量
 * 
 * @author Frank Cheung
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public interface DataDict {
	public static final int PIC_NORMAL = 1;
	public static final int PIC_COVER = 2;
	public static final int PIC_in_GALLERY = 3;

	public static final Map<Integer, String> picMap = new HashMap() {
		private static final long serialVersionUID = -1L;
		{
			put(PIC_NORMAL, "普通图片");
			put(PIC_COVER, "头像/封面");
			put(PIC_in_GALLERY, "相册图片");
		}
	};
}
