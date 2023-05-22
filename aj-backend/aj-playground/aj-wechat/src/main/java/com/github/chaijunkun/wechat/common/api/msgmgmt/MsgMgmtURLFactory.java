package com.github.chaijunkun.wechat.common.api.msgmgmt;

import com.github.chaijunkun.wechat.common.api.AbstractURLFactory;
import com.github.chaijunkun.wechat.common.api.URLBean;

/**
 * 消息管理接口相关URL
 * @author chaijunkun
 * @since 2016年9月6日
 */
public class MsgMgmtURLFactory extends AbstractURLFactory {
	
	/** 发送模板消息接口URL */
	private static final URLBean templateSend = new URLBean(true, COMMON_API_DOMAIN, "/cgi-bin/message/template/send");

	/**
	 * 获取发送模板消息接口URL
	 * @return 发送模板消息接口URL
	 */
	public String getTemplateSend() {
		return templateSend.getAbsoluteURL();
	}

}
