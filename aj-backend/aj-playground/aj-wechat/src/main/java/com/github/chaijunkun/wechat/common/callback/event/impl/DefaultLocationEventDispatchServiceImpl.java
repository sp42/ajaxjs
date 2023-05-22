package com.github.chaijunkun.wechat.common.callback.event.impl;

import com.github.chaijunkun.wechat.common.callback.WeChatCallbackDispatchService;
import com.github.chaijunkun.wechat.common.callback.WeChatCallbackException;
import com.github.chaijunkun.wechat.common.callback.event.LocationEventDispatchService;
import com.github.chaijunkun.wechat.common.callback.xml.event.LocationEvent;
import com.github.chaijunkun.wechat.common.callback.xml.msg.BaseMsg;

/**
 * 默认的定位事件处理服务
 * @author chaijunkun
 * @since 2016年8月31日
 */
public class DefaultLocationEventDispatchServiceImpl extends WeChatCallbackDispatchService implements LocationEventDispatchService {

	@Override
	public BaseMsg dispatchEvent(LocationEvent event) throws WeChatCallbackException {
		if (logger.isDebugEnabled()){
			logger.debug("receive a location event, from:{}, to:{}, longitude:{}, latitude:{}, precision:{}",
					event.getFromUserName(),
					event.getToUserName(),
					event.getLongitude(),
					event.getLatitude(),
					event.getPrecision());
		}
		return null;
	}

}
