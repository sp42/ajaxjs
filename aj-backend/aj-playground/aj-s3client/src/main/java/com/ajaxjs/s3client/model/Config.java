package com.ajaxjs.s3client.model;

import lombok.Data;

/**
 * 配置
 */
@Data
public class Config {
    /**
     * 访问 API
     */
    private String endPoint;

    /**
     * 访问 Key
     */
    private String accessKey;

    /**
     * 访问密钥
     */
    private String secretKey;

    /**
     * 存储桶名称
     */
    private String bucketName;

    /**
     * 签名中的标识，每个厂商不同
     */
    private String remark;

    /**
     * 地区
     */
    private String region = "auto";
}
