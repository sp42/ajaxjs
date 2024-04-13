package com.ajaxjs.s3client;

import com.ajaxjs.s3client.model.Config;
import lombok.Data;

@Data
public abstract class BaseS3Client implements S3Client {
    public static final String DATE = "Date";

    public static final String AUTHORIZATION = "Authorization";

    public static final String HTTPS = "https://";

    private Config config;

    @Override
    public boolean putObject(String objectName, byte[] fileBytes) {
        return putObject(getConfig().getBucketName(), objectName, fileBytes);
    }

    @Override
    public boolean getObject(String objectName) {
        return getObject(getConfig().getBucketName(), objectName);
    }

    @Override
    public boolean deleteObject(String objectName) {
        return deleteObject(getConfig().getBucketName(), objectName);
    }
}
