package com.ajaxjs.web.google_captcha;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.ajaxjs.data_service.DataServiceConstant;
import com.ajaxjs.data_service.model.ServiceContext;
import com.ajaxjs.data_service.plugin.IPlugin;

/**
 * 防机器人提交
 *
 * @author Frank Cheung
 */
public class GoogleCapchta_DataServicePlugin implements IPlugin {
	@Autowired(required = false)
	private GoogleFilter g;

	@Override
	public boolean before(DataServiceConstant.CRUD type, ServiceContext ctx) {
		Map<String, Object> requestParams = ctx.getRequestParams();
		
		if (g != null && requestParams.containsKey(GoogleFilter.PARAM_NAME)) {
			String token = requestParams.get(GoogleFilter.PARAM_NAME).toString();
			requestParams.remove(GoogleFilter.PARAM_NAME);

			try {
				g.check(token);
			} catch (Throwable e) {
				throw e;
			}
		}

		return true;
	}
}
