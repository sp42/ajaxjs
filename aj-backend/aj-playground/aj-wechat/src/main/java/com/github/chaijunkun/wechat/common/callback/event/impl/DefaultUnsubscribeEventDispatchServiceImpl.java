package com.github.chaijunkun.wechat.common.callback.event.impl;

import com.github.chaijunkun.wechat.common.callback.event.UnsubscribeEventDispatchService;
import com.github.chaijunkun.wechat.common.callback.WeChatCallbackDispatchService;
import com.github.chaijunkun.wechat.common.callback.WeChatCallbackException;
import com.github.chaijunkun.wechat.common.callback.xml.event.UnsubscribeEvent;
import com.github.chaijunkun.wechat.common.callback.xml.msg.BaseMsg;

/**
 * 默认的取消关注事件处理服务
 * @author chaijunkun
 * @since 2016年8月31日
 */
public class DefaultUnsubscribeEventDispatchServiceImpl extends WeChatCallbackDispatchService implements UnsubscribeEventDispatchService {

	@Override
	public BaseMsg dispatchEvent(UnsubscribeEvent event) throws WeChatCallbackException {
		if (logger.isDebugEnabled()){
			logger.debug("receive a unsubscribe event, from:{}, to:{}", event.getFromUserName(), event.getToUserName());
		}return null;
	}

}
