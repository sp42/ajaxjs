package com.batch_read.util;

import java.net.*;
import java.util.*;
import java.security.*;
import java.text.SimpleDateFormat;

public class GuidCreator {

	private static int cnt = 0;

	public static synchronized String getGUID() throws Exception {
		StringBuffer code = new StringBuffer();
		try {
			java.util.Date dt = new java.util.Date(System.currentTimeMillis());
			SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmssSSS");// format system time
			String randomCode = fmt.format(dt);
			cnt = (cnt + 1) % 10000; // You are free the set %100 to
			// 1000,100000
			code.append(randomCode).append(cnt);
			return code.toString();
		} catch (Exception e) {
			throw new Exception("createFileName error:" + e.getMessage(), e);
		}
	}

	protected static int count = 0;

	public static synchronized String getUUID() {
		count++;
		long time = System.currentTimeMillis();

		String timePattern = Long.toHexString(time);
		int leftBit = 14 - timePattern.length();
		if (leftBit > 0) {
			timePattern = "0000000000".substring(0, leftBit) + timePattern;
		}

		String uuid = timePattern + Long.toHexString(Double.doubleToLongBits(Math.random())) + Long.toHexString(Double.doubleToLongBits(Math.random()))
				+ "000000000000000000";

		uuid = uuid.substring(0, 32).toUpperCase();

		return uuid;
	}

	private String seedingString = "";
	private String rawGUID = "";
	private boolean bSecure = false;
	private static Random myRand;
	private static SecureRandom mySecureRand;

	private static String s_id;

	public static final int BeforeMD5 = 1;
	public static final int AfterMD5 = 2;
	public static final int FormatString = 3;
	static {
		mySecureRand = new SecureRandom();
		long secureInitializer = mySecureRand.nextLong();
		myRand = new Random(secureInitializer);
		try {
			s_id = InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public GuidCreator() {
	}

	/*
	 * Constructor with security option. Setting secure true enables each random
	 * number generated to be cryptographically strong. Secure false defaults to the
	 * standard Random function seeded with a single cryptographically strong random
	 * number.
	 */
	public GuidCreator(boolean secure) {
		bSecure = secure;
	}

	/*
	 * Method to generate the random GUID
	 */
	private void getRandomGUID(boolean secure) {
		MessageDigest md5 = null;
		StringBuffer sbValueBeforeMD5 = new StringBuffer();

		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Error: " + e);
		}

		try {
			long time = System.currentTimeMillis();
			long rand = 0;

			if (secure) {
				rand = mySecureRand.nextLong();
			} else {
				rand = myRand.nextLong();
			}

			// This StringBuffer can be a long as you need; the MD5
			// hash will always return 128 bits. You can change
			// the seed to include anything you want here.
			// You could even stream a file through the MD5 making
			// the odds of guessing it at least as great as that
			// of guessing the contents of the file!
			sbValueBeforeMD5.append(s_id);
			sbValueBeforeMD5.append(":");
			sbValueBeforeMD5.append(Long.toString(time));
			sbValueBeforeMD5.append(":");
			sbValueBeforeMD5.append(Long.toString(rand));

			seedingString = sbValueBeforeMD5.toString();
			md5.update(seedingString.getBytes());

			byte[] array = md5.digest();
			StringBuffer sb = new StringBuffer();
			for (int j = 0; j < array.length; ++j) {
				int b = array[j] & 0xFF;
				if (b < 0x10)
					sb.append('0');
				sb.append(Integer.toHexString(b));
			}

			rawGUID = sb.toString();

		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
	}

	public String createNewGuid(int nFormatType, boolean secure) {
		getRandomGUID(secure);
		String sGuid = "";
		if (BeforeMD5 == nFormatType) {
			sGuid = this.seedingString;
		} else if (AfterMD5 == nFormatType) {
			sGuid = this.rawGUID;
		} else {
			sGuid = this.toString();
		}
		return sGuid;
	}

	public String createNewGuid(int nFormatType) {
		return this.createNewGuid(nFormatType, this.bSecure);
	}

	/*
	 * Convert to the standard format for GUID (Useful for SQL Server
	 * UniqueIdentifiers, etc.) Example: C2FEEEAC-CFCD-11D1-8B05-00600806D9B6
	 */
	public String toString() {
		String raw = rawGUID.toUpperCase();
		StringBuffer sb = new StringBuffer();
		sb.append(raw.substring(0, 8));
		sb.append("-");
		sb.append(raw.substring(8, 12));
		sb.append("-");
		sb.append(raw.substring(12, 16));
		sb.append("-");
		sb.append(raw.substring(16, 20));
		sb.append("-");
		sb.append(raw.substring(20));

		return sb.toString();
	}

	public static void main(String args[]) {
		GuidCreator myGUID = new GuidCreator();
//		System.out.println("Seeding String="
//				+ myGUID.createNewGuid(GuidCreator.BeforeMD5));
//		System.out.println("rawGUID="
//				+ myGUID.createNewGuid(GuidCreator.AfterMD5));
		System.out.println("RandomGUID=" + myGUID.createNewGuid(GuidCreator.AfterMD5));
	}
}