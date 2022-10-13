package com.ajaxjs.wechat.payment;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ajaxjs.net.http.Get;
import com.ajaxjs.net.http.HttpEnum;
import com.ajaxjs.wechat.Constant;
import com.ajaxjs.wechat.merchant.HttpRequestWrapper;
import com.ajaxjs.wechat.merchant.MerchantConfig;

/**
 * 账单
 * 
 * @author Frank Cheung
 *
 */
@Component
public class BillService implements Constant, HttpEnum {
	@Autowired
	private MerchantConfig mchCfg;

	private final static String TRADEBILL_API = "/v3/bill/tradebill";

	/**
	 * 下载交易账单 https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_6.shtml
	 * 
	 * @param billDate 账单日期
	 * @param billType 账单类型（可为 null）
	 */
	Map<String, Object> getTradeBill(String billDate, String billType) {
		String url = TRADEBILL_API + "?bill_date=" + billDate;

		if (billType != null)
			url += "&bill_type=" + billType;

		HttpRequestWrapper r = new HttpRequestWrapper(GET, url);

		Map<String, Object> result = Get.api(API_DOMAIN + url, Utils.getSetHeadFn(mchCfg, r));

		return result;
	}

	private final static String FUNDFLOWBILL_API = "/v3/bill/fundflowbill";

	/**
	 * 下载资金账单 https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_7.shtml
	 * 
	 * @param billDate    账单日期
	 * @param accountType 资金账户类型（可为 null）
	 */
	Map<String, Object> getFundFlowBill(String billDate, String accountType) {
		String url = FUNDFLOWBILL_API + "?bill_date=" + billDate;

		if (accountType != null)
			url += "&account_type=" + accountType;

		HttpRequestWrapper r = new HttpRequestWrapper(GET, url);

		Map<String, Object> result = Get.api(API_DOMAIN + url, Utils.getSetHeadFn(mchCfg, r));

		return result;
	}
}
