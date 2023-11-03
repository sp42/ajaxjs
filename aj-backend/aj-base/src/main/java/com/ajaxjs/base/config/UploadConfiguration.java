package com.ajaxjs.base.config;


import com.ajaxjs.base.service.file_upload.NsoHttpUpload;
import com.ajaxjs.base.service.file_upload.OssUpload;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OSS 配置
 */
@Configuration
public class UploadConfiguration {
    @Bean
    public NsoHttpUpload fileUpload() {
        return new NsoHttpUpload();
    }

	@Bean
	public OssUpload OssUpload() {
		return new OssUpload();
	}
}
