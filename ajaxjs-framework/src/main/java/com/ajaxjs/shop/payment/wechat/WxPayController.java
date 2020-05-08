package com.ajaxjs.shop.payment.wechat;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.filter.MvcFilter;
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
			service.payNotification(perpayReturn, responseResult);
		} catch (Throwable e) {
			responseResult.put("return_code", "FAIL");
			responseResult.put("return_msg", e.getMessage() != null ? e.getMessage() : e.toString());
		}

		String xml = MapTool.mapToXml(responseResult);
		LOGGER.info("xml::" + xml);
		return "xml::" + xml;
	}

	@Override
	public IBaseService<Map<String, Object>> getService() {
		return null;
	}
}
