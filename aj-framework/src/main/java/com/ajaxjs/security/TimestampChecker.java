package com.ajaxjs.security;

/**
 * 时间戳检查
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class TimestampChecker {
	private int overLimitMins;

	public boolean check(String value) {
		return isOver(value, overLimitMins);
	}

	/**
	 * 
	 * @param thatTime
	 * @param overLimitMins
	 * @return true = 时间戳超时
	 */
	public boolean isOver(String thatTime, int overLimitMins) {
		return isOver(Long.parseLong(thatTime), overLimitMins);
	}

	/**
	 * 
	 * @param thatTime
	 * @param overLimitMins
	 * @return true = 时间戳超时
	 */
	public boolean isOver(long thatTime, int overLimitMins) {
		long diff = getNowTimestamp() - thatTime;

		return diff > (overLimitMins * 60);
	}

	/**
	 * 返回时间戳
	 * 
	 * @return 时间戳
	 */
	public static long getNowTimestamp() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * 返回时间戳字符串
	 * 
	 * @return 时间戳字符串
	 */
	public static String getNowTimestampStr() {
		return getNowTimestamp() + "";
	}

	public static void main(String[] args) throws InterruptedException {
		TimestampChecker t = new TimestampChecker();
		long ago = TimestampChecker.getNowTimestamp();

		Thread.sleep(61 * 1000);

		System.out.println(t.isOver(ago, 1));
//		assertTrue(!t.isOver(ago, 1));

	}

	public int getOverLimitMins() {
		return overLimitMins;
	}

	public void setOverLimitMins(int overLimitMins) {
		this.overLimitMins = overLimitMins;
	}
}
