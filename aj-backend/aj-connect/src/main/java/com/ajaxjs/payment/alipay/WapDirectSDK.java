package com.ajaxjs.payment.alipay;

import com.ajaxjs.payment.alipay.model.*;

import java.util.Collections;
import java.util.List;

public class WapDirectSDK extends AbstractAlipay {
    private static String SIGN_WAP_RSA = "0001";

    private static String SIGN_WAP_MD5 = "MD5";


    public WapDirectSDK(AlipayConfig config) {
        super(config);
    }

    public AlipayRequestData makeSimpleWapTradeDirect(String tradeId, String subject, double total, String callBackUrl, String notifyUrl, String merchantUrl) {
        AlipayRequestData a = new AlipayRequestData();
        a.setString("service", "alipay.wap.trade.create.direct");
        a.setString("format", "xml");
        a.setString("v", "2.0");
        a.setString("partner", config.getPartnerId());
        a.setString("req_id", tradeId);
        a.setString("req_data", "direct_trade_create_req");
        a.setReqString("subject", subject);
        a.setReqString("out_trade_no", tradeId);
        a.setReqString("total_fee", String.valueOf(total));
        a.setReqString("seller_account_name", config.getSellerAccount());
//		a.setReqString(AlipayWapRequestCreateDict.REQUIRED_REQDATA.CALL_BACK_URL, callBackUrl);
//		a.setReqString(AlipayWapRequestCreateDict.OPTIONAL_REQDATA.NOTIFY_URL, notifyUrl);
//		a.setReqString(AlipayWapRequestCreateDict.OPTIONAL_REQDATA.MERCHANT_URL, merchantUrl);

        return a;
    }

    private AlipayRequestData makeWapAuthAndExecute(String token) {
        AlipayRequestData a = new AlipayRequestData();
        a.setString("service", "alipay.wap.auth.authAndExecute");
        a.setString("format", "xml");
        a.setString("v", "2.0");
        a.setString("partner", config.getPartnerId());
        a.setString("req_data", "auth_and_execute_req");
        a.setReqString("request_token", token);

        return a;
    }

    private List<StringPair> sign(List<StringPair> p) {
        String sign = null;

        if (preferRSA) {
            p.add(new StringPair("sec_id", SIGN_WAP_RSA));
            Collections.sort(p);
            sign = signRSA(p);
        } else {
            p.add(new StringPair("sec_id", SIGN_WAP_MD5));
            Collections.sort(p);
            sign = signMD5(p);
        }

        p.add(new StringPair("sign", sign));

        return p;
    }

    public String create(AlipayRequestData request) {
        List<StringPair> p = sign(request.getSortedParameters());

        try {
//            String auth = http.get("wappaygw.alipay.com", 443, "https", "/service/rest.htm?" + join(p, true, false));
            String auth = "";
            GroupStringPair params = parseQueryString(auth);
            decrypt(params);
            AlipayResponseData res = AlipayResponseData.parse(params);
            boolean verified = false;

            if (preferRSA && res.getString("sec_id").equals(SIGN_WAP_RSA))
                verified = verifyRSA(res.getString("sign"), res.getSortedParameters("sign"));
            else if (res.getString("sec_id").equals(SIGN_WAP_MD5))
                verified = verifyMD5(res.getString("sign"), res.getSortedParameters("sign"));

            if (!verified)
                return null;

            String token = res.getExtraString("request_token");
            if (token == null) {
//				LOG.trace("Cannot find token in response");
                return null;
            }

            p = sign(makeWapAuthAndExecute(token).getSortedParameters());

            return "https://wappaygw.alipay.com/service/rest.htm?" + join(p, true, false);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void decrypt(GroupStringPair params) {
        if (!preferRSA)
            return;

        String sec = params.get("sec_id");

        if (!SIGN_WAP_RSA.equals(sec))
            return;

        if (params.get("res_data") != null) {
            String value = params.get("res_data");
            params.update("res_data", decrypt(value));
        } else if (params.get("notify_data") != null) {
            String value = params.get("notify_data");
            params.update("notify_data", decrypt(value));
        }
    }

    public boolean verifyCallback(AlipayResponseData response) {
        List<StringPair> parameterList = response.getOrderedParameters("sign", "sign_type");
        // This parameter "sign_type" is not documented.
        String type = response.getString("sign_type");

        if (type.equals(SIGN_WAP_RSA))
            return verifyRSA(response.getString("sign"), parameterList);
        else if (type.equals(SIGN_WAP_MD5))
            return verifyMD5(response.getString("sign"), parameterList);
        else
            return false;
    }

    public boolean verifyNotify(AlipayResponseData response) {
        List<StringPair> parameterList = response.getOrderedParameters("sign");
        String type = response.getString("sec_id");

        if (type.equals(SIGN_WAP_RSA)) {
            String content = "service=" + response.getString("service") +
                    "&v=" + response.getString("v") +
                    "&sec_id=" + response.getString("sec_id") +
                    "&notify_data=" + response.getString("notify_data");

            return rsaVerify(content, response.getString("sign"));
            // return verifyRSA(response.getString("sign"), parameterList);
        } else if (type.equals(SIGN_WAP_MD5))
            return verifyMD5(response.getString("sign"), parameterList);
        else
            return false;
    }
}
