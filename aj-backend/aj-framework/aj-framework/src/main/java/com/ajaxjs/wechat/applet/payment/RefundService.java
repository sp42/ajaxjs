package com.ajaxjs.wechat.applet.payment;


import com.ajaxjs.util.ObjectHelper;
import com.ajaxjs.util.convert.EntityConvert;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.wechat.applet.payment.payment.RefundNotifyResult;
import com.ajaxjs.wechat.applet.payment.payment.RefundResult;
import com.ajaxjs.wechat.applet.payment.payment.RefundResultAmount;
import com.ajaxjs.wechat.merchant.MerchantConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 退款处理
 */
@Service
public class RefundService extends CommonService {
    private static final LogHelper LOGGER = LogHelper.getLog(RefundService.class);

    @Autowired
    private MerchantConfig mchCfg;
    @Value("${wechat.merchant.refundNotifyUrl}")
    private String refundNotifyUrl;

    /**
     * 申请退款 <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_9.shtml">...</a>
     *
     * @param transactionId 微信支付订单号
     * @param refundMoney   退款金额
     * @param totalMoney    原订单金额
     * @param outRefundNo   商户退款单号
     * @param reason        退款原因
     */
    public RefundResult refund(String transactionId, int refundMoney, int totalMoney, Long outRefundNo, String reason) {
        Map<String, Object> params = new HashMap<>();
        params.put("transaction_id", transactionId);
        params.put("notify_url", refundNotifyUrl);
        params.put("out_refund_no", outRefundNo);
        params.put("reason", reason);
        params.put("amount", ObjectHelper.hashMap("refund", refundMoney, "total", totalMoney, "currency", "CNY"));

        String url = "/v3/refund/domestic/refunds";
        Map<String, Object> map = AppletPayUtils.postMap(mchCfg, url, params);
        RefundResult bean = EntityConvert.map2Bean(map, RefundResult.class, false, false);
        RefundResultAmount amount = EntityConvert.map2Bean((Map<String, Object>) map.get("amount"), RefundResultAmount.class, false, false);
        bean.setAmount(amount);

        return bean;
    }

    /**
     * 查询单笔退款
     * <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_10.shtml">...</a>
     *
     * @param outRefundNo 商户退款单号日期
     * @return 退款结果
     */
    public Map<String, Object> queryRefund(String outRefundNo) {
        String url = "/v3/refund/domestic/refunds/" + outRefundNo;
        return AppletPayUtils.get(mchCfg, url, Map.class);
    }

    private final static String SUCCESS = "REFUND.SUCCESS";

    /**
     * 退款结果通知
     * <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_11.shtml">...</a>
     *
     * @param params 回调参数
     * @return 支付结果
     */
    public RefundNotifyResult refundNotifyCallback(Map<String, Object> params) {
        if (params.containsKey("event_type") && SUCCESS.equals(params.get("event_type"))) {
            // 退款成功
            String json = decrypt(params);

            Map<String, Object> map = EntityConvert.json2map(json);
            RefundNotifyResult bean = EntityConvert.map2Bean(map, RefundNotifyResult.class, false, false);

            @SuppressWarnings("unchecked")
            Map<String, Object> amount = (Map<String, Object>) map.get("amount");
            bean.setTotal((Integer) amount.get("total"));
            bean.setRefund((Integer) amount.get("refund"));
            bean.setPayer_total((Integer) amount.get("payer_total"));
            bean.setPayer_refund((Integer) amount.get("payer_refund"));

            return bean;
        }

        throw new IllegalArgumentException("返回参数失败！");
    }

    @Override
    public MerchantConfig getMchCfg() {
        return mchCfg;
    }
}
