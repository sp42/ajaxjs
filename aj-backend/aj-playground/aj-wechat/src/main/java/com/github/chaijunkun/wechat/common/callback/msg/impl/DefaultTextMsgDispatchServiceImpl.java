package com.github.chaijunkun.wechat.common.callback.msg.impl;

import com.github.chaijunkun.wechat.common.callback.msg.TextMsgDispatchService;
import com.github.chaijunkun.wechat.common.callback.xml.msg.BaseMsg;
import com.github.chaijunkun.wechat.common.callback.xml.msg.TextMsg;
import com.github.chaijunkun.wechat.common.callback.WeChatCallbackDispatchService;

/**
 * 默认的文本消息处理服务
 * @author chaijunkun
 * @since 2016年8月30日
 */
public class DefaultTextMsgDispatchServiceImpl extends WeChatCallbackDispatchService implements TextMsgDispatchService {
	
	@Override
	public BaseMsg dispatchMsg(TextMsg msg) {
		if (logger.isDebugEnabled()){
			logger.debug("receive a text msg, msg id:{}, from:{}, to:{}, content:{}", msg.getMsgId(), msg.getFromUserName(), msg.getToUserName(), msg.getContent());
		}
		return null;
	}

}
