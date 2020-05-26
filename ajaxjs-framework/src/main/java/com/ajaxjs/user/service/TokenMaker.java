package com.ajaxjs.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.cryptography.Symmetri_Cipher;

public class TokenMaker {
	public static Function<String, String> encryptAES(String key) {
		return str -> Symmetri_Cipher.AES_Encrypt(str, key);
	}

	public static String addSalt(String str) {
		return str.replace("{salt}", CommonUtil.getRandomString(6));
	}

	public static String addTimespam(String str) {
		long timeStamp = System.currentTimeMillis() / 1000;
		return str.replace("{timeStamp}", timeStamp + "");
	}

	public static Function<String, String> value(String value) {
		return str -> {
			System.out.println(str);
			System.out.println(str.contains("{value}"));
			return str.replace("{value}", value);
		};
	}

	public static String TOKEN_TPL = "email-{value}-{salt}-{timeStamp}";

	public static Predicate<String[]> checkEmail(String email, List<Throwable> ex) {
		return arr -> {
			if (email.equals(arr[1])) {
				return true;
			} else {
				ex.add(new IllegalAccessError("邮件地址不匹配！"));
				return false;
			}
		};
	}

	public static Predicate<String> checkTimespam(int min, List<Throwable> ex) {
		return v -> {
			long timespam = Long.parseLong(v);
			long diff = System.currentTimeMillis() / 1000 - timespam;

			if (diff > (min * 60)) {
				if (ex != null)
					ex.add(new IllegalAccessError("时间戳超时"));
				return false;
			} else {
				return true;
			}
		};
	}

	public static void main(String[] args) throws InterruptedException {
		Function<String, String> fn = Function.identity();
		fn = fn.andThen(TokenMaker.value("sp42@qq.com")).andThen(TokenMaker::addSalt).andThen(TokenMaker::addTimespam)
				.andThen(encryptAES("abc"));
//		fn = fn.andThen(Encode::md5).andThen(Encode::base64Encode);

		String encrypted = fn.apply(TOKEN_TPL);
		System.out.println(encrypted);

		String decrypted = Symmetri_Cipher.AES_Decrypt(encrypted, "abc");

		if (decrypted != null) {
			String[] arr = decrypted.split("-");
			Thread.sleep(500);

			List<Throwable> ex = new ArrayList<>();
			Predicate<String[]> checkTimespam_arr = _arr -> checkTimespam(5, ex).test(_arr[3]);

			boolean isOk = checkEmail("sp42@qq.com", ex).and(checkTimespam_arr).test(arr);

			System.out.println(isOk);
		}

	}
}
