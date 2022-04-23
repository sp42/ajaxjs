package com.ajaxjs.wechat.payment;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ajaxjs.framework.shop.model.OrderInfo;
import com.ajaxjs.framework.shop.service.OrderService;
import com.ajaxjs.net.http.Get;
import com.ajaxjs.net.http.HttpEnum;
import com.ajaxjs.net.http.Post;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.util.map.MapTool;
import com.ajaxjs.wechat.Constant;
import com.ajaxjs.wechat.applet.model.LoginSession;
import com.ajaxjs.wechat.applet.model.WeChatAppletConfig;
import com.ajaxjs.wechat.merchant.HttpRequestWrapper;
import com.ajaxjs.wechat.merchant.MerchantConfig;
import com.ajaxjs.wechat.merchant.SignerMaker;
import com.ajaxjs.wechat.payment.model.PreOrder;

/**
 * 小程序支付业务
 * 
 * @author Frank Cheung
 *
 */
@Component
public class Pay_AppletService implements Constant, HttpEnum {
	private static final LogHelper LOGGER = LogHelper.getLog(OrderService.class);

	@Autowired
	private WeChatAppletConfig appletCfg;

	@Autowired
	private MerchantConfig mchCfg;

	private static final String JSAPI = "/v3/pay/transactions/jsapi";

	@Value("${WeChat.Merchant.AppletPayNotifyUrl}")
	private String appletPayNotifyUrl;

//	@Autowired(required = false)
//	OrderService orderService;

	/**
	 * 下单 https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_1.shtml
	 * 
	 * @param session
	 * @param goods
	 * @return
	 */
	public Map<String, Object> preOrder(LoginSession session, OrderInfo order) {
		// 支付者
		HashMap<String, String> payer = new HashMap<>();
		payer.put("openid", session.getOpenid());

		// 金额
		HashMap<String, Integer> amount = new HashMap<>();
		amount.put("total", Utils.bigDecimal2Fen(order.getOrderPrice()));

		LOGGER.info(mchCfg.getMchId() + "::::::::" + appletPayNotifyUrl);

		// 构建支付参数
		PreOrder p = new PreOrder();
		p.setAppid(appletCfg.getAccessKeyId());
		p.setMchid(mchCfg.getMchId());
		p.setOut_trade_no(order.getOrderNo());
		p.setDescription(order.getName());
		p.setNotify_url(appletPayNotifyUrl);
		p.setDescription(order.getName());

		Map<String, Object> params = MapTool.bean2Map(p);
		params.put("amount", amount);
		params.put("payer", payer);

		String rawJson = JsonHelper.toJson(params);
		String url = "/v3/pay/transactions/jsapi";

		HttpRequestWrapper r = new HttpRequestWrapper(POST, url, rawJson);

		SignerMaker signer = new SignerMaker(mchCfg);
		String signToken = signer.getToken(r);// 得到签名

//		LOGGER.info(signToken);
		Map<String, Object> result = Post.api(API_DOMAIN + JSAPI, rawJson, conn -> {
			Utils.setSign2Header(conn, signToken);
		});

//		orderService.updateOrderTransactionId(order.getId(), result.get("").toString());

//		return null;
		return result;
	}

	private final static String QUERY_TRANSACTION_API = "/v3/pay/transactions/id/";

	/**
	 * 微信支付订单号查询订单 https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_2.shtml
	 * 
	 * @param transactionId 微信支付订单号
	 */
	Map<String, Object> getOrderByTransactionId(String transactionId) {
		String url = QUERY_TRANSACTION_API + transactionId + "?mchid=" + mchCfg.getMchId();
		HttpRequestWrapper r = new HttpRequestWrapper(GET, url);
		Map<String, Object> result = Get.api(API_DOMAIN + url, Utils.getSetHeadFn(mchCfg, r));

		return result;
	}

	private final static String QUERY_TRANSACTION_API_O = "/v3/pay/transactions/out-trade-no/";

	/**
	 * 商户订单号查询订单 https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_2.shtml
	 * 
	 * @param outTradeNo 商户订单号
	 */
	Map<String, Object> getOrderByOrderNo(String outTradeNo) {
		String url = QUERY_TRANSACTION_API_O + outTradeNo + "?mchid=" + mchCfg.getMchId();
		HttpRequestWrapper r = new HttpRequestWrapper(GET, url);
		Map<String, Object> result = Get.api(API_DOMAIN + url, Utils.getSetHeadFn(mchCfg, r));

		return result;
	}

	private final static String CLOSE_API = "/v3/pay/transactions/out-trade-no/%s/close";

	/**
	 * 退款结果通知 https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_11.shtml
	 * 
	 * @param outTradeNo 商户订单号
	 */
	void closeOrder(String outTradeNo) {
		String url = String.format(CLOSE_API, outTradeNo);

		Map<String, String> params = new HashMap<>();
		params.put("mchid", mchCfg.getMchId());
		String rawJson = JsonHelper.toJson(params);
		HttpRequestWrapper r = new HttpRequestWrapper(POST, url, rawJson);

		Post.api(API_DOMAIN + url, rawJson, Utils.getSetHeadFn(mchCfg, r)); // 该接口是无数据返回的
	}
}
