package com.github.chaijunkun.wechat.common.util;

import org.apache.commons.codec.binary.Base64;

/**
 * XXTEA加解密组件
 * @author chaijunkun
 * @since 2016年7月31日
 */
public class XXTEAUtil {

	/**
	 * 使用密钥加密数据
	 * @param plain
	 * @param key
	 * @return
	 */
	public static byte[] encrypt(byte[] plain, byte[] key) {
		if (plain.length == 0) {
			return plain;
		}
		return toByteArray(encrypt(toIntArray(plain, true), toIntArray(key, false)), false);
	}

	/**
	 * 使用密钥解密
	 * @param cipher
	 * @param key
	 * @return
	 */
	public static byte[] decrypt(byte[] cipher, byte[] key) {
		if (cipher.length == 0) {
			return cipher;
		}
		return toByteArray(decrypt(toIntArray(cipher, false), toIntArray(key, false)), true);
	}

	/**
	 * 使用密钥加密数据
	 * @param v
	 * @param k
	 * @return
	 */
	public static int[] encrypt(int[] v, int[] k) {
		int n = v.length - 1;

		if (n < 1) {
			return v;
		}
		if (k.length < 4) {
			int[] key = new int[4];

			System.arraycopy(k, 0, key, 0, k.length);
			k = key;
		}
		int z = v[n], y = v[0], delta = 0x9E3779B9, sum = 0, e;
		int p, q = 6 + 52 / (n + 1);

		while (q-- > 0) {
			sum = sum + delta;
			e = sum >>> 2 & 3;
			for (p = 0; p < n; p++) {
				y = v[p + 1];
				z = v[p] += (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
			}
			y = v[0];
			z = v[n] += (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
		}
		return v;
	}

	/**
	 * 使用密钥解密数据
	 * @param v
	 * @param k
	 * @return
	 */
	public static int[] decrypt(int[] v, int[] k) {
		int n = v.length - 1;

		if (n < 1) {
			return v;
		}
		if (k.length < 4) {
			int[] key = new int[4];

			System.arraycopy(k, 0, key, 0, k.length);
			k = key;
		}
		int z = v[n], y = v[0], delta = 0x9E3779B9, sum, e;
		int p, q = 6 + 52 / (n + 1);

		sum = q * delta;
		while (sum != 0) {
			e = sum >>> 2 & 3;
		for (p = n; p > 0; p--) {
			z = v[p - 1];
			y = v[p] -= (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
		}
		z = v[n];
		y = v[0] -= (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
		sum = sum - delta;
		}
		return v;
	}

	/**
	 * 字节数组转换为整型数组
	 * @param data
	 * @param includeLength
	 * @return
	 */
	private static int[] toIntArray(byte[] data, boolean includeLength) {
		int n = (((data.length & 3) == 0) ? (data.length >>> 2) : ((data.length >>> 2) + 1));
		int[] result;

		if (includeLength) {
			result = new int[n + 1];
			result[n] = data.length;
		} else {
			result = new int[n];
		}
		n = data.length;
		for (int i = 0; i < n; i++) {
			result[i >>> 2] |= (0x000000ff & data[i]) << ((i & 3) << 3);
		}
		return result;
	}

	/**
	 * 整型数组转换为字节数组
	 * @param data
	 * @param includeLength
	 * @return
	 */
	private static byte[] toByteArray(int[] data, boolean includeLength) {
		int n = data.length << 2;
		if (includeLength) {
			int m = data[data.length - 1];

			if (m > n) {
				return null;
			} else {
				n = m;
			}
		}
		byte[] result = new byte[n];

		for (int i = 0; i < n; i++) {
			result[i] = (byte) ((data[i >>> 2] >>> ((i & 3) << 3)) & 0xff);
		}
		return result;
	}

	/**
	 * 先XXXTEA加密，后Base64加密
	 * @param plain
	 * @param key
	 * @return
	 */
	public static String encrypt(String plain, String key) {
		String cipher = "";
		byte[] k = key.getBytes();
		byte[] v = plain.getBytes();
		cipher = new String(Base64.encodeBase64(XXTEAUtil.encrypt(v, k)));
		cipher = cipher.replace('+', '-');
		cipher = cipher.replace('/', '_');
		cipher = cipher.replace('=', '.');
		return cipher;
	}

	/**
	 * 先Base64解密，后XXXTEA解密
	 * @param cipher
	 * @param key
	 * @return
	 */
	public static String decrypt(String cipher, String key) {
		String plain = "";
		cipher = cipher.replace('-', '+');
		cipher = cipher.replace('_', '/');
		cipher = cipher.replace('.', '=');
		byte[] k = key.getBytes();
		byte[] v = Base64.decodeBase64(cipher);
		plain = new String(XXTEAUtil.decrypt(v, k));
		return plain;
	}

}