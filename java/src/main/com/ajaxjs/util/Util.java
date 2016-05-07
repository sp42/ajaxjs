package com.ajaxjs.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;

import com.ajaxjs.Constant;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Util {
	/**
	 * 判断数组是否有意义
	 * 
	 * @param arr
	 *            输入的数组
	 * @return true 表示为素组不是为空，是有内容的，false 表示为数组为空数组，length = 0
	 */
	public static boolean isNotNull(Object[] arr) {
		return arr != null && arr.length > 0;
	}
	
	/**
	 * 判断 collection 是否有意义
	 * 
	 * @param collection
	 *            Map输入的集合
	 * @return true 表示为集合不是为空，是有内容的，false 表示为空集合
	 */
	public static boolean isNotNull(Collection<?> collection) {
		return collection != null && !collection.isEmpty();
	}

	/**
	 * 判断 map 是否有意义
	 * 
	 * @param map
	 *            输入的
	 * @return true 表示为 map 不是为空，是有内容的，false 表示为空 map
	 */
	public static boolean isNotNull(Map<?, ?> map) {
		return map != null && !map.isEmpty();
	}
	
	/**
	 * 强类型转换，有 null 检测
	 * 
	 * @param obj输入的对象
	 * @param clazz
	 *            目标类型
	 * @return T型结果
	 */
	@SuppressWarnings("unchecked")
	public static <T> T TypeConvert(Object obj, Class<T> clazz) {
		return obj == null ? null : (T) obj;
	}
	
	/**
	 * 强类型转换为布尔型
	 * 
	 * @param obj
	 *            输入的对象
	 * @return 布尔型结果
	 */
	public static boolean toBoolean(Object obj) {
		return TypeConvert(obj, Boolean.class);
	}

	/**
	 * 强类型转换为整型
	 * 
	 * @param obj
	 *            输入的对象
	 * @return 整型结果
	 */
	public static int toInt(Object obj) {
		return TypeConvert(obj, Integer.class);
	}

	/**
	 * 强类型转换为日期型
	 * 
	 * @param obj
	 *            输入的对象
	 * @return 日期型结果
	 */
	public static Date toDate(Object obj) {
		return TypeConvert(obj, Date.class);
	}

	/**
	 * 强类型转换为字符串型
	 * 
	 * @param obj
	 *            输入的对象
	 * @param isUserToString
	 *            是否使用 toString() 转换
	 * @return 字符串型
	 */
	public static String to_String(Object obj, boolean isUserToString) {
		if (obj != null) {
			return isUserToString ? obj.toString() : TypeConvert(obj, String.class);
		} else
			return null;
	}
	
	/**
	 * 强类型转换为字符串型（使用强类型转换而不是 toString()）
	 * @param obj
	 * @return
	 */
	public static String to_String(Object obj){
		return to_String(obj, false);
	}
	
	/**
	 * 获取当前类所在的目录下的一个资源
	 * @param cls
	 * @param fileName
	 * @return
	 */
	public static String getClassFolder_FilePath(Class<?> cls, String fileName){
		return StringUtil.urlDecode(cls.getResource(fileName).getPath());
	}
	
	/**
	 * BASE64 编码
	 * @param str
	 * @return
	 */
	public static String base64Encode(String str) {
		return new BASE64Encoder().encode(str.getBytes());
	}

	/**
	 * BASE64 解码
	 * 这里需要强制捕获异常。 中文乱码：http://s.yanghao.org/program/viewdetail.php?i=54806
	 * @param rawBase64
	 * @return
	 */
	public static String base64Decode(String rawBase64) {
		String str = null;
		
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] bytes = null;
		
		try {
			bytes = decoder.decodeBuffer(rawBase64);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(bytes != null)str = StringUtil.byte2String(bytes);
		
		return str;
	}
	
	/**
	 * 获取 字符串 md5 hash
	 * @param str
	 * @return
	 */
	public static String md5(String str){
		byte[] b = null;
		
		try {
			b = str.getBytes(Constant.encoding_UTF8);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		if(b != null)
			try {
				return DatatypeConverter.printHexBinary(MessageDigest.getInstance("MD5").digest(b));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				return null;
			} 
		else return null;
	}
	
	private static final String text = "0123456789abcdefghijklmnopqrstuvwxyz";  
    
	/**
	 * 生成随机密码。 可以控制生成的密码长度， 密码由数字和字母组成。
	 *  //生成8位随机密码  
        System.out.println(generate(8));  
        System.out.println(generate(8))
	 * @param length
	 * @return
	 */
    public synchronized static String passwordGenerator (int length){  
        StringBuffer sb = new StringBuffer();  
        Random random = new Random();  
        
        for (int i = 0; i < length; i++)sb.append(text.charAt(random.nextInt(text.length())));  
        
        return sb.toString();  
    }  
}
