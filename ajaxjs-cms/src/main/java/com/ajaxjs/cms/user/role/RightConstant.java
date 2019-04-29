package com.ajaxjs.cms.user.role;

/**
 * 操作权限
 * 
 * @author Frank Cheung
 *
 */
public interface RightConstant {
	public static final int READ = 1;
	public static final int CREATE = 2;
	public static final int UPDATE = 3;
	public static final int DELETE = 4;

	public static final int FRONTEND_ALLOW_ENTNER = 4;

	public static final int ADMIN_SYSTEM_ALLOW_ENTNER = 5;

	public static final int API_ALLOW_ACCESS = 6;

	public static final int ARTICLE_ONLINE = 7;
	public static final int ARTICLE_OFFLINE = 8;
	
	public static final int HR_ONLINE = 9;
	public static final int HR_OFFLINE = 10;
	
	public static final int PRODUCT_ONLINE = 11;
	public static final int PRODUCT_OFFLINE = 12;

	public static final int FEEDBACK = 13;
	
	public static final int TOPIC = 14;
	
	public static final int ADS = 15;
	
	public static final int SECTION = 16;
	
	public static final int WEBSITE = 17;
	
	public static final int GLOBAL_SETTING = 18;
	
	public static final int USER = 19;
	
	public static final int USER_PRIVILEGE = 20;
	
	public static final int DEVELOPER_TOOL = 21;
	
	public static final int SHOP = 22;
	
	public static final int SHOP_SELLER = 23;
	
	/**
	 * 查询num的第pos位权限值
	 * 
	 * @param num 总权限值
	 * @param pos 从右数起第pos位,从0开始
	 * @return 第pos位为1时，返回true，否则返回false
	 */
	public static boolean check(long num, int pos) {
		num >>>= pos;// 右移X位
		return (num & 1) == 1;
	}

	/**
	 * 将num的第pos位设置为v
	 * 
	 * @param num 权限值
	 * @param pos 从右数起第pos位,从0开始
	 * @param v 值
	 */
	public static long set(long num, int pos, boolean v) {
		boolean old = check(num, pos);// 原权限
		
		if (v) {// 期望改为无权限
			if (!old) {// 原来有权限
				num = num + (1L << pos);// 将第pos位设置为1
			}
		} else {// 期望改为有权限
			if (old) {// 原来无权限
				num = num - (1L << pos);// 将第pos位设置为0
			}
		}
		
		return num;
	}

	/**
	 * 调试方法，将long转为二进制，并输出
	 */
	public static void printBinary(long num) {
		long reLong = num;
		// 得到64位值
		byte[] buf = new byte[64];
		int pos = 64;
		
		do {
			buf[--pos] = (byte) (reLong & 1);
			reLong >>>= 1;// 右移一位，相当除以2
		} while (reLong != 0);

		// print
		for (int i = 0; i < buf.length; i++) {
			System.out.print(buf[i]);
		}
		System.out.print("-->" + num + "\r\n");
	}
}
