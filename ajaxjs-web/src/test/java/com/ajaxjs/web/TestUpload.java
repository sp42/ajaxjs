package com.ajaxjs.web;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

import com.ajaxjs.net.http.NetUtil;
import com.ajaxjs.web.mock.MockRequest;

public class TestUpload {

	/**
	 * 
	 * @author Administrator
	 *
	 */
	public static class MockServletInputStream extends ServletInputStream {
		private InputStream delegate;

		public MockServletInputStream(byte[] b) {
			delegate = new ByteArrayInputStream(b);
		}

		@Override
		public int read() throws IOException {
			return delegate.read();
		}

		@Override
		public boolean isFinished() {
			return false;
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void setReadListener(ReadListener readListener) {
		}
	}

	@Test
	public void testUpload() throws IOException {
		UploadFileInfo uploadFileInfo = new UploadFileInfo();
		uploadFileInfo.maxSingleFileSize = 1024 * 50000; // 50 MB;
		uploadFileInfo.allowExtFilenames = new String[] { "txt", "log" };
		uploadFileInfo.isFileOverwrite = true;
		uploadFileInfo.saveFolder = "c:\\t2\\";

		HttpServletRequest request = MockRequest.mockRequest("foo", "upload");
		when(request.getMethod()).thenReturn("POST");
		when(request.getContentType()).thenReturn("multipart/form-data; boundary=" + NetUtil.BOUNDARY);

		Map<String, Object> map = new HashMap<>();
		map.put("name", "foo");
		map.put("file23", new File("C:\\temp\\newfile.txt"));
//		map.put("file2", new File("C:\\temp\\hhh2018-11-07.log"));

		byte[] b = NetUtil.toFromData(map);
		when(request.getContentLength()).thenReturn(b.length);
		when(request.getInputStream()).thenReturn(new MockServletInputStream(b));

		UploadFile uploadRequest = new UploadFile(request, uploadFileInfo);
		assertNotNull(uploadRequest);

		uploadRequest.upload();

	}
}
