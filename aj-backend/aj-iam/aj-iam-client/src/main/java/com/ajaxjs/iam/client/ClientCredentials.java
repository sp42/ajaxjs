package com.ajaxjs.iam.client;


import lombok.Data;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.ajaxjs.iam.jwt.JwtUtils;

/**
 * OAuth 2.0 中的客户端凭证（Client Credentials）授权模式请求
 */
@Data
public class ClientCredentials {
    /**
     * 获取 Token URL
     */
    private String tokenEndPoint;

    /**
     * 客户端 id
     */
    private String clientId;

    /**
     * 客户端密钥
     */
    private String clientSecret;

    private RestTemplate restTemplate;

    public static String encodeClient(String clientId, String clientSecret) {
        String clientAndSecret = clientId + ":" + clientSecret;

        return "Basic " + JwtUtils.encodeBase64(clientAndSecret);
    }

    public void requestWithBasic(String clientId, String clientSecret) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", encodeClient(clientId, clientSecret)); // 请求头

        MultiValueMap<String, Object> bodyParams = new LinkedMultiValueMap<>();
        bodyParams.add("grant_type", "client_credentials");

        ResponseEntity<String> responseEntity = restTemplate.exchange(tokenEndPoint, HttpMethod.POST, new HttpEntity<>(bodyParams, headers), String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {

        }
    }
}
