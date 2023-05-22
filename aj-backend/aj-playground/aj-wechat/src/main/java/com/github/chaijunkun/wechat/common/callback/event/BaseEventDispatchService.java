package com.github.chaijunkun.wechat.common.callback.event;

import com.github.chaijunkun.wechat.common.callback.xml.event.BaseEvent;
import com.github.chaijunkun.wechat.common.callback.WeChatCallbackException;
import com.github.chaijunkun.wechat.common.callback.xml.msg.BaseMsg;

/**
 * 基础事件处理服务
 * @author chaijunkun
 * @since 2016年9月7日
 */
public interface BaseEventDispatchService<T extends BaseEvent> {
	
	public BaseMsg dispatchEvent(T event) throws WeChatCallbackException;

}
