package com.ajaxjs.wechat.payment;

import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.util.function.Consumer;

import com.ajaxjs.wechat.merchant.HttpRequestWrapper;
import com.ajaxjs.wechat.merchant.MerchantConfig;
import com.ajaxjs.wechat.merchant.SignerMaker;

/**
 * 工具类
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class Utils {
	private final static String SCHEMA = "WECHATPAY2-SHA256-RSA2048";

	/**
	 * 设置签名 Token 到 HTTP 请求头
	 * 
	 * @param conn
	 * @param signToken
	 */
	public static void setSign2Header(HttpURLConnection conn, String signToken) {
		conn.addRequestProperty("Authorization", SCHEMA + " " + signToken);
		conn.addRequestProperty("Accept", "application/json");
		conn.addRequestProperty("Content-Type", "application/json");
	}

	/**
	 * 
	 * @param mchCfg
	 * @param r
	 * @return
	 */
	public static Consumer<HttpURLConnection> getSetHeadFn(MerchantConfig mchCfg, HttpRequestWrapper r) {
		String signToken = new SignerMaker(mchCfg).getToken(r);// 得到签名

		return (HttpURLConnection conn) -> setSign2Header(conn, signToken);
	}

	/**
	 * 元乘以 100，并四舍五入，并取整
	 */
	private final static BigDecimal TO_FEN = new BigDecimal(100);

	/**
	 * BigDecimal 转换为分
	 * 
	 * @param price 价格，单位是 元
	 * @return 价格，单位是 分
	 */
	public static int bigDecimal2Fen(BigDecimal price) {
		return price.multiply(TO_FEN).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
	}

}
