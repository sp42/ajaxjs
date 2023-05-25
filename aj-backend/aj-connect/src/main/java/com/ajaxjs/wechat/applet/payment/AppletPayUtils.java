package com.ajaxjs.wechat.applet.payment;

import com.ajaxjs.framework.spring.DiContextUtil;
import com.ajaxjs.net.http.Get;
import com.ajaxjs.net.http.HttpEnum;
import com.ajaxjs.net.http.Post;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.util.map.MapTool;
import com.ajaxjs.wechat.merchant.HttpRequestWrapper;
import com.ajaxjs.wechat.merchant.MerchantConfig;
import com.ajaxjs.wechat.merchant.SignerMaker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 工具类
 */
public class AppletPayUtils {
    public final static String SCHEMA = "WECHATPAY2-SHA256-RSA2048";

    /**
     * 微信商户 API 前缀
     */
    public final static String API_DOMAIN = "https://api.mch.weixin.qq.com";

    /**
     * 设置签名 Token 到 HTTP 请求头
     *
     * @param conn      HTTP 连接对象
     * @param signToken 签名 Token
     */
    public static void setSign2Header(HttpURLConnection conn, String signToken) {
        conn.addRequestProperty("Authorization", SCHEMA + " " + signToken);
        conn.addRequestProperty("Accept", "application/json");
        conn.addRequestProperty("Content-Type", "application/json");

        ProfitSharingServiceImpl s = DiContextUtil.getBean(ProfitSharingServiceImpl.class);// 写死
        conn.addRequestProperty("Wechatpay-Serial", s.platformCertSerialNo);
    }

    /**
     * @param mchCfg 商户配置
     * @param r      请求接口的 HTTP 方法、URL 和 请求报文主体
     * @return 回调
     */
    public static Consumer<HttpURLConnection> getSetHeadFn(MerchantConfig mchCfg, HttpRequestWrapper r) {
        String signToken = new SignerMaker(mchCfg).getToken(r);// 得到签名

        return (HttpURLConnection conn) -> setSign2Header(conn, signToken);
    }

    /**
     * 元乘以 100，并四舍五入，并取整
     */
    private final static BigDecimal TO_FEN = new BigDecimal(100);

    /**
     * BigDecimal 转换为分
     *
     * @param price 价格，单位是 元
     * @return 价格，单位是 分
     */
    public static int bigDecimal2Fen(BigDecimal price) {
        return price.multiply(TO_FEN).setScale(0, RoundingMode.HALF_UP).intValue();
    }

    /**
     * 元转分，确保price保留两位有效数字
     * <a href="https://codeantenna.com/a/IF8eyoVGvN">...</a>
     *
     * @return 分
     */
    public static int changeY2F(double price) {
        DecimalFormat df = new DecimalFormat("#.00");
        price = Double.parseDouble(df.format(price));

        return (int) (price * 100);
    }

    public static <T> T post(MerchantConfig mchCfg, String url, Object params, Class<T> resultClz) {
        String rawJson = JsonHelper.toJson(params);
        HttpRequestWrapper rw = new HttpRequestWrapper(HttpEnum.POST, url, rawJson);

        System.out.println(":::请求参数：" + rawJson);
        Map<String, Object> result = Post.api(API_DOMAIN + url, rawJson, getSetHeadFn(mchCfg, rw));

        if (resultClz == null)
            return null;

        if (result.containsKey("code ") && result.containsKey("message"))
            result.put("isOk", false);// 表示失败
        else
            result.put("isOk", true);

        if (resultClz == Map.class)
            return (T) result;

        return MapTool.map2Bean(result, resultClz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(MerchantConfig mchCfg, String url, Class<T> resultClz) {
        HttpRequestWrapper rw = new HttpRequestWrapper(HttpEnum.GET, url);
        Map<String, Object> result = Get.api(API_DOMAIN + url, getSetHeadFn(mchCfg, rw));

        if (result.containsKey("code") && result.containsKey("message"))
            result.put("isOk", false);// 表示失败
        else
            result.put("isOk", true);

        if (resultClz == Map.class)
            return (T) result;
        else
            return MapTool.map2Bean(result, resultClz);
    }
}
