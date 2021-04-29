package com.ajaxjs.thirdparty;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.framework.thirdparty.NsoHttpUpload;
import com.ajaxjs.framework.thirdparty.NsoHttpUploader;
import com.ajaxjs.net.http.NetUtil;
import com.ajaxjs.web.UploadFileInfo;
import com.ajaxjs.web.mock.MockRequest;
import com.ajaxjs.web.mock.MockServletInputStream;

public class TestNsoUpload {
//	@Test
	public void testHttpUpload() {
		ConfigService.load("c:\\project\\aj-website-site_config.json");
//		System.out.println(listBuk());
//		createEmptyFile("test.jpg");
		NsoHttpUpload.uploadFile("C:\\project\\ajaxjs-maven-global.xml");
	}

	@Test
	public void testUploader() throws IOException {
		ConfigService.load("c:\\project\\aj-website-site_config.json");

		UploadFileInfo uploadFileInfo = new UploadFileInfo();
		uploadFileInfo.maxSingleFileSize = 1024 * 50000; // 50 MB;
		uploadFileInfo.allowExtFilenames = new String[] { "txt", "java" };
		uploadFileInfo.isFileOverwrite = true;
		uploadFileInfo.saveFolder = "c:\\t2\\";

		HttpServletRequest request = MockRequest.mockRequest("foo", "upload");
		when(request.getMethod()).thenReturn("POST");
		when(request.getContentType()).thenReturn("multipart/form-data; boundary=" + NetUtil.BOUNDARY);

		Map<String, Object> map = new HashMap<>();
		map.put("name", "foo");
		map.put("file23", new File("C:\\temp\\serviceImpl\\InstitutionScoreServiceImpl.java"));

		byte[] b = NetUtil.toFromData(map);
		when(request.getContentLength()).thenReturn(b.length);
		when(request.getInputStream()).thenReturn(new MockServletInputStream(b));

		NsoHttpUploader uploadRequest = new NsoHttpUploader(request, uploadFileInfo);
		assertNotNull(uploadRequest);

		uploadRequest.upload();
	}
}
