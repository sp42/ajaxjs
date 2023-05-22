package com.github.chaijunkun.wechat.common.api.msgmgmt;

import com.github.chaijunkun.wechat.common.api.WeChatAPI;
import com.github.chaijunkun.wechat.common.api.WeChatAPIException;

/**
 * 消息管理接口
 * @author chaijunkun
 * @since 2016年9月6日
 */
public class MsgMgmtAPI extends WeChatAPI<MsgMgmtURLFactory> {

	@Override
	public MsgMgmtURLFactory getUrlFactory() {
		return urlFactory;
	}

	@Override
	public void setUrlFactory(MsgMgmtURLFactory urlFactory) {
		this.urlFactory = urlFactory;
	}
	
	public TemplateSendResult templateSend(TemplateSendParam param) throws WeChatAPIException {
		return super.doPostAPIWithAccessToken(urlFactory.getTemplateSend(), param, TemplateSendResult.class);
	}

}
