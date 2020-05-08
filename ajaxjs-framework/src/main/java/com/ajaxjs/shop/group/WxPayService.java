package com.ajaxjs.shop.group;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.shop.ShopConstant;
import com.ajaxjs.shop.ShopHelper;
import com.ajaxjs.shop.dao.OrderInfoDao;
import com.ajaxjs.shop.model.OrderInfo;
import com.ajaxjs.shop.service.CartService;
import com.ajaxjs.shop.service.OrderService;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.weixin.payment.PayConstant;
import com.ajaxjs.weixin.payment.PaySignatures;
import com.ajaxjs.weixin.payment.PaymentNotification;
import com.ajaxjs.weixin.payment.PerpayInfo;
import com.ajaxjs.weixin.payment.PerpayReturn;
import com.ajaxjs.weixin.payment.WxPay;

/**
 * 订单核心业务
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Bean
public class WxPayService extends BaseService<OrderInfo> implements PayConstant {
	public static OrderInfoDao dao = new Repository().bind(OrderInfoDao.class);

	public static final LogHelper LOGGER = LogHelper.getLog(WxPayService.class);

	{
		setUiName("订单");
		setShortName("order");
		setDao(dao);
	}

	@Resource("OrderService")
	private OrderService orderService;

	/**
	 * 生成订单并支付
	 * 
	 * @param userId    用户 id
	 * @param addressId 送货地址id
	 * @param cartIds   订单明细由购物车的数据生成
	 * @return
	 */
	public Map<String, ?> createOrderAndPay(long userId, long addressId, String[] cartIds, String ip) {
		OrderInfo orderInfo = orderService.processOrder(userId, addressId, cartIds, 0);
		return wxPay(userId, orderInfo, ip);
	}

	public Map<String, ?> pay(long userId, long orderId, String ip) {
		return wxPay(userId, dao.findById(orderId), ip);
	}

	/**
	 * 核心支付方法
	 * 
	 * @param userId
	 * @param orderInfo
	 * @param ip
	 * @return
	 */
	static Map<String, ?> wxPay(long userId, OrderInfo orderInfo, String ip) {
		String openId = dao.findUserOpenId(userId); // 小程序内调用登录接口，获取到用户的 openid

		// 预付单
		PerpayInfo perpayInfo = new PerpayInfo();
		perpayInfo.setOpenid(openId);
		perpayInfo.setBody("集结号-网超");
		perpayInfo.setIp(ip);

//		Map<String, String> map = WxPay.unifiedOrder(orderInfo, perpayInfo);
//		PerpayReturn result = WxPay.sendUnifiedOrder(map); 

		PerpayReturn result = WxPay.sendUnifiedOrder(orderInfo, perpayInfo);// 商户 server 调用支付统一下单

		if (result.isSuccess()) {
			LOGGER.info("获取 perpayid 成功！{0}", result.getPrepay_id());
//			Map<String, String> r = PaySignatures.getPayParam(result.getPrepay_id(), map.get("nonce_str")); // 商户 server 调用再次签名
			Map<String, String> r = PaySignatures.getPayParam(result.getPrepay_id(), result.getNonce_str()); // 商户
																												// server
																												// 调用再次签名
			r.put("orderInfoId", orderInfo.getId() + ""); // 新订单 id

			return r; // 将此 map 返回给小程序客户端，让它来调起支付界面
		} else {
			LOGGER.warning("获取 perpayid 失败！错误代码：{0}，错误信息：{1}。", result.getError_code(), result.getReturn_msg());
			return new HashMap<String, Object>() {
				private static final long serialVersionUID = 1L;
				{
					put("isOk", false);
					put("msg", result.getReturn_msg());
				}
			};
		}
	}

	@Resource("CartService")
	private CartService cartService;

	/**
	 * 处理支付通知的异步回调，控制器必须提供一个接口调用该服务方法
	 * 
	 * @param perpayReturn
	 * @param responseResult
	 * @return
	 */
	public Map<String, String> payNotification(PaymentNotification perpayReturn, Map<String, String> responseResult) {
		LOGGER.info("处理支付通知的异步回调");
		boolean isOk = false;
		String msg = "UNKONW";

		if (perpayReturn.isSuccess()) {
			// 验证签名是否正确
			String toCheck = PaySignatures.generateSignature(perpayReturn.getData(), PaySignatures.getMchSecretId());

			if (perpayReturn.getSign().equals(toCheck)) {
				String totalFee = perpayReturn.getTotal_fee(), orderNo = perpayReturn.getOut_trade_no();

				OrderInfo order = orderService.findByOrderNo(orderNo);
				Objects.requireNonNull(order, "订单 " + orderNo + " 不存在");

				if (totalFee.endsWith(ShopHelper.toCent(order.getTotalPrice()))) {// 收到的金额和订单的金额是否吻合？
					OrderInfo updateBill = new OrderInfo();
					updateBill.setId(order.getId());
					updateBill.setTransactionId(perpayReturn.getTransaction_id());
					updateBill.setPayStatus(ShopConstant.PAYED);
					updateBill.setPayDate(new Date());
					update(updateBill);
					isOk = true;
					msg = "OK";
				} else {
					msg = "非法响应！交易金额不对，支付方返回 " + totalFee + "分，而订单记录是 " + (ShopHelper.toCent(order.getTotalPrice()))
							+ "分";
				}
			} else {
				msg = "非法响应！";
			}
		} else {
			msg = "交易失败";
		}

		responseResult.put("return_code", isOk ? "SUCCESS" : "FAIL");
		responseResult.put("return_msg", msg);

		return responseResult;
	}
}