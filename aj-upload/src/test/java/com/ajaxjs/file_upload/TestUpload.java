package com.ajaxjs.file_upload;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ajaxjs.file_upload.s3.NsoHttpUpload;
import com.ajaxjs.file_upload.s3.OssUpload;
import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.io.Resources;

@ContextConfiguration(locations = { "classpath*:applicationContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestUpload {
	MockMvc mockMvc;

	@Autowired
	WebApplicationContext wac;

	@Before
	public void init() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	File file = new File(Resources.getResourcesFromClasspath("img.png"));

//	@Test
	public void testCommonUpload() throws Exception {
		byte[] bytes = FileHelper.openAsByte(file);
		String filenmae = "abldj75zav.png";
		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", filenmae, MediaType.MULTIPART_FORM_DATA_VALUE, bytes);
		ResultActions andDo = mockMvc.perform(multipart("/upload").file(mockMultipartFile)).andExpect(status().isOk()).andExpect(content().string(filenmae))
				.andDo(print());

		System.out.println(andDo);
		assertNotNull(andDo);
	}

	@Autowired
	NsoHttpUpload nsoHttpUpload;

	@Autowired(required = false)
	IFileUpload fileUpload; // 默认是网易云

	@Test
	public void testNso() {
		byte[] bytes = FileHelper.openAsByte(file);
		String filenmae = "abldj75zav.png";

		boolean uploadFile = nsoHttpUpload.upload(filenmae, bytes);
//		boolean uploadFile = fileUpload.upload(filenmae, bytes);
		assertTrue(uploadFile);
	}

//	@Autowired(required = false)
	OssUpload ossUpload;

//	@Test
	public void testOss() {
		byte[] bytes = FileHelper.openAsByte(file);
		String filename = "abldj75zav.png";

		assertTrue(ossUpload.upload(filename, bytes));
//		assertTrue(uploadFile);
	}
}
