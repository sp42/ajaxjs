package com.ajaxjs.storage.file;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;

/**
 * S3 客户端
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class S3Client extends AmazonS3Client {
	/**
	 * 桶名称
	 */
	private String bucketName;

	@SuppressWarnings("deprecation")
	public S3Client(String url, String accessKey, String secretkey, String bucketName) {
		super(new BasicAWSCredentials(accessKey, secretkey), new ClientConfiguration());
		this.bucketName = bucketName;
		setEndpoint(url);
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
}
