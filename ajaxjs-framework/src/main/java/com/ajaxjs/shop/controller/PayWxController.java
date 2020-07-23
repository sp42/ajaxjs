package com.ajaxjs.shop.controller;

import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.shop.ShopConstant;
import com.ajaxjs.shop.model.OrderInfo;
import com.ajaxjs.shop.payment.wxpay.PayNodifyResult;
import com.ajaxjs.shop.payment.wxpay.PaymentNotification;
import com.ajaxjs.shop.service.OrderService;
import com.ajaxjs.shop.service.Pay;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.io.IoHelper;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.MapTool;
import com.ajaxjs.web.mvc.filter.MvcFilter;

@Component
@Path("/shop/pay/wxpay")
public class PayWxController extends BaseController<Map<String, Object>> {
	private static final LogHelper LOGGER = LogHelper.getLog(PayWxController.class);

	@Resource("autoWire:ioc.OrderService|OrderService")
	private OrderService service;

	@POST
	@Path("notify")
	@MvcFilter(filters = { DataBaseFilter.class })
	public String result(HttpServletRequest request) throws ServiceException {
		LOGGER.info("微信服务器同步通知");

		PayNodifyResult result;

		try (InputStream in = request.getInputStream();) {
			String xml = IoHelper.byteStream2string(in);// 读取参数

			if (CommonUtil.isEmptyString(xml))
				throw new NullPointerException("没返回任何报文");

			result = payNotification(xml);
		} catch (Throwable e) {
			result = new PayNodifyResult();
			result.setReturn_code("FAIL");
			result.setReturn_msg(e.getMessage() != null ? e.getMessage() : e.toString());
		}

		String xml = MapTool.beanToXml(result);
		LOGGER.info("xml::" + xml);
		return "xml::" + xml;
	}

	@Resource("OrderService")
	private OrderService orderService;

	/**
	 * 处理支付通知的异步回调，控制器必须提供一个接口调用该服务方法
	 * 
	 * @param xml
	 * @return
	 */
	private PayNodifyResult payNotification(String xml) {
		LOGGER.info("处理支付通知的异步回调");

		Map<String, String> r = MapTool.xmlToMap(xml);
		PaymentNotification perpayReturn = MapTool.map2Bean(MapTool.as(r, v -> v == null ? null : (Object) v),
				PaymentNotification.class);
		Objects.requireNonNull(perpayReturn, "报文序列化XML为空");

		boolean isOk = false;
		String msg = "UNKONW";

		if (perpayReturn.isSuccess()) {
			// 验证签名是否正确
			String toCheck = Pay.generateSignature(r, ConfigService.getValueAsString("shop.payment.wx.apiSecret"));

			if (perpayReturn.getSign().equalsIgnoreCase(toCheck)) {
				String totalFee = perpayReturn.getTotal_fee(), orderNo = perpayReturn.getOut_trade_no();

				OrderInfo order = orderService.findByOrderNo(orderNo);
				Objects.requireNonNull(order, "订单 " + orderNo + " 不存在");

				if (totalFee.endsWith(Pay.toCent(order.getTotalPrice()))) {// 收到的金额和订单的金额是否吻合？
					OrderInfo updateBill = new OrderInfo();
					updateBill.setId(order.getId());
					updateBill.setTransactionId(perpayReturn.getTransaction_id());
					updateBill.setPayStatus(ShopConstant.PAYED);
					updateBill.setPayDate(new Date());
					orderService.update(updateBill);

					isOk = true;
					msg = "OK";
				} else {
					msg = "非法响应！交易金额不对，支付方返回 " + totalFee + "分，而订单记录是 " + (Pay.toCent(order.getTotalPrice())) + "分";
				}
			} else {
				msg = "非法响应！签名不通过";
			}
		} else {
			msg = "交易失败";
		}

		PayNodifyResult result = new PayNodifyResult();
		result.setReturn_code(isOk ? "SUCCESS" : "FAIL");
		result.setReturn_msg(msg);

		return result;
	}

	@Override
	public IBaseService<Map<String, Object>> getService() {
		return null;
	}
}
