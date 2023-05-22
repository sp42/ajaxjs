package com.github.chaijunkun.wechat.common.callback.msg.impl;

import com.github.chaijunkun.wechat.common.callback.msg.ShortVideoMsgDispatchService;
import com.github.chaijunkun.wechat.common.callback.xml.msg.BaseMsg;
import com.github.chaijunkun.wechat.common.callback.xml.msg.ShortVideoMsg;
import com.github.chaijunkun.wechat.common.callback.WeChatCallbackDispatchService;

/**
 * 默认的短视频消息处理服务
 * @author chaijunkun
 * @since 2016年8月30日
 */
public class DefaultShortVideoMsgDispatchServiceImpl extends WeChatCallbackDispatchService implements ShortVideoMsgDispatchService {

	@Override
	public BaseMsg dispatchMsg(ShortVideoMsg msg) {
		if (logger.isDebugEnabled()){
			logger.debug("receive a short voideo msg, msg id:{}, from:{}, to:{}, media id:{}, thumb media id:{}", msg.getMsgId(), msg.getFromUserName(), msg.getToUserName(), msg.getMediaId(), msg.getThumbMediaId());
		}
		return null;
	}

}
