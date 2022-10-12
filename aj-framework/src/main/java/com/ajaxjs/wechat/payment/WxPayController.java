package com.ajaxjs.wechat.payment;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.shop.service.OrderService;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.WebHelper;
import com.ajaxjs.util.binrary.BytesUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.util.map.MapTool;
import com.ajaxjs.wechat.applet.model.LoginSession;
import com.ajaxjs.wechat.applet.model.WeChatAppletConfig;
import com.ajaxjs.wechat.applet.util.AesUtil;
import com.ajaxjs.wechat.applet.util.PemUtil;
import com.ajaxjs.wechat.applet.util.RsaCryptoUtil;
import com.ajaxjs.wechat.merchant.MerchantConfig;
import com.ajaxjs.wechat.payment.model.PayResult;
import com.ajaxjs.wechat.payment.model.RequestPayment;
import com.ajaxjs.wechat.user.UserMgr;

/**
 * 微信小程序支付接口
 * 
 * @author Frank Cheung
 *
 */
@RestController
@RequestMapping("/wx_pay")
public class WxPayController {
	private static final LogHelper LOGGER = LogHelper.getLog(WxPayController.class);

	@Autowired(required = false)
	OrderService orderService;

	/**
	 * 传入设备 id，生成小程序支付所需参数返回。
	 * 
	 * @param sessionId
	 * @param machineId
	 * @return
	 */
	@PostMapping(value = "/applet_pay", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
	public String order(@RequestHeader(required = true) String sessionId, @RequestParam(required = true) long machineId) {
		LOGGER.info("微信 小程序 支付下单 " + sessionId);
		LoginSession session = UserMgr.isLoginedCheck(sessionId);

		Map<String, Object> result = orderService.preOrder(session, machineId);

		if (result != null) {
			if (result.containsKey("prepay_id")) {// 预支付交易会话标识
				LOGGER.info("获取 prepay_id 成功");
				String prepayId = result.get("prepay_id").toString();
				String payParams = getRequestPayment(prepayId);

				return payParams;
			} else if (result.containsKey("message"))
				return BaseController.toJson(result.get("message"));
		}

		return BaseController.jsonNoOk("微信支付下单失败！");
	}

	private final static String SCCUESS = "TRANSACTION.SUCCESS";

	private final static String TRADE_STATE = "SUCCESS";

	/**
	 * 客户端无须调用该接口，该接口由微信支付中心调用
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/applet_pay_notify", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
	public String notifyUrl(HttpServletRequest req) {
		String errMsg = "未知异常";

		try {
			Map<String, Object> params = WebHelper.getRawBodyAsJson(req);

			if (params != null) {
				if (params.containsKey("event_type") && SCCUESS.equals(params.get("event_type"))) {
					// 支付成功
					@SuppressWarnings("unchecked")
					Map<String, Object> resource = (Map<String, Object>) params.get("resource");
					LOGGER.info(params.get("summary") + "" + resource);

					// 对 resource 对象进行解密
					String ciphertext = resource.get("ciphertext").toString();
					LOGGER.info(ciphertext);

					byte[] apiV3KeyByte = BytesUtil.getUTF8_Bytes(merchantConfig.getApiV3Key());
					byte[] associatedData = BytesUtil.getUTF8_Bytes(resource.get("associated_data").toString());
					byte[] nonce = BytesUtil.getUTF8_Bytes(resource.get("nonce").toString());

					// 解密
					AesUtil aesUtil = new AesUtil(apiV3KeyByte);
					String cert = aesUtil.decryptToString(associatedData, nonce, ciphertext);
					LOGGER.info(cert);

					PayResult bean = json2PayResultBean(cert);

					if (TRADE_STATE.equals(bean.getTrade_state())) {// 再次检查
						// 业务逻辑判断是否收到钱
						LOGGER.info("收到钱：" + bean.getPayer_total());

						return BaseController.jsonOk();
					} else
						throw new NullPointerException("解密失败");
				}
			} else
				throw new NullPointerException("返回参数失败！");
		} catch (Throwable e) {
			LOGGER.warning(e);
			errMsg = e.getMessage();
		}

		return "{\"code\": \"FAIL\", \"message\": \"" + errMsg + "\"}";
	}

	/**
	 * 官方返回的 JSON 是嵌套的，现在将其扁平化
	 * 
	 * @param json
	 * @return
	 */
	public static PayResult json2PayResultBean(String json) {
		Map<String, Object> map = JsonHelper.parseMap(json);
		PayResult bean = MapTool.map2Bean(map, PayResult.class, false, false);

		@SuppressWarnings("unchecked")
		Map<String, Object> amount = (Map<String, Object>) map.get("amount");
		bean.setTotal((int) amount.get("total"));
		bean.setPayer_total((int) amount.get("payer_total"));

		@SuppressWarnings("unchecked")
		Map<String, Object> payer = (Map<String, Object>) map.get("payer");
		bean.setPayerOpenId(payer.get("openid").toString());

		return bean;
	}

	@Autowired
	WeChatAppletConfig weChatAppletConfig;

	@Autowired
	private MerchantConfig merchantConfig;

	/**
	 * 
	 * @param privateKey
	 * @param rp
	 * @return
	 */
	private String getSign(String privateKey, RequestPayment rp) {
		StringBuilder sb = new StringBuilder();
		sb.append(weChatAppletConfig.getAccessKeyId() + "\n");
		sb.append(rp.getTimeStamp() + "\n");
		sb.append(rp.getNonceStr() + "\n");
		sb.append(rp.getPrepayIdPackage() + "\n");

		PrivateKey key = PemUtil.loadPrivateKey(privateKey);

		return RsaCryptoUtil.sign(key, sb.toString().getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * package 修正，最后转换为 JSON 字符串
	 * 
	 * @param rp
	 * @return
	 */
	public String getRequestPayment(String prepayId) {
		RequestPayment rp = new RequestPayment();
		rp.setTimeStamp((System.currentTimeMillis() / 1000) + "");
		rp.setNonceStr(StrUtil.getRandomString(10));
		rp.setPrepayIdPackage("prepay_id=" + prepayId);

		String sign = getSign(merchantConfig.getPrivateKey(), rp);
		rp.setPaySign(sign);

		Map<String, Object> map = MapTool.bean2Map(rp);
		map.put("package", rp.getPrepayIdPackage());
		map.remove("prepayIdPackage");

		return JsonHelper.toJson(map);
	}
}
