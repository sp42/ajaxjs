package com.github.chaijunkun.wechat.common.callback.event.impl;

import com.github.chaijunkun.wechat.common.callback.event.SubscribeEventDispatchService;
import com.github.chaijunkun.wechat.common.callback.WeChatCallbackDispatchService;
import com.github.chaijunkun.wechat.common.callback.WeChatCallbackException;
import com.github.chaijunkun.wechat.common.callback.xml.event.SubscribeEvent;
import com.github.chaijunkun.wechat.common.callback.xml.msg.BaseMsg;

/**
 * 默认的关注事件处理服务
 * @author chaijunkun
 * @since 2016年8月31日
 */
public class DefaultSubscribeEventDispatchServiceImpl extends WeChatCallbackDispatchService implements SubscribeEventDispatchService {

	@Override
	public BaseMsg dispatchEvent(SubscribeEvent event) throws WeChatCallbackException {
		if (logger.isDebugEnabled()){
			logger.debug("receive a subscribe event, from:{}, to:{}, eventKey:{}", event.getFromUserName(), event.getToUserName(), event.getEventKey());
		}
		return null;
	}

}
