package com.ajaxjs.object_storage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.util.Encode;
import com.netease.cloud.ClientConfiguration;
import com.netease.cloud.auth.BasicCredentials;
import com.netease.cloud.services.nos.NosClient;
import com.netease.cloud.services.nos.model.GetObjectRequest;
import com.netease.cloud.services.nos.model.ListObjectsRequest;
import com.netease.cloud.services.nos.model.NOSObjectSummary;
import com.netease.cloud.services.nos.model.ObjectListing;
import com.netease.cloud.services.nos.model.ObjectMetadata;

public class TestNOS {
//  本地地址路径
	private File destinationFile = new File("C:\\Users\\admin\\Documents\\WeChat Files\\frank42a\\FileStorage\\File\\2020-05\\fb2c6ae731d9029b634326a8dc1a9962_t.gif");

//	@Test
	public void upload() {
		ConfigService.load("E:\\project\\aj-website\\WebContent\\META-INF\\site_config.json");

		String accessKey = ConfigService.get("uploadFile.ObjectStorageService.NOS.accessKey");
		String secretKey = ConfigService.get("uploadFile.ObjectStorageService.NOS.secretKey");
		String bucket = ConfigService.get("uploadFile.ObjectStorageService.NOS.bucket");
		String folder = ConfigService.get("uploadFile.ObjectStorageService.NOS.folder");

		ClientConfiguration conf = new ClientConfiguration();
		// 设置 NosClient 使用的最大连接数
		conf.setMaxConnections(200);
		// 设置 socket 超时时间
		conf.setSocketTimeout(10000);
		// 设置失败请求重试次数
		conf.setMaxErrorRetry(2);
		// 如果要用 https 协议，请加上下面语句
//		conf.setProtocol(Protocol.HTTPS);

		NosClient nosClient = new NosClient(new BasicCredentials(accessKey, secretKey), conf);
		nosClient.setEndpoint("nos-eastchina1.126.net");

		// 要上传文件的路径
		try {
			nosClient.putObject(bucket, folder + "/sss.gif", destinationFile);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
//		GetObjectRequest getObjectRequest = new GetObjectRequest(bucketname, objectname);
//		ObjectMetadata objectMetadata = nosClient.getObject(getObjectRequest, destinationFile);
	}

	@Test
	public void backup() {
		ConfigService.load("E:\\project\\aj-website\\WebContent\\META-INF\\site_config.json");

		String accessKey = ConfigService.get("uploadFile.ObjectStorageService.NOS.accessKey");
		String secretKey = ConfigService.get("uploadFile.ObjectStorageService.NOS.secretKey");
		String bucket = ConfigService.get("uploadFile.ObjectStorageService.NOS.bucket");
//		String folder = ConfigService.get("uploadFile.ObjectStorageService.NOS.folder");

		ClientConfiguration conf = new ClientConfiguration();
		// 设置 NosClient 使用的最大连接数
		conf.setMaxConnections(200);
		// 设置 socket 超时时间
		conf.setSocketTimeout(10000);
		// 设置失败请求重试次数
		conf.setMaxErrorRetry(2);
		// 如果要用 https 协议，请加上下面语句
//		conf.setProtocol(Protocol.HTTPS);

		NosClient nosClient = new NosClient(new BasicCredentials(accessKey, secretKey), conf);
		nosClient.setEndpoint("nos-eastchina1.126.net");

		ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
		listObjectsRequest.setBucketName(bucket);
		listObjectsRequest.setMaxKeys(50);
		ObjectListing listObjects = nosClient.listObjects(listObjectsRequest);
		List<NOSObjectSummary> listResult = new ArrayList<>();

		do {
			listResult.addAll(listObjects.getObjectSummaries());

			if (listObjects.isTruncated()) {
				ListObjectsRequest request = new ListObjectsRequest();
				request.setBucketName(listObjectsRequest.getBucketName());
				request.setMarker(listObjects.getNextMarker());
				listObjects = nosClient.listObjects(request);
			} else {
				break;
			}
		} while (listObjects != null);

		for (NOSObjectSummary s : listResult) {
			System.out.println(s.getKey());

		}

		download(nosClient, bucket, listResult.get(1));
	}

	static void download(NosClient nosClient, String bucket, NOSObjectSummary summary) {
		ObjectMetadata metadata = nosClient.getObjectMetadata(bucket, summary.getKey());
		String objectName = metadata.getObjectName();
		objectName = Encode.urlDecode(objectName);
		File destinationFile = new File("c:\\temp\\" + objectName);
		ObjectMetadata m = nosClient.getObject(new GetObjectRequest(bucket, objectName), destinationFile);
		System.out.println(m);
	}

}