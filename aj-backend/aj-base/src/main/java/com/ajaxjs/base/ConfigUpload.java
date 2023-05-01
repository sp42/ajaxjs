package com.ajaxjs.base;

import com.ajaxjs.oss.NsoHttpUpload;
import com.ajaxjs.oss.OssUpload;
import org.springframework.context.annotation.Bean;

/**
 * OSS 配置
 */
public class ConfigUpload {
    @Bean
    public NsoHttpUpload fileUpload() {
        return new NsoHttpUpload();
    }

	@Bean
	public OssUpload OssUpload() {
		return new OssUpload();
	}
}
