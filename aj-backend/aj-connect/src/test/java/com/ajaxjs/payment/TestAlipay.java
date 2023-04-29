package com.ajaxjs.payment;

import com.ajaxjs.payment.alipay.PCDirectSDK;
import com.ajaxjs.payment.alipay.model.AlipayConfig;
import com.ajaxjs.payment.alipay.model.AlipayRequestData;
import com.ajaxjs.payment.alipay.model.StringPair;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class TestAlipay {
    @Test
    public void test() {
        AlipayConfig cfg = new AlipayConfig();
        cfg.setPartnerId("2021001160631867");
        cfg.setMd5key("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1wZOc4LDr2xOxEJKXBMFORvDXw9qcvXikKhd+TkNJ/2pSWt36aZwo+E2XwQdm0EBZ9/APNaXsS2ijTM4j3E/Dmu6LA7vsHAsoJdZZ1SwxdpOuNHW4XccgFVNFMGLxbV9kiQOcRTg8olLd5dPy207FhEMpUYhYMtnb/xN72xF2XEXZv2DMKAQX4GfFnF36YdvXxJv+2DCoc4U4yos3aniwmyQO2rFVqk2eGwKR9BdRTYwpQxBbwLTblrKtym2Pi6zOPp5k0gIChwKPNcSc3C3FGpzcHeKfcVkzki3mHIUwtjLPoTlglApQz/O7gFbbIlqbPkBQsT0KKPqZw1uNbQtVwIDAQAB");
        cfg.setSellerAccount("2021001160631867");
        cfg.setMyPrivateKey( "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDXBk5zgsOvbE7EQkpcEwU5G8NfD2py9eKQqF35OQ0n/alJa3fppnCj4TZfBB2bQQFn38A81pexLaKNMziPcT8Oa7osDu+wcCygl1lnVLDF2k640dbhdxyAVU0UwYvFtX2SJA5xFODyiUt3l0/LbTsWEQylRiFgy2dv/E3vbEXZcRdm/YMwoBBfgZ8WcXfph29fEm/7YMKhzhTjKizdqeLCbJA7asVWqTZ4bApH0F1FNjClDEFvAtNuWsq3KbY+LrM4+nmTSAgKHAo81xJzcLcUanNwd4p9xWTOSLeYchTC2Ms+hOWCUClDP87uAVtsiWps+QFCxPQoo+pnDW41tC1XAgMBAAECggEAC4NN7gdhXOIqsX1vJ9Do5a7j3hIgO/mIYQByz6ZLWwnM5552dC8yNsQXOi0pPKfVYG9thpRi2Ny9qFvGun9wxd/kvvkpwkOvUAXqhorVUDKpJmIt5DKRdxmaH/Ng/XwDNjDyCQH6LWJGhSe34Qd/M2ZETBEeNmWNq0RSE/CzKLo5RvSCVjG8pbhh9ckU9MdNknadMek/maOdy0zTAnRvVW1XiKD5GKPGbzvTyYK7VZZU5n9PZGv5jqCQuWchGwpVtM5R11t002/duonHPknwtMjz10CjXQaQwZtmUIgz2ZoFasswqIC80Jtwc5+ykt7rNWqRwvmgEi5m2bTZPhVhcQKBgQDzyRYjlU2vxY7D6K34GNLGH2pr96rVRgGqMgGsVDQpE4Sjz/kUxZpxGCYYMmHEEe5Eh8nMxJlUMJYxDCxnigEtFqN/p5gXO+/0mC9ZtB5snc3ohCDTtT3qxHZNPE6YEaeod41SFifhaUmn0RKLuSjnsJ59yfa21qNtE5GbGzU6fwKBgQDhzFGl4l/1ZZCJKi+Qmus0wZN3nDpNvZ2e7yAERhb4zLIDLxK41r1GSZCmejfkr4xRm4egF/bQGtA/WgUTmQGfldN2sp1S3XE76kpa2VpjpybR93dHsm3P95Jd0QxVF3wPPRw9SOQeBdyv5kdKqWtNYYpb9qAONJEC+OUs/USxKQKBgH8cGwIEqBP9Axpc6NCSSNvqp3eSFC3NqbKPDlZHNUcxmRg8qRfOjF9pgvtc0aME4mIzFuJAo93E05rRSRnXWBOlWSyHOwLqSowTCaVRCAgEI8eXlPIt7xHIzBIrXqNlmTB63WGuka6zUidBGNVnfs+HRKw7sPrWYTHPYT0pa5CLAoGAOKmLO7OJHQ21rZYWhZU6vOxu0y8zOlsA+OdNA4WLMjg726yuaBALQ14mnIBDEnIzJi9/Y19gImz4IU23xGk7DQ8OF9VOwYKsbbqnmhC9LBDAHySYnDdccznPUxeonpncGf7d3UszY41ZChMhhMzrDmoRzG4hUC9EzJgk+ps757ECgYBgNw37bntWZA0+Ourl1L5eM8WAZamZKPqw9w46hmWABwDWcLXdw30LTukijblyf1dFlYPr6CIWoe3jzEzOCUhxHiXJUi9/NyJ+YPPe0LtI05Q1pDxBScg7QOAwfo7gX5ms1VmXdoX7+mGSw5IJCmD11QqhxVQi9/6JbQDc1hX1uA==");
        cfg.setAlipayPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArJS6ur3uqhpv6z3ismnmGv4P2p5r3aJ/jOG6KiY9HoFQGvScBVdirsoKsdiliCbaF4uHP03/rFxdsB2jESzXNJFNT6SU9RFs5uqWLMBVzu9SJRkNteBL6+aXPeIdhN0Jn/9zsRk2kJA7LwP9mt7cIACKvGcllll5R3VUZS/X/Lbmnu0Kr06kqegrrVDMq8hASaqZggiwyia/3eDn70M6VAo9dWb92pkKj/JKVZ8CzxFTr6eORaJPsTo2o9ChnlGwB5S6+KR+jLwI5hFgtqfFm5Ktj85nwZH3aEmEtEMVw32hiXLQrpI3C/+uoqVc82X4SyQC97AQxOc7Sh65ogCNawIDAQAB");

        PCDirectSDK pcSdk = new PCDirectSDK(cfg);

        String dt = new SimpleDateFormat("yyyyMMddHHMM").format(new Date());
        String tradeId = "TEST_TRADE_DEMO_" + dt + String.format("_%02d", 878787);
        // 支付时显示的名称
        String subject = "AlipayJavaSDK测试";

        // 金额,0.01是人民币1分.
        double total = 0.01;

        String notifyURL = "http://oneskill.com/alipay-java-sdk-demo/pc/notify";
        String errorNotifyURL = "http://oneskill.com/alipay-java-sdk-demo/pc/error-notify";
        String returnURL = "https://oneskill.com/alipay-java-sdk-demo/pc/return";

        AlipayRequestData alipayReq = pcSdk.makeSimpleDirectPayRequest(tradeId, subject, total, notifyURL, returnURL, errorNotifyURL);
        List<StringPair> alipayFields = pcSdk.create(alipayReq);
        String alipayActionUrl = pcSdk.getActionURL();

        StringBuilder html = new StringBuilder();
        html.append("<html><body>" + "<form action=\"" + alipayActionUrl + "\" method=\"POST\">\n" + "\t\t<table>\n" + "\t\t\t<tr>\n" + "\t\t\t\t<th>KEY</th>\n"
                + "\t\t\t\t<th>VALUE</th>\n" + "\t\t\t</tr>\n");

        for (StringPair field : alipayFields) {
            html.append("\t\t\t<tr>\n" + "\t\t\t\t<td>" + field.getFirst() + "</td>\n" + "\t\t\t\t<td><input type=\"text\" name=\"" + field.getFirst() + "\"\n"
                    + "\t\t\t\t\tvalue=\"" + field.getSecond() + "\" /></td>\n" + "\t\t\t</tr>\n");
        }
        html.append("\t\t</table>\n" + "\t\t<input type=\"submit\" value=\"Go to Alipay.com\" />\n" + "\t</form>");

        html.append("<hr />");
        html.append("</body></html>");

        System.out.println(html);

        assertNotNull(pcSdk);
    }
}
