package com.ajaxjs.object_storage;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.net.http.NetUtil;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.web.UploadFileInfo;
import com.ajaxjs.web.mock.MockRequest;
import com.ajaxjs.web.mock.MockServletInputStream;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

public class TestQiuNiuYun {
//	@Test
	public void test() {
		ConfigService.load("D:\\project\\leidong\\WebContent\\META-INF\\site_config.json");

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

			Map<String, Object> map = JsonHelper.parseMap(response.bodyString());
			DefaultPutRet putRet = new DefaultPutRet();
			putRet.hash = map.get("hash").toString();
			putRet.key = map.get("key").toString();
//			DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
			System.out.println(putRet.key);
			assertNotNull(putRet.hash);

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

	@Test
	public void testUpload() throws IOException {
		ConfigService.load("D:\\project\\leidong\\WebContent\\META-INF\\site_config.json");

		UploadFileInfo uploadFileInfo = new UploadFileInfo();
		uploadFileInfo.maxSingleFileSize = 1024 * 50000; // 50 MB;
		uploadFileInfo.allowExtFilenames = new String[] { "txt", "png" };
		uploadFileInfo.isFileOverwrite = true;
		uploadFileInfo.saveFolder = "c:\\t2\\";

		HttpServletRequest request = MockRequest.mockRequest("foo", "upload");
		when(request.getMethod()).thenReturn("POST");
		when(request.getContentType()).thenReturn("multipart/form-data; boundary=" + NetUtil.BOUNDARY);

		Map<String, Object> map = new HashMap<>();
		map.put("name", "foo");
		map.put("file23", new File("C:\\temp\\l.png"));

		byte[] b = NetUtil.toFromData(map);
		when(request.getContentLength()).thenReturn(b.length);
		when(request.getInputStream()).thenReturn(new MockServletInputStream(b));

		QiNiuYunUploadFile uploadRequest = new QiNiuYunUploadFile(request, uploadFileInfo);
		assertNotNull(uploadRequest);

		uploadRequest.upload();
	}
}
