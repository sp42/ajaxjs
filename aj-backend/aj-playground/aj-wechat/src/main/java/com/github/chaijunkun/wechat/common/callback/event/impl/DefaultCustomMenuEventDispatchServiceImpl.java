package com.github.chaijunkun.wechat.common.callback.event.impl;

import com.github.chaijunkun.wechat.common.callback.event.CustomMenuEventDispatchService;
import com.github.chaijunkun.wechat.common.callback.WeChatCallbackDispatchService;
import com.github.chaijunkun.wechat.common.callback.WeChatCallbackException;
import com.github.chaijunkun.wechat.common.callback.xml.event.CustomMenuEvent;
import com.github.chaijunkun.wechat.common.callback.xml.msg.BaseMsg;

/**
 * 默认的自定义菜单事件处理服务
 * @author chaijunkun
 * @since 2016年8月31日
 */
public class DefaultCustomMenuEventDispatchServiceImpl extends WeChatCallbackDispatchService implements CustomMenuEventDispatchService {

	@Override
	public BaseMsg dispatchEvent(CustomMenuEvent event) throws WeChatCallbackException {
		if (logger.isDebugEnabled()){
			logger.debug("receive a custom menu event, from:{}, to:{}, eventKey:{}", event.getFromUserName(), event.getToUserName(), event.getEventKey());
		}
		return null;
	}

}
