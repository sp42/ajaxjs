package com.ajaxjs.sql.util.geo;

/**
 * mysql 的 linestring、point 从 jdbc 读入为 byte [] 类型，下面的 java 代码实现逐次读取经纬度数据，返回 double 数组
 *
 * @author <a href="https://my.oschina.net/jingshishengxu/blog/1936212">...</a>
 *
 */
public class MySqlGeoUtils {
	public static double[] bytestoPoints(byte[] arr) {
		if (arr == null)
			return null;

		if (arr.length == 25)
			return bytesToOnePoint(arr);

		return bytesToMutiPoints(arr);
	}

	private static double bytes2Double(byte[] arr, int start) {
		long value = 0;

		for (int i = 0; i < 8; i++)
			value |= ((long) (arr[start + i] & 0xff)) << (8 * i);

		return Double.longBitsToDouble(value);
	}

	public static double[] bytesToOnePoint(byte[] arr) {
		return new double[] { bytes2Double(arr, 9), bytes2Double(arr, 17) };
	}

	private static double[] bytesToMutiPoints(byte[] arr) {
		int len = (arr.length - 13) / 8;
		double[] result = new double[len];

		for (int i = 0; i < len; ++i)
			result[i] = bytes2Double(arr, 13 + i * 8);

		return result;
	}

	/**
	 * 将 double 型经纬度转成 mysql 的 point 类型数据
	 * 
	 * @param lat
	 * @param lng
	 * @return
	 */
	public static byte[] convert2Point(double lat, double lng) {
		byte[] bytelat = double2Bytes(lat);
		byte[] bytelng = double2Bytes(lng);
		byte[] bpoint = new byte[25];
		bpoint[4] = 0x01;
		bpoint[5] = 0x01;

		for (int i = 0; i < 8; ++i) {
			bpoint[9 + i] = bytelng[i];
			bpoint[17 + i] = bytelat[i];
		}

		return bpoint;
	}

	public static byte[] double2Bytes(double d) {
		long value = Double.doubleToRawLongBits(d);
		byte[] byteRet = new byte[8];

		for (int i = 0; i < 8; i++)
			byteRet[i] = (byte) ((value >> 8 * i) & 0xff);

		return byteRet;
	}
}
