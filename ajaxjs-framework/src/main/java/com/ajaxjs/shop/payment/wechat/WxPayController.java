package com.ajaxjs.shop.payment.wechat;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.shop.ShopConstant;
import com.ajaxjs.shop.model.OrderInfo;
import com.ajaxjs.shop.payment.wechat.model.PaymentNotification;
import com.ajaxjs.shop.service.OrderService;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.io.IoHelper;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.MapTool;

@Bean
@Path("/shop/pay/wxpay")
public class WxPayController extends BaseController<Map<String, Object>> {
	private static final LogHelper LOGGER = LogHelper.getLog(WxPayController.class);

	@Resource("autoWire:ioc.OrderService|OrderService")
	private OrderService service;

	@POST
	@Path("notify")
	@MvcFilter(filters = { DataBaseFilter.class })
	public String result(HttpServletRequest request) throws ServiceException {
		LOGGER.info("微信服务器同步通知");

		Map<String, String> responseResult = new HashMap<>();

		try {
			InputStream in = null;
			String xml = null;

			try {
				in = request.getInputStream();
				xml = IoHelper.byteStream2string(in);// 读取参数
			} finally {
				in.close();
			}

			if (CommonUtil.isEmptyString(xml))
				throw new NullPointerException("没返回任何报文");

			Map<String, String> r = MapTool.xmlToMap(xml);
			PaymentNotification perpayReturn = MapTool.map2Bean(MapTool.as(r, v -> v == null ? null : (Object) v),
					PaymentNotification.class);
			Objects.requireNonNull(perpayReturn, "报文序列化XML为空");

			perpayReturn.setData(r);
			payNotification(perpayReturn, responseResult);
		} catch (Throwable e) {
			responseResult.put("return_code", "FAIL");
			responseResult.put("return_msg", e.getMessage() != null ? e.getMessage() : e.toString());
		}

		String xml = MapTool.mapToXml(responseResult);
		LOGGER.info("xml::" + xml);
		return "xml::" + xml;
	}

	@Resource("OrderService")
	private OrderService orderService;

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
			String toCheck = WxUtil.generateSignature(perpayReturn.getData(),
					ConfigService.getValueAsString("mini_program.MchSecretId"));

			if (perpayReturn.getSign().equals(toCheck)) {
				String totalFee = perpayReturn.getTotal_fee(), orderNo = perpayReturn.getOut_trade_no();

				OrderInfo order = orderService.findByOrderNo(orderNo);
				Objects.requireNonNull(order, "订单 " + orderNo + " 不存在");

				if (totalFee.endsWith(WxUtil.toCent(order.getTotalPrice()))) {// 收到的金额和订单的金额是否吻合？
					OrderInfo updateBill = new OrderInfo();
					updateBill.setId(order.getId());
					updateBill.setTransactionId(perpayReturn.getTransaction_id());
					updateBill.setPayStatus(ShopConstant.PAYED);
					updateBill.setPayDate(new Date());
					orderService.update(updateBill);

					isOk = true;
					msg = "OK";
				} else {
					msg = "非法响应！交易金额不对，支付方返回 " + totalFee + "分，而订单记录是 " + (WxUtil.toCent(order.getTotalPrice()))
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

	@Override
	public IBaseService<Map<String, Object>> getService() {
		return null;
	}
}