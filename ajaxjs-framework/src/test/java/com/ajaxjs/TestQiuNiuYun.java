package com.ajaxjs;

import org.junit.Test;

import com.ajaxjs.config.ConfigService;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

public class TestQiuNiuYun {
	@Test
	public void test() {
		ConfigService.load("D:\\project\\leidong\\src\\main\\resources\\site_config.json");

		String accessKey = ConfigService.getValueAsString("uploadFile.ObjectStorageService.QiuNiuYun.accessKey");
		String secretKey = ConfigService.getValueAsString("uploadFile.ObjectStorageService.QiuNiuYun.secretKey");
		String bucket = ConfigService.getValueAsString("uploadFile.ObjectStorageService.QiuNiuYun.bucket");

		Auth auth = Auth.create(accessKey, secretKey);
		String upToken = auth.uploadToken(bucket);

		UploadManager uploadManager = new UploadManager(new Configuration(Region.huanan()));
		// ...生成上传凭证，然后准备上传
		// 如果是Windows情况下，格式是 D:\\qiniu\\test.png
		String localFilePath = "C:\\Users\\admin\\Desktop\\l.png";
		// 默认不指定key的情况下，以文件内容的hash值作为文件名
		String key = null;

		try {
			Response response = uploadManager.put(localFilePath, key, upToken);
			// 解析上传成功的结果
			DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
			System.out.println(putRet.key);
			System.out.println(putRet.hash);

		} catch (QiniuException ex) {
			Response r = ex.response;
			System.err.println(r.toString());

			try {
				System.err.println(r.bodyString());
			} catch (QiniuException ex2) {
				// ignore
			}
		}
	}
}
