package com.github.chaijunkun.wechat.common.callback;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import com.github.chaijunkun.wechat.common.WeChatAPIConfig;
import com.github.chaijunkun.wechat.common.callback.event.CustomMenuEventDispatchService;
import com.github.chaijunkun.wechat.common.callback.event.LocationEventDispatchService;
import com.github.chaijunkun.wechat.common.callback.event.SubscribeEventDispatchService;
import com.github.chaijunkun.wechat.common.callback.event.UnsubscribeEventDispatchService;
import com.github.chaijunkun.wechat.common.callback.event.impl.DefaultCustomMenuEventDispatchServiceImpl;
import com.github.chaijunkun.wechat.common.callback.event.impl.DefaultLocationEventDispatchServiceImpl;
import com.github.chaijunkun.wechat.common.callback.event.impl.DefaultSubscribeEventDispatchServiceImpl;
import com.github.chaijunkun.wechat.common.callback.event.impl.DefaultUnsubscribeEventDispatchServiceImpl;
import com.github.chaijunkun.wechat.common.callback.msg.ShortVideoMsgDispatchService;
import com.github.chaijunkun.wechat.common.callback.msg.TextMsgDispatchService;
import com.github.chaijunkun.wechat.common.callback.msg.VoiceMsgDispatchService;
import com.github.chaijunkun.wechat.common.callback.msg.impl.DefaultShortVideoMsgDispatchServiceImpl;
import com.github.chaijunkun.wechat.common.callback.msg.impl.DefaultTextMsgDispatchServiceImpl;
import com.github.chaijunkun.wechat.common.callback.msg.impl.DefaultVoiceMsgDispatchServiceImpl;
import com.github.chaijunkun.wechat.common.callback.xml.CommonXML;
import com.github.chaijunkun.wechat.common.callback.xml.RxEncryptXML;
import com.github.chaijunkun.wechat.common.callback.xml.TxEncryptXML;
import com.github.chaijunkun.wechat.common.callback.xml.TypeAnalyzingBean;
import com.github.chaijunkun.wechat.common.callback.xml.event.*;
import com.github.chaijunkun.wechat.common.callback.xml.msg.BaseMsg;
import com.github.chaijunkun.wechat.common.callback.xml.msg.ShortVideoMsg;
import com.github.chaijunkun.wechat.common.callback.xml.msg.TextMsg;
import com.github.chaijunkun.wechat.common.callback.xml.msg.VoiceMsg;
import com.github.chaijunkun.wechat.common.util.JSONUtil;
import com.github.chaijunkun.wechat.common.util.XMLUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.chaijunkun.wechat.common.callback.WeChatCallbackException.CallbackErrEnum;
import com.github.chaijunkun.wechat.common.callback.xml.event.CustomMenuEvent;
import com.github.chaijunkun.wechat.common.callback.xml.event.LocationEvent;
import com.github.chaijunkun.wechat.common.callback.xml.event.SubscribeEvent;
import com.github.chaijunkun.wechat.common.callback.xml.event.UnsubscribeEvent;

/**
 * 微信回调服务
 *
 * @author chaijunkun
 * @since 2016年9月6日
 */
@Service
public class WeChatCallbackDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(WeChatCallbackDispatcher.class);

    /**
     * 微信安全模式接入的组件
     */
    private volatile WeChatSecureMode secureMode;

    /**
     * 微信接入配置
     */
    @Autowired
    private WeChatAPIConfig apiConfig;

    /**
     * 文本消息处理服务
     */
    @Autowired(required = false)
    private TextMsgDispatchService textMsgDispatchService;

    /**
     * 语音消息处理服务
     */
    @Autowired(required = false)
    private VoiceMsgDispatchService voiceMsgDispatchService;

    /**
     * 短视频消息处理服务
     */
    @Autowired(required = false)
    private ShortVideoMsgDispatchService shortVideoMsgDispatchService;

    /**
     * 关注事件处理服务
     */
    @Autowired(required = false)
    private SubscribeEventDispatchService subscribeEventDispatchService;

    /**
     * 关注事件处理服务
     */
    @Autowired(required = false)
    private UnsubscribeEventDispatchService unsubscribeEventDispatchService;

    /**
     * 定位事件处理服务
     */
    @Autowired(required = false)
    private LocationEventDispatchService locationEventDispatchService;

    /**
     * 自定义菜单事件处理服务
     */
    @Autowired(required = false)
    private CustomMenuEventDispatchService customMenuEventDispatchService;

    public WeChatCallbackDispatcher() {
        logger.debug("initializing WeChat callback dispatcher");
        //回调消息处理
        textMsgDispatchService = new DefaultTextMsgDispatchServiceImpl();
        voiceMsgDispatchService = new DefaultVoiceMsgDispatchServiceImpl();
        shortVideoMsgDispatchService = new DefaultShortVideoMsgDispatchServiceImpl();
        //回调事件处理
        subscribeEventDispatchService = new DefaultSubscribeEventDispatchServiceImpl();
        unsubscribeEventDispatchService = new DefaultUnsubscribeEventDispatchServiceImpl();
        locationEventDispatchService = new DefaultLocationEventDispatchServiceImpl();
        customMenuEventDispatchService = new DefaultCustomMenuEventDispatchServiceImpl();
    }

    private synchronized WeChatSecureMode getSecureMode() throws WeChatCallbackException {
        if (null == secureMode) {
            secureMode = new WeChatSecureMode(this.getApiConfig().getAppId(), this.getApiConfig().getEncodingAESKey());
        }
        return secureMode;
    }

    /**
     * 消息处理器
     */
    private BaseMsg dispatchMsg(CommonXML.MsgType msgType, TypeAnalyzingBean bean, String xml) throws WeChatCallbackException {
        BaseMsg retVal = null;

        try {
            switch (msgType) {
                case text:
                    TextMsg textMsg = XMLUtil.fromXML(xml, TextMsg.class);
                    retVal = textMsgDispatchService.dispatchMsg(textMsg);
                    break;
                case voice:
                    VoiceMsg voiceMsg = XMLUtil.fromXML(xml, VoiceMsg.class);
                    retVal = voiceMsgDispatchService.dispatchMsg(voiceMsg);
                    break;
                case shortvideo:
                    ShortVideoMsg shortVideoMsg = XMLUtil.fromXML(xml, ShortVideoMsg.class);
                    retVal = shortVideoMsgDispatchService.dispatchMsg(shortVideoMsg);
                    break;
                case event:
                    BaseEvent.EventType eventType = BaseEvent.EventType.getByType(bean.getEvent());
                    retVal = dispatchEvent(eventType, bean, xml);
                    break;
                case unknown:
                default:
                    logger.debug("other type of msg:{}", bean.getMsgType());
                    break;
            }
        } catch (IOException e) {
            throw new WeChatCallbackException(CallbackErrEnum.SysErr, e);
        }

        return retVal;
    }

    /**
     * 事件处理器
     */
    private BaseMsg dispatchEvent(BaseEvent.EventType eventType, TypeAnalyzingBean bean, String xml) throws WeChatCallbackException {
        BaseMsg retVal = null;

        try {
            switch (eventType) {
                case SubscribeEvent:
                case ScanEvent:
                    SubscribeEvent subscribeEvent = XMLUtil.fromXML(xml, SubscribeEvent.class);
                    retVal = subscribeEventDispatchService.dispatchEvent(subscribeEvent);
                    break;
                case UnsubscribeEvent:
                    UnsubscribeEvent unsubscribeEvent = XMLUtil.fromXML(xml, UnsubscribeEvent.class);
                    retVal = unsubscribeEventDispatchService.dispatchEvent(unsubscribeEvent);
                    break;
                case LocationEvent:
                    LocationEvent locationEvent = XMLUtil.fromXML(xml, LocationEvent.class);
                    retVal = locationEventDispatchService.dispatchEvent(locationEvent);
                    break;
                case CustomMenuEvent:
                    CustomMenuEvent customMenuEvent = XMLUtil.fromXML(xml, CustomMenuEvent.class);
                    retVal = customMenuEventDispatchService.dispatchEvent(customMenuEvent);
                    break;
                case unknown:
                default:
                    logger.debug("other type of event:{}", bean.getEvent());
                    break;
            }
        } catch (IOException e) {
            throw new WeChatCallbackException(CallbackErrEnum.SysErr, e);
        }
        return retVal;
    }

    /**
     * 从请求参数中抽取回调参数对象
     *
     * @param requestParams request 请求参数对象
     * @return
     */
    private WeChatCallbackQueryParam extractCallbackParam(Map<String, String[]> requestParams) {
        WeChatCallbackQueryParam param = new WeChatCallbackQueryParam();
        //抽取回调签名参数
        String[] vals = null;
        vals = requestParams.get(WeChatCallbackQueryParam.SIGNATURE_FIELD);

        if (ArrayUtils.isNotEmpty(vals))
            param.setSignature(vals[0]);

        //抽取时间戳参数
        vals = requestParams.get(WeChatCallbackQueryParam.TIMESTAMP_FIELD);
        if (ArrayUtils.isNotEmpty(vals)) {
            param.setTimestamp(vals[0]);
        }
        //抽取随机数参数
        vals = requestParams.get(WeChatCallbackQueryParam.NONCE_FIELD);
        if (ArrayUtils.isNotEmpty(vals)) {
            param.setNonce(vals[0]);
        }
        //抽取回显字符参数
        vals = requestParams.get(WeChatCallbackQueryParam.ECHOSTR_FIELD);
        if (ArrayUtils.isNotEmpty(vals)) {
            param.setEchostr(vals[0]);
        }
        //抽取消息发送方的openId
        vals = requestParams.get(WeChatCallbackQueryParam.OPEN_ID_FIELD);
        if (ArrayUtils.isNotEmpty(vals)) {
            param.setOpenId(vals[0]);
        }
        //抽取消息加解密方式
        vals = requestParams.get(WeChatCallbackQueryParam.ENCRYPT_TYPE_FIELD);
        if (ArrayUtils.isNotEmpty(vals)) {
            param.setEncryptType(vals[0]);
        }
        //抽取回调签名
        vals = requestParams.get(WeChatCallbackQueryParam.MSG_SIGNATURE_FIELD);
        if (ArrayUtils.isNotEmpty(vals)) {
            param.setMsgSignature(vals[0]);
        }
        return param;
    }

    /**
     * 计算不定参数的签名
     *
     * @param elements 参与计算签名的元素
     * @return
     */
    private String calcSignature(String... elements) {
        //按字典序排序
        Arrays.sort(elements);
        StringBuilder merge = new StringBuilder();
        for (String element : elements) {
            merge.append(element);
        }
        //计算sha1
        return DigestUtils.sha1Hex(merge.toString());
    }

    /**
     * 处理回调请求
     *
     * @param requestParams 请求对象的查询参数 {@code request.getParameterMap()}
     * @param callbackXML   请求体中抽取的内容 {@code request.getInputStream() -> String}
     * @return
     * @throws WeChatCallbackException
     * @throws IOException
     */
    public String dispatch(Map<String, String[]> requestParams, String callbackXML) throws WeChatCallbackException, IOException {
        if (MapUtils.isEmpty(requestParams)) {
            throw new IllegalArgumentException("回调请求头参数缺失");
        }
        //验证回调签名
        WeChatCallbackQueryParam param = this.extractCallbackParam(requestParams);
        if (logger.isDebugEnabled()) {
            logger.debug("received callback header params:{}", JSONUtil.toJSON(param));
        }
        //验证回调请求合法性
        String calcCallbackSign = this.calcSignature(apiConfig.getCallbackToken(), param.getTimestamp(), param.getNonce());
        if (!calcCallbackSign.equals(param.getSignature())) {
            throw new WeChatCallbackException(CallbackErrEnum.CallbackRequestSignErr);
        }
        //请求体中无字符,表示该请求需要返回空或者特定字符
        if (StringUtils.isBlank(callbackXML)) {
            //此时有可能是验证接口可用性,需要返回指定的响应代码
            logger.debug("received content is empty");
            return param.getEchostr();
        }
        //签名验证结束,开始处理业务
        //判断加密模式
        WeChatCallbackQueryParam.EncryptType encryptType = WeChatCallbackQueryParam.EncryptType.getEncrypTypeByValue(param.getEncryptType());
        //安全模式中解密出来的数据结构
        WeChatSecureMode.WeChatDecryptStruct decryptStruct = null;
        switch (encryptType) {
            case AES:
                //安全模式需要对请求消息解密
                //首先提取加密xml中的密文部分
                RxEncryptXML rxEncryptXML = XMLUtil.fromXML(callbackXML, RxEncryptXML.class);
                if (null == rxEncryptXML || StringUtils.isBlank(rxEncryptXML.getEncrypt())) {
                    throw new IllegalAccessError("加密消息无数据");
                }
                //拿到纯密文部分
                callbackXML = rxEncryptXML.getEncrypt();
                //安全模式验证消息签名，防止被篡改
                String calcMsgSignature = this.calcSignature(apiConfig.getCallbackToken(), param.getTimestamp(), param.getNonce(), callbackXML);
                if (!calcMsgSignature.equals(param.getMsgSignature())) {
                    throw new WeChatCallbackException(CallbackErrEnum.CallbackMsgSignErr);
                }
                //解密
                decryptStruct = this.getSecureMode().decryptMsg(callbackXML);
                callbackXML = decryptStruct.getContent();
                break;
            case NONE:
            default:
                //明文或兼容模式不需要对请求体本身解码
                break;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("received content:{}", callbackXML);
        }
        TypeAnalyzingBean baseMsg = XMLUtil.fromXML(callbackXML, TypeAnalyzingBean.class);
        CommonXML.MsgType msgType = CommonXML.MsgType.getByType(baseMsg.getMsgType());
        BaseMsg retMsg = dispatchMsg(msgType, baseMsg, callbackXML);
        //进行返回
        switch (encryptType) {
            case AES:
                //无消息返回空串
                if (null == retMsg) {
                    return null;
                }
                //将要返回的内容转换为字符串
                String plain = XMLUtil.toXML(retMsg);
                //加密返回数据
                String cipher = this.getSecureMode().encryptMsg(plain, decryptStruct.getRandomData(), decryptStruct.getAppId());
                TxEncryptXML txEncryptXML = new TxEncryptXML();
                txEncryptXML.setEncrypt(cipher);
                txEncryptXML.setMsgSignature(this.calcSignature(apiConfig.getCallbackToken(), param.getTimestamp(), param.getNonce(), cipher));
                txEncryptXML.setNonce(param.getNonce());
                txEncryptXML.setTimeStamp(Long.parseLong(param.getTimestamp()));
                String retEncryptedXML = XMLUtil.toXML(txEncryptXML);
                if (logger.isDebugEnabled()) {
                    logger.debug("return encrypted xml:{}", retEncryptedXML);
                }
                return retEncryptedXML;
            case NONE:
            default:
                //明文或兼容模式直接返回明文
                if (null == retMsg) {
                    return null;
                } else {
                    String retPlainXML = XMLUtil.toXML(retMsg);
                    if (logger.isDebugEnabled()) {
                        logger.debug("return plain xml:{}", retPlainXML);
                    }
                    return retPlainXML;
                }
        }
    }

    /**
     * 获取微信接入配置
     *
     * @return 微信接入配置
     */
    public WeChatAPIConfig getApiConfig() {
        return apiConfig;
    }

    /**
     * 设置微信接入配置
     *
     * @param apiConfig 微信接入配置
     * @throws WeChatCallbackException
     */
    public void setApiConfig(WeChatAPIConfig apiConfig) throws WeChatCallbackException {
        this.apiConfig = apiConfig;
    }

    /**
     * 获取文本消息处理服务
     *
     * @return 文本消息处理服务
     */
    public TextMsgDispatchService getTextMsgDispatchService() {
        return textMsgDispatchService;
    }

    /**
     * 设置文本消息处理服务
     *
     * @param textMsgDispatchService 文本消息处理服务
     */
    public void setTextMsgDispatchService(TextMsgDispatchService textMsgDispatchService) {
        this.textMsgDispatchService = textMsgDispatchService;
    }

    /**
     * 获取语音消息处理服务
     *
     * @return 语音消息处理服务
     */
    public VoiceMsgDispatchService getVoiceMsgDispatchService() {
        return voiceMsgDispatchService;
    }

    /**
     * 设置语音消息处理服务
     *
     * @param voiceMsgDispatchService 语音消息处理服务
     */
    public void setVoiceMsgDispatchService(VoiceMsgDispatchService voiceMsgDispatchService) {
        this.voiceMsgDispatchService = voiceMsgDispatchService;
    }

    /**
     * 获取短视频消息处理服务
     *
     * @return 短视频消息处理服务
     */
    public ShortVideoMsgDispatchService getShortVideoMsgDispatchService() {
        return shortVideoMsgDispatchService;
    }

    /**
     * 设置短视频消息处理服务
     *
     * @param shortVideoMsgDispatchService 短视频消息处理服务
     */
    public void setShortVideoMsgDispatchService(ShortVideoMsgDispatchService shortVideoMsgDispatchService) {
        this.shortVideoMsgDispatchService = shortVideoMsgDispatchService;
    }

    /**
     * 获取关注事件处理服务
     *
     * @return 关注事件处理服务
     */
    public SubscribeEventDispatchService getSubscribeEventDispatchService() {
        return subscribeEventDispatchService;
    }

    /**
     * 设置关注事件处理服务
     *
     * @param subscribeEventDispatchService 关注事件处理服务
     */
    public void setSubscribeEventDispatchService(SubscribeEventDispatchService subscribeEventDispatchService) {
        this.subscribeEventDispatchService = subscribeEventDispatchService;
    }

    /**
     * 获取关注事件处理服务
     *
     * @return 关注事件处理服务
     */
    public UnsubscribeEventDispatchService getUnsubscribeEventDispatchService() {
        return unsubscribeEventDispatchService;
    }

    /**
     * 设置关注事件处理服务
     *
     * @param unsubscribeEventDispatchService 关注事件处理服务
     */
    public void setUnsubscribeEventDispatchService(UnsubscribeEventDispatchService unsubscribeEventDispatchService) {
        this.unsubscribeEventDispatchService = unsubscribeEventDispatchService;
    }

    /**
     * 获取定位事件处理服务
     *
     * @return 定位事件处理服务
     */
    public LocationEventDispatchService getLocationEventDispatchService() {
        return locationEventDispatchService;
    }

    /**
     * 设置定位事件处理服务
     *
     * @param locationEventDispatchService 定位事件处理服务
     */
    public void setLocationEventDispatchService(LocationEventDispatchService locationEventDispatchService) {
        this.locationEventDispatchService = locationEventDispatchService;
    }

    /**
     * 获取自定义菜单事件处理服务
     *
     * @return 自定义菜单事件处理服务
     */
    public CustomMenuEventDispatchService getCustomMenuEventDispatchService() {
        return customMenuEventDispatchService;
    }

    /**
     * 设置自定义菜单事件处理服务
     *
     * @param customMenuEventDispatchService 自定义菜单事件处理服务
     */
    public void setCustomMenuEventDispatchService(CustomMenuEventDispatchService customMenuEventDispatchService) {
        this.customMenuEventDispatchService = customMenuEventDispatchService;
    }

}
