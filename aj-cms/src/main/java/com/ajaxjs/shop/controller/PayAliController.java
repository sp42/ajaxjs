package com.ajaxjs.shop.controller;

import java.util.Date;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.shop.ShopConstant;
import com.ajaxjs.shop.model.OrderInfo;
import com.ajaxjs.shop.service.OrderService;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.MapTool;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.filter.MvcFilter;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;

@Component
@Path("/shop/pay/alipay")
public class PayAliController extends BaseController<Map<String, Object>> {
	private static final LogHelper LOGGER = LogHelper.getLog(PayAliController.class);

	@Resource("autoWire:ioc.OrderService|OrderService")
	private OrderService service;

	@GET
	@Path("result")
	@MvcFilter(filters = { DataBaseFilter.class })
	public String result(Map<String, Object> params, ModelAndView mv) throws ServiceException {
		LOGGER.info("支付宝服务器同步通知");

		if (isSignPass(params)) {
			// 支付成功
			String outerTradeNo = params.get("out_trade_no").toString();// 商户订单号
			String trade_no = params.get("trade_no").toString();// 支付宝交易号
			String total_amount = params.get("total_amount").toString();// 付款金额

			// TODO total_amount 金额判断
			if (total_amount != null) {

			}

			// 查询当前订单信息
			OrderInfo info = service.findByOrderNo(outerTradeNo);
			OrderInfo uInfo = new OrderInfo(); // 用于更新的 bean
			uInfo.setId(info.getId());
			uInfo.setPayStatus(ShopConstant.PAYED);
			uInfo.setPayDate(new Date());
			uInfo.setOuterTradeNo(trade_no);

			service.update(uInfo);

//			return "html::验证签名成功<br />" + "trade_no:" + trade_no + "<br/>out_trade_no:" + outerTradeNo
//					+ "<br/>total_amount:" + total_amount;
			mv.put("title", "支付结果");
			mv.put("msg", "支付成功！");
			mv.put("redirect", "../../../order/");
			return page("msg");
		} else {
			throw new ServiceException("验证签名失败！");
		}
	}

	/*
	 * 实际验证过程建议商户务必添加以下校验： 1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
	 * 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额）， 3、校验通知中的seller_id（或者seller_email)
	 * 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
	 * 4、验证app_id是否为该商户本身。
	 */
	@GET
	@Path("notify")
	public String notify(Map<String, Object> params) throws ServiceException {
		LOGGER.info("支付宝服务器异步通知");

		if (isSignPass(params)) {
			String out_trade_no = params.get("out_trade_no").toString();// 商户订单号
			String trade_no = params.get("trade_no").toString();// 支付宝交易号
			String trade_status = params.get("trade_status").toString();// 交易状态

			System.out.println("trade_no:" + trade_no + "<br/>out_trade_no:" + out_trade_no);

			if (trade_status.equals("TRADE_FINISHED")) {
				// 判断该笔订单是否在商户网站中已经做过处理
				// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				// 如果有做过处理，不执行商户的业务程序

				// 注意：
				// 退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
			} else if (trade_status.equals("TRADE_SUCCESS")) {
				// 判断该笔订单是否在商户网站中已经做过处理
				// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				// 如果有做过处理，不执行商户的业务程序

				// 注意：
				// 付款完成后，支付宝系统发送该交易状态通知
			}

			return "html::验证签名成功";
		} else {
			throw new ServiceException("验证签名失败！");
		}
	}

	/**
	 * 校验签名是否通过。注意是支付宝公钥，不是应用公钥
	 * 
	 * @param _params 请求参数
	 * @return 是否验证通过
	 */
	private static boolean isSignPass(Map<String, Object> _params) {
		Map<String, String> params = MapTool.as(_params, v -> v != null ? v.toString() : null);

		try {
			return AlipaySignature.rsaCheckV1(params, ConfigService.getValueAsString("shop.payment.Alipay.alipay_public_key"),
					"utf-8", "RSA2");
		} catch (AlipayApiException e) {
			LOGGER.warning(e);
			return false;

		}
	}

	@Override
	public IBaseService<Map<String, Object>> getService() {
		return null;
	}
}
