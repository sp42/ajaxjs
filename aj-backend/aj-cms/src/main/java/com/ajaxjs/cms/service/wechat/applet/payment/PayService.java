package com.ajaxjs.cms.service.wechat.applet.payment;

import com.ajaxjs.framework.BusinessException;
import com.ajaxjs.util.ObjectHelper;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.convert.EntityConvert;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.cms.service.wechat.applet.model.LoginSession;
import com.ajaxjs.cms.service.wechat.applet.payment.payment.PayResult;
import com.ajaxjs.cms.service.wechat.applet.payment.payment.PreOrder;
import com.ajaxjs.cms.service.wechat.applet.payment.payment.RequestPayment;
import com.ajaxjs.cms.service.wechat.common.PemUtil;
import com.ajaxjs.cms.service.wechat.common.RsaCryptoUtil;
import com.ajaxjs.cms.service.wechat.merchant.MerchantConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Map;

/**
 * 小程序支付业务
 */
@EqualsAndHashCode(callSuper = true)
@Service
@Data
public class PayService extends CommonService {
    private static final LogHelper LOGGER = LogHelper.getLog(PayService.class);

    @Autowired
    private MerchantConfig mchCfg;

    @Value("${wechat.merchant.payNotifyUrl}")
    private String appletPayNotifyUrl;

    /**
     * 下单
     * <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_1.shtml">...</a>
     *
     * @param session     会话
     * @param totalMoney  交易金额，单位是 分
     * @param outTradeNo  订单号
     * @param description 描述
     * @return 预支付交易会话标识
     */
    public String preOrder(LoginSession session, String appId, int totalMoney, String outTradeNo, String description) {
        return preOrder(session.getOpenId(), appId, totalMoney, outTradeNo, description);
    }

    public String preOrder(String openId, String appId, int totalMoney, String outTradeNo, String description) {
        Map<String, String> payer = ObjectHelper.hashMap("openid", openId);// 支付者
        Map<String, Integer> amount = ObjectHelper.hashMap("total", totalMoney); // 金额

        LOGGER.info(mchCfg.getMchId() + "::::::::" + appletPayNotifyUrl);

        if (!StringUtils.hasText(appletPayNotifyUrl))
            throw new IllegalArgumentException("appletPayNotifyUrl 不能为空");

        // 构建支付参数
        PreOrder p = new PreOrder();
        p.setAppid(appId);
        p.setMchid(mchCfg.getMchId());
        p.setOut_trade_no(outTradeNo);
        p.setDescription(description);
        p.setNotify_url(appletPayNotifyUrl);

        Map<String, Object> params = EntityConvert.bean2Map(p);
        params.put("amount", amount);
        params.put("payer", payer);
        params.put("settle_info", ObjectHelper.hashMap("profit_sharing", true));

        String url = "/v3/pay/transactions/jsapi";
        Map<String, Object> map = AppletPayUtils.postMap(mchCfg, url, params);

        if ((Boolean) map.get("isOk") && map.get("code") == null) {
            return map.get("prepay_id").toString();
        } else
            throw new BusinessException(map.get("message").toString());
    }

    /**
     * 微信支付订单号查询订单
     * <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_2.shtml">...</a>
     *
     * @param transactionId 微信支付订单号
     */
    public Map<String, Object> getOrderByTransactionId(String transactionId) {
        String url = "/v3/pay/transactions/id/" + transactionId + "?mchid=" + mchCfg.getMchId();

        return AppletPayUtils.get(mchCfg, url, Map.class);
    }

    /**
     * 商户订单号查询订单
     * <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_2.shtml">...</a>
     *
     * @param outTradeNo 商户订单号
     */
    public Map<String, Object> getOrderByOrderNo(String outTradeNo) {
        String url = "/v3/pay/transactions/out-trade-no/" + outTradeNo + "?mchid=" + mchCfg.getMchId();

        return AppletPayUtils.get(mchCfg, url, Map.class);
    }

    /**
     * 关闭订单
     * <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_3.shtml">...</a>
     *
     * @param outTradeNo 商户订单号
     */
    public void closeOrder(String outTradeNo) {
        String url = "/v3/pay/transactions/out-trade-no/" + outTradeNo + "/close";
        Map<String, String> params = ObjectHelper.hashMap("mchid", mchCfg.getMchId());
        AppletPayUtils.postMap(mchCfg, url, params);// 该接口是无数据返回的
    }

    private final static String SUCCESS = "TRANSACTION.SUCCESS";

    private final static String TRADE_STATE = "SUCCESS";

    /**
     * 用户支付后，微信通知我们的接口。
     * 客户端无须调用该接口，该接口由微信支付中心调用
     *
     * @param params 回调参数
     * @return 支付结果
     */
    public PayResult payCallback(Map<String, Object> params) {
        if (params.containsKey("event_type") && SUCCESS.equals(params.get("event_type"))) {
            // 支付成功
            String cert = decrypt(params);
            return json2PayResultBean(cert);

//            if (TRADE_STATE.equals(bean.getTrade_state())) {// 再次检查
//                // 业务逻辑判断是否收到钱
//                LOGGER.info("收到钱：" + bean.getPayer_total());
//
//                return BaseController.jsonOk();
//            } else
//                throw new NullPointerException("解密失败");
        }

        throw new IllegalArgumentException("返回参数失败！");
    }

    /**
     * 官方返回的 JSON 是嵌套的，现在将其扁平化
     *
     * @param json JSON
     * @return PayResult
     */
    private static PayResult json2PayResultBean(String json) {
        Map<String, Object> map = EntityConvert.json2map(json);
        PayResult bean = EntityConvert.map2Bean(map, PayResult.class, false, false);

        @SuppressWarnings("unchecked")
        Map<String, Object> amount = (Map<String, Object>) map.get("amount");
        bean.setTotal((int) amount.get("total"));
        bean.setPayer_total((int) amount.get("payer_total"));

        @SuppressWarnings("unchecked")
        Map<String, Object> payer = (Map<String, Object>) map.get("payer");
        bean.setPayerOpenId(payer.get("openid").toString());

        return bean;
    }

    /**
     * 传入预支付交易会话标识 id，生成小程序支付所需参数返回
     * package 修正，最后转换为 JSON 字符串
     *
     * @param prepayId 预支付交易会话标识
     * @return 小程序支付所需参数
     */
    public String getRequestPayment(String prepayId, String appId) {
        RequestPayment rp = new RequestPayment();
        rp.setTimeStamp(String.valueOf(System.currentTimeMillis() / 1000));
        rp.setNonceStr(StrUtil.getRandomString(10));
        rp.setPrepayIdPackage("prepay_id=" + prepayId);

        String sign = getSign(mchCfg.getPrivateKey(), rp, appId);
        rp.setPaySign(sign);

        Map<String, Object> map = EntityConvert.bean2Map(rp);
        map.put("package", rp.getPrepayIdPackage());
        map.remove("prepayIdPackage");

        return EntityConvert.map2json(map);
    }

    private String getSign(String privateKey, RequestPayment rp, String appId) {
        String sb = appId + "\n" +
                rp.getTimeStamp() + "\n" +
                rp.getNonceStr() + "\n" +
                rp.getPrepayIdPackage() + "\n";

        PrivateKey key = PemUtil.loadPrivateKeyByPath(privateKey);

        return RsaCryptoUtil.sign(key, sb.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public MerchantConfig getMchCfg() {
        return mchCfg;
    }
}
