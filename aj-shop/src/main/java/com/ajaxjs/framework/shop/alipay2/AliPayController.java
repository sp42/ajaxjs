package com.ajaxjs.framework.shop.alipay2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.framework.shop.alipay2.util.AlipayNotify;
import com.ajaxjs.framework.shop.alipay2.util.AlipaySubmit;
import com.ajaxjs.framework.shop.alipay2.util.UtilDate;

@RestController
@RequestMapping(value = "api/ali")
public class AliPayController {
	@RequestMapping(value = "/pay", method = RequestMethod.POST)
	public String deposit(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		Map<String, String> sParaTemp = new HashMap<>();
		sParaTemp.put("service", AlipayConfig.service);
		sParaTemp.put("partner", AlipayConfig.partner);
		sParaTemp.put("seller_id", AlipayConfig.seller_id);
		sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("payment_type", AlipayConfig.payment_type);
		sParaTemp.put("notify_url", AlipayConfig.notify_url);
		sParaTemp.put("return_url", AlipayConfig.return_url);
		sParaTemp.put("anti_phishing_key", AlipayConfig.anti_phishing_key);
		sParaTemp.put("exter_invoke_ip", AlipayConfig.exter_invoke_ip);
		sParaTemp.put("out_trade_no", "123");
		sParaTemp.put("subject", "subject");
		sParaTemp.put("total_fee", "0.01");
		sParaTemp.put("body", "body");

		// 建立请求
		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "get", "确认");
		return sHtmlText;
	}

	@RequestMapping(value = "/result")
	public String aliOrderBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("ok!!!!!");
		Map<String, String> params = new HashMap<>();
		Map<?, ?> requestParams = request.getParameterMap();

		for (Iterator<?> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++)
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";

			params.put(name, valueStr);
		}

		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");

		System.out.println(out_trade_no);

		if (AlipayNotify.verify(params)) {
			if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {

			}

			return "success";
		} else {// 验证失败
			return "fail";
		}
	}

	@RequestMapping(value = "/refund", method = RequestMethod.POST)
	public String refund(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		Map<String, String> sParaTemp = new HashMap<>();
		sParaTemp.put("service", AlipayConfig.refund_service);
		sParaTemp.put("partner", AlipayConfig.partner);
		sParaTemp.put("seller_user_id", AlipayConfig.seller_id);
		sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("notify_url", AlipayConfig.notify_url + "/refund");

		DateFormat dft = new SimpleDateFormat("yyyyMMdd");
		sParaTemp.put("refund_date", UtilDate.getDateFormatter());
		sParaTemp.put("batch_no", dft.format(new Date()) + "123");
		sParaTemp.put("batch_num", "1");
		sParaTemp.put("detail_data", request.getParameter("tradeNo") + "^" + request.getParameter("price") + "^" + request.getParameter("reason"));

		// 建立请求
		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "get", "确认");
		return sHtmlText;
	}

	@RequestMapping(value = "/refund/result")
	public String refundBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取支付宝POST过来反馈信息
		Map<String, String> params = new HashMap<>();
		Map<String, String[]> requestParams = request.getParameterMap();

		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";

			for (int i = 0; i < values.length; i++)
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";

			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}

		String batch_no = new String(request.getParameter("batch_no").getBytes("ISO-8859-1"), "UTF-8");
		String success_num = new String(request.getParameter("success_num").getBytes("ISO-8859-1"), "UTF-8");
		String result_details = new String(request.getParameter("result_details").getBytes("ISO-8859-1"), "UTF-8");

		System.out.println(batch_no);
		System.out.println(success_num);
		System.out.println(result_details);

		if (AlipayNotify.verify(params)) // 验证成功
			return "success";
		else // 验证失败
			return "fail";

	}
}
