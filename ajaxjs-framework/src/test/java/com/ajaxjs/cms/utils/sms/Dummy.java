package com.ajaxjs.cms.utils.sms;

import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.temp.SMS;

@Bean("Dummy")
public class Dummy {
	@Resource("AliyunSMSSender")
	private SMS sms;

	public SMS getSms() {
		return sms;
	}

	public void setSms(SMS sms) {
		this.sms = sms;
	}
}
