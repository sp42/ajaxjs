package com.ajaxjs.iam.client;

import com.ajaxjs.util.StrUtil;
import lombok.Data;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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

    public void request() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, Object> bodyParams = new LinkedMultiValueMap<>();
        bodyParams.add("client_id", clientId);
        bodyParams.add("client_secret", clientSecret);
        bodyParams.add("grant_type", "client_credentials");

        ResponseEntity<String> responseEntity = restTemplate.exchange(tokenEndPoint, HttpMethod.POST, new HttpEntity<>(bodyParams, headers), String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {

        }
    }

    public void requestWithBasic(String clientId, String clientSecret) {
        String clientAndSecret = clientId + ":" + clientSecret;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + StrUtil.base64Encode(clientAndSecret)); // 请求头

        MultiValueMap<String, Object> bodyParams = new LinkedMultiValueMap<>();
        bodyParams.add("grant_type", "client_credentials");

        ResponseEntity<String> responseEntity = restTemplate.exchange(tokenEndPoint, HttpMethod.POST, new HttpEntity<>(bodyParams, headers), String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {

        }
    }
}
