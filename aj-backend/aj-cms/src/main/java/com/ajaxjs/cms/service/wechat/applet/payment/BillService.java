package com.ajaxjs.cms.service.wechat.applet.payment;

import com.ajaxjs.cms.service.wechat.merchant.HttpRequestWrapper;
import com.ajaxjs.cms.service.wechat.merchant.MerchantConfig;
import com.ajaxjs.net.http.Get;
import com.ajaxjs.net.http.HttpConstants;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 账单
 *
 * @author Frank Cheung
 */
@Service("WxBillService")
public class BillService {
//    @Autowired
    private MerchantConfig mchCfg;

    /**
     * 下载交易账单 <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_6.shtml">...</a>
     *
     * @param billDate 账单日期
     * @param billType 账单类型（可为 null）
     * @param tarType  压缩类型 不填则默认是数据流
     *                 枚举值：
     *                 GZIP：返回格式为.gzip的压缩包账单
     *                 示例值：GZIP
     * @return 账单下载地址
     */
    public String getTradeBill(String billDate, String billType, String tarType) {
        String url = "/v3/bill/tradebill?bill_date=" + billDate;

        if (billType != null)
            url += "&bill_type=" + billType;

        if (tarType != null)
            url += "&tar_type=" + tarType;

        Map<String, Object> map = AppletPayUtils.get(mchCfg, url, Map.class);

        if ((Boolean) map.get("isOk")) {
            return map.get("download_url").toString();
        } else {
            throw new RuntimeException(map.get("message").toString());
        }
    }

    /**
     * 下载资金账单 <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_7.shtml">...</a>
     *
     * @param billDate    账单日期
     * @param accountType 资金账户类型（可为 null）
     * @param tarType     压缩类型 不填则默认是数据流
     *                    枚举值：
     *                    GZIP：返回格式为.gzip的压缩包账单
     *                    示例值：GZIP
     * @return 账单下载地址
     */
    public String getFundFlowBill(String billDate, String accountType, String tarType) {
        String url = "/v3/bill/fundflowbill?bill_date=" + billDate;

        if (accountType != null)
            url += "&account_type=" + accountType;

        if (tarType != null)
            url += "&tar_type=" + tarType;

        Map<String, Object> map = AppletPayUtils.get(mchCfg, url, Map.class);

        if ((Boolean) map.get("isOk")) {
            return map.get("download_url").toString();
        } else {
            throw new RuntimeException(map.get("message").toString());
        }
    }

    /**
     * 下载账单
     *
     * @param url 账单 url，由 getBills() 返回
     */
    public void billDownload(String url) {
        HttpRequestWrapper rw = new HttpRequestWrapper(HttpConstants.GET, url);
        Map<String, Object> result = Get.api(url, AppletPayUtils.getSetHeadFn(mchCfg, rw));
        // 流数据处理
    }
}
