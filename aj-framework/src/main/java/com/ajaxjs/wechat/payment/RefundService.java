package com.ajaxjs.wechat.payment;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ajaxjs.net.http.Get;
import com.ajaxjs.net.http.HttpEnum;
import com.ajaxjs.net.http.Post;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.wechat.Constant;
import com.ajaxjs.wechat.merchant.HttpRequestWrapper;
import com.ajaxjs.wechat.merchant.MerchantConfig;

/**
 * 退款处理
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
@Component
public class RefundService implements Constant, HttpEnum {
	@Autowired
	private MerchantConfig mchCfg;

	private final static String REFUNDS_API = "/v3/refund/domestic/refunds";

	/**
	 * 申请退款 https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_9.shtml 很复杂
	 * TODO
	 * 
	 * @param billDate 账单日期
	 * @param billType 账单类型
	 */
	Map<String, Object> refund(String transactionId, String billType) {
		Map<String, String> params = new HashMap<>();
		params.put("transaction_id", transactionId);
		String rawJson = JsonHelper.toJson(params);
		Map<String, Object> result = Post.api(REFUNDS_API, rawJson);

		return result;
	}

	private final static String QUERY＿REFUNDS_API = "/v3/refund/domestic/refunds/";

	/**
	 * 查询单笔退款 https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_10.shtml
	 * 
	 * @param outRefundNo 商户退款单号日期
	 */
	Map<String, Object> queryRefund(String outRefundNo) {
		String url = QUERY＿REFUNDS_API + outRefundNo;
		HttpRequestWrapper r = new HttpRequestWrapper(GET, url);
		Map<String, Object> result = Get.api(API_DOMAIN + url, Utils.getSetHeadFn(mchCfg, r));

		return result;
	}

	/**
	 * 退款结果通知 https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_11.shtml
	 */
	static void refundNotify() {

	}
}
