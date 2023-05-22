package com.github.chaijunkun.wechat.common.callback.msg.impl;

import com.github.chaijunkun.wechat.common.callback.xml.msg.BaseMsg;
import com.github.chaijunkun.wechat.common.callback.xml.msg.VoiceMsg;
import com.github.chaijunkun.wechat.common.callback.WeChatCallbackDispatchService;
import com.github.chaijunkun.wechat.common.callback.msg.VoiceMsgDispatchService;

/**
 * 默认的视频消息处理服务
 */
public class DefaultVoiceMsgDispatchServiceImpl extends WeChatCallbackDispatchService implements VoiceMsgDispatchService {

    @Override
    public BaseMsg dispatchMsg(VoiceMsg msg) {
        logger.debug("receive a short voideo msg, msg id:{}, from:{}, to:{}, media id:{}, format:{}", msg.getMsgId(), msg.getFromUserName(), msg.getToUserName(), msg.getMediaId(), msg.getFormat());

        return null;
    }

}
