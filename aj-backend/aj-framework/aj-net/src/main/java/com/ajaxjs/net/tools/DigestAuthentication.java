package com.ajaxjs.net.tools;

import com.ajaxjs.util.StringUtil;
import lombok.Data;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Data
public class DigestAuthentication implements Filter {
    /**
     * 用户名，你可以改为你配置的
     */
    private String userName = "usm";

    /**
     * 密码，你可以改为你配置的
     */
    private String password = "password";

    /**
     *
     */
    private String authMethod = "auth";

    /**
     *
     */
    private String realm = "example.com";

    public String nonce;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("HTTP DigestAuthentication……");

        // 定时器，每分钟刷新 nonce
        nonce = calculateNonce();

//		SetTimeout.timeout(() -> {
//			System.out.println("刷新 Nonce....");
////			log("刷新 Nonce....");
//			nonce = calculateNonce();
//		}, 1, 1);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException {
        authenticate((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    @Override
    public void destroy() {
    }

    protected void authenticate(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws ServletException {
        resp.setContentType("text/html;charset=UTF-8");
        String authHeader = req.getHeader("Authorization");

        try {
            if (StringUtil.hasText(authHeader)) {
                if (authHeader.startsWith("Digest")) {
                    // parse the values of the Authentication header into a hashmap
                    Map<String, String> headerValues = parseHeader(authHeader);
                    String method = req.getMethod();
                    String ha1 = StringUtil.md5(userName + ":" + realm + ":" + password);
                    String ha2;
                    String qop = headerValues.get("qop");
                    String reqURI = headerValues.get("uri");

                    if (StringUtil.hasText(qop) && qop.equals("auth-int")) {
                        String requestBody = "";
                        try (InputStream in = req.getInputStream()) {
                            StringUtil.byteStream2string(in);
                        }

                        String entityBodyMd5 = StringUtil.md5(requestBody);
                        ha2 = StringUtil.md5(method + ":" + reqURI + ":" + entityBodyMd5);
                    } else
                        ha2 = StringUtil.md5(method + ":" + reqURI);

                    String serverResponse;

                    if (StringUtil.hasText(qop)) {
//						String domain = headerValues.get("realm");
                        String nonceCount = headerValues.get("nc");
                        String clientNonce = headerValues.get("cnonce");

                        serverResponse = StringUtil.md5(ha1 + ":" + nonce + ":" + nonceCount + ":" + clientNonce + ":" + qop + ":" + ha2);
                    } else
                        serverResponse = StringUtil.md5(ha1 + ":" + nonce + ":" + ha2);

                    String clientResponse = headerValues.get("response");

                    assert serverResponse != null;
                    if (!serverResponse.equals(clientResponse)) {
                        show401(resp);
                        return;
                    }
                } else {
                    resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, " This Servlet only supports Digest Authorization");
                    return;
                }
            } else {
                show401(resp);
                return;
            }

            // allows to go
            chain.doFilter(req, resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void show401(HttpServletResponse resp) throws IOException {
        resp.addHeader("WWW-Authenticate", getAuthenticateHeader());
        resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

    /**
     * 解析 Authorization 头，将其转换为一个 Map
     * Gets the Authorization header string minus the "AuthType" and returns a hashMap of keys and values
     */
    private static Map<String, String> parseHeader(String header) {
        // separate  out the part of the string which tells you which Auth scheme is it
        String headerWithoutScheme = header.substring(header.indexOf(" ") + 1).trim();
        String[] keyValue = headerWithoutScheme.split(",");
        Map<String, String> values = new HashMap<>();

        for (String keyVal : keyValue) {
            if (keyVal.contains("=")) {
                String key = keyVal.substring(0, keyVal.indexOf("="));
                String value = keyVal.substring(keyVal.indexOf("=") + 1);
                values.put(key.trim(), value.replaceAll("\"", "").trim());
            }
        }

        return values;
    }

    /**
     * 生成认证的 HTTP 头
     *
     * @return HTTP 头
     */
    private String getAuthenticateHeader() {
        String header = "";

        header += "Digest realm=\"" + realm + "\",";
        if (StringUtil.hasText(authMethod))
            header += "qop=" + authMethod + ",";

        header += "nonce=\"" + nonce + "\",";
        header += "opaque=\"" + StringUtil.md5(realm + nonce) + "\""; // 域名跟 nonce 的 md5 = Opaque

        return header;
    }

    /**
     * 根据时间和随机数生成 nonce
     * <p>
     * Calculate the nonce based on current time-stamp up to the second, and a random seed
     */
    public static String calculateNonce() {
        String now = new SimpleDateFormat("yyyy:MM:dd:hh:mm:ss").format(new Date());

        return StringUtil.md5(now + new Random(100000).nextInt());
    }
}
