package com.ajaxjs.util;

public class UUID {
	/**
	 * 看最后一个是不是 uuid
	 * 
	 * @return
	 */
//	public static boolean isUUID(String url) {
//		String js = "'%s'.match(/\\w{8}(?:-\\w{4}){3}-\\w{12}/) != null;";
//		js = String.format(js, url);
//		boolean isUUID = App.jsRuntime.eval_return_Boolean(js);
//		return isUUID;
//	}
	
	/**
	 * 为数据库获取一个唯一的主键 id 的代码 
	 * @return UUID
	 */
	public static String get(){
		return java.util.UUID.randomUUID().toString();
	}
	
	/**
	 * 去掉“-”符号 
	 * @param uuid
	 * @return
	 */
	public static String remove(String uuid){
		  return uuid.substring(0, 8) + uuid.substring(9, 13)+ uuid.substring(14, 18) + uuid.substring(19, 23) + uuid.substring(24); 
	}
	
	public static final String uuid_regExp = "[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}";

	/**
	 * 判断输入的字符串是否包含 uuid 特征。
	 * @param str
	 * @return
	 */
	public static boolean isUUID(String str) {
		return StringUtil.regMatch(uuid_regExp, str) != null;
	}
}
