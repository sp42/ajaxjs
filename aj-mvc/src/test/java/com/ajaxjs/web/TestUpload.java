package com.ajaxjs.web;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

import com.ajaxjs.net.http.NetUtil;
import com.ajaxjs.util.ioc.EveryClass;
import com.ajaxjs.web.mock.MockRequest;
import com.ajaxjs.web.mock.MockServletInputStream;

public class TestUpload {
	@Test
	public void testUpload() throws IOException {
		UploadFileInfo uploadFileInfo = new UploadFileInfo();
		uploadFileInfo.maxSingleFileSize = 1024 * 50000; // 50 MB;
		uploadFileInfo.allowExtFilenames = new String[] { "txt", "log" };
		uploadFileInfo.isFileOverwrite = true;
		uploadFileInfo.saveFolder = "c:\\temp\\";

		HttpServletRequest request = MockRequest.mockRequest("foo", "upload");
		when(request.getMethod()).thenReturn("POST");
		when(request.getContentType()).thenReturn("multipart/form-data; boundary=" + NetUtil.BOUNDARY);

		Map<String, Object> map = new HashMap<>();
		map.put("name", "foo");
		String path = EveryClass.getResourcesFromClasspath("com\\ajaxjs\\web\\newfile.txt");
		map.put("file23", new File(path));

		byte[] b = NetUtil.toFromData(map);
		when(request.getContentLength()).thenReturn(b.length);
		when(request.getInputStream()).thenReturn(new MockServletInputStream(b));

		UploadFile uploadRequest = new UploadFile(request, uploadFileInfo);
		assertNotNull(uploadRequest);

		uploadRequest.upload();
	}
}
