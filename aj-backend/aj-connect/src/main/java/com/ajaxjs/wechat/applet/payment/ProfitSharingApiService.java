package com.ajaxjs.wechat.applet.payment;

import com.ajaxjs.util.ListUtils;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.util.map.MapTool;
import com.toway.newfleet.business.wx.applet_pay.merchant.MerchantConfig;
import com.toway.newfleet.business.wx.applet_pay.model.profit_sharing.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分账 API
 * <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter8_1_1.shtml">...</a>
 */
@Service
public class ProfitSharingApiService extends CommonService {
    private static final LogHelper LOGGER = LogHelper.getLog(ProfitSharingApiService.class);

    @Autowired
    private MerchantConfig mchCfg;

    /**
     * 请求分账
     * 微信订单支付成功后，商户发起分账请求，将结算后的资金分到分账接收方
     *
     * @param order 请求分账参数
     * @return 分账结果
     */
    public RequestOrderResult order(RequestOrder order) {
        String url = "/v3/profitsharing/orders";

        return AppletPayUtils.post(mchCfg, url, order, RequestOrderResult.class);
    }

    public static Map<String, String> ORDER_PROFIT = new HashMap<>();

    /**
     * 不同 AppId 的用户 请求分账。同一个 app 下的多个用户，用上面的那个方法
     *
     * @param orderInfo 订单信息，out_order_no 和 transaction_id 必填
     * @param receivers 请求分账参数列表
     * @return 分账结果
     */
    public List<RequestOrderResult> order(RequestOrder orderInfo, List<RequestOrder> receivers) {
        LOGGER.info("开始分账");

        List<RequestOrderResult> results = new ArrayList<>(receivers.size());

        for (int i = 0, j = receivers.size(); i < j; i++) {
            RequestOrder receiver = receivers.get(i);
            receiver.setTransaction_id(orderInfo.getTransaction_id());
            receiver.setUnfreeze_unsplit(false);

            // 第一分账是我们自己的订单号，第二次，第三次请求分账，out_order_no 参数是上一次返回的 order_id
            if (i == 0)
                receiver.setOut_order_no(orderInfo.getOut_order_no());

            RequestOrderResult result = order(receiver);
            ORDER_PROFIT.put(receiver.getOut_order_no(), orderInfo.getOut_order_no());
//            result.setOrder(receiver);
//            result.
            results.add(result);

            if ((i + 1) < j) { // 对下一次 set out_order_no
                if (result != null && result.getOrder_id() != null) {
                    receivers.get(i + 1).setOut_order_no(result.getOrder_id());
                } else
                    LOGGER.warning("前一笔分账失败");
            }
        }

        return results;
    }

    /**
     * 查询分账结果
     *
     * @param transactionId 微信订单号
     * @param outOrderNo    商户分账单号
     * @return 分账结果
     */
    public RequestOrderResult getOrders(String transactionId, String outOrderNo) {
        String url = "/v3/profitsharing/orders/" + outOrderNo + "?transaction_id=" + transactionId;

        return AppletPayUtils.get(mchCfg, url, RequestOrderResult.class);
    }

    /**
     * 请求分账回退
     *
     * @param order 分账回退参数
     * @return 回退结果
     */
    public ReturnOrderResult returnOrders(ReturnOrder order) {
        String url = "/v3/profitsharing/return-orders";

        return AppletPayUtils.post(mchCfg, url, order, ReturnOrderResult.class);
    }

    /**
     * 查询分账回退结果 API
     * <p>
     * 商户需要核实回退结果，可调用此接口查询回退结果
     * <p>
     * 注意：
     * • 如果分账回退接口返回状态为处理中，可调用此接口查询回退结果
     *
     * @param outReturnNo 商户回退单号 调用回退接口提供的商户系统内部的回退单号
     *                    示例值：R20190516001
     * @param outOrderNo  原发起分账请求时使用的商户系统内部的分账单号
     *                    示例值：P20190806125346
     * @return 回退结果
     */
    public ReturnOrderResult getReturnOrders(String outReturnNo, String outOrderNo) {
        String url = "/v3/profitsharing/return-orders/" + outReturnNo + "?out_order_no=" + outOrderNo;

        return AppletPayUtils.get(mchCfg, url, ReturnOrderResult.class);
    }

    /**
     * 解冻剩余资金
     * <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter8_1_5.shtml">...</a>
     *
     * @param transactionId 微信订单号
     * @param outOrderNo    商户分账单号
     * @param description   分账的原因描述，分账账单中需要体现
     * @return 不需要进行分账的订单，可直接调用本接口将订单的金额全部解冻给本商户
     */
    public RequestOrderResult unfreeze(String transactionId, String outOrderNo, String description) {
        String url = "/v3/profitsharing/orders";

        Map<String, String> params = ListUtils.hashMap("transaction_id", transactionId, "out_order_no", outOrderNo, "description", description);

        return AppletPayUtils.post(mchCfg, url, params, RequestOrderResult.class);
    }

    /**
     * 查询剩余待分金额
     *
     * @param transactionId 微信订单号
     * @return 订单剩余待分金额，整数，单元为分
     */
    public int getUnsplitMoney(String transactionId) {
        String url = "/v3/profitsharing/transactions/" + transactionId + "/amounts";
        Map<String, Object> map = AppletPayUtils.get(mchCfg, url, Map.class);

        if ((Boolean) map.get("isOk")) {
            return (int) map.get("unsplit_amount");
        } else {
            throw new RuntimeException(map.get("message").toString());
        }
    }

    /**
     * 添加分账接收方 API
     * <p>
     * 商户发起添加分账接收方请求，建立分账接收方列表。后续可通过发起分账请求，将分账方商户结算后的资金，分到该分账接收方
     * <p>
     * 注意：
     * • 商户需确保向微信支付传输用户身份信息和账号标识信息做一致性校验已合法征得用户授权
     *
     * @param receiver 分账接收方
     * @return 结果
     */
    public ReceiverAddResult addReceivers(ReceiverAdd receiver) {
        String url = "/v3/profitsharing/receivers/add";

        return AppletPayUtils.post(mchCfg, url, receiver, ReceiverAddResult.class);
    }

    /**
     * 删除分账接收方
     * 商户发起删除分账接收方请求。删除后，不支持将分账方商户结算后的资金，分到该分账接收方
     *
     * @param appid   微信分配的公众账号ID
     *                示例值：wx8888888888888888
     * @param type    分账接收方类型
     * @param account 分账接收方账号
     * @return 删除分账接收方结果
     */
    public ReceiverResult deleteReceivers(String appid, String type, String account) {
        String url = "/v3/profitsharing/receivers/delete";
        Map<String, String> params = ListUtils.hashMap("appid", appid, "type", type, "account", account);

        return AppletPayUtils.post(mchCfg, url, params, ReceiverResult.class);
    }

    /**
     * 分账动账通知
     * TODO
     *
     * @param params 参数
     */
    public void notify(Map<String, Object> params) {
    }

    /**
     * 申请分账账单
     * 微信支付按天提供分账账单文件，商户可以通过该接口获取账单文件的下载地址。
     *
     * @param billDate 账单日期 格式yyyy-MM-DD
     *                 仅支持三个月内的账单下载申请。
     *                 示例值：2019-06-11
     * @param tarType  压缩类型 不填则默认是数据流
     *                 枚举值：
     *                 GZIP：返回格式为.gzip的压缩包账单
     *                 示例值：GZIP
     * @return 账单下载地址
     */
    public String getBills(String billDate, String tarType) {
        String url = "/v3/profitsharing/transactions/bills?bill_date=" + billDate;

        if (tarType != null)
            url += "&tar_type=" + tarType;

        Map<String, Object> map = AppletPayUtils.get(mchCfg, url, Map.class);

        if ((Boolean) map.get("isOk")) {
            return map.get("download_url").toString();
        } else {
            throw new RuntimeException(map.get("message").toString());
        }
    }

    @Autowired
    BillService billService;

    /**
     * 下载账单
     *
     * @param url 账单 url，由 getBills() 返回
     */
    public void billDownload(String url) {
        billService.billDownload(url);
    }


    /**
     * 查询最大分账比例
     *
     * @return 子商户允许父商户分账的最大比例，比如20表示20%
     */
    public int getMax() {
        String url = "/v3/profitsharing/merchant-configs/" + mchCfg.getMchId();
        Map<String, Object> map = AppletPayUtils.get(mchCfg, url, Map.class);
        int maxRatio = (int) map.get("max_ratio");

        return maxRatio / 100;
    }

    @Override
    public MerchantConfig getMchCfg() {
        return mchCfg;
    }

    private final static String SUCCESS = "TRANSACTION.SUCCESS";

    public ResultNotify notifyCallback(Map<String, Object> params) {
        if (params.containsKey("event_type") && SUCCESS.equals(params.get("event_type"))) {
            String json = decrypt(params);

            Map<String, Object> map = JsonHelper.parseMap(json);
            ResultNotify bean = MapTool.map2Bean(map, ResultNotify.class, false, false);
            Map<String, Object> ro = (Map<String, Object>) map.get("receiver");
            RequestOrderReceivers receiver = MapTool.map2Bean(ro, RequestOrderReceivers.class, false, false);
            bean.setReceiver(receiver);

            return bean;
        }

        throw new IllegalArgumentException("返回参数失败！");
    }
}
