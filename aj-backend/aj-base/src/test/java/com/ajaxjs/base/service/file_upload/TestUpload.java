package com.ajaxjs.base.service.file_upload;

import com.ajaxjs.base.BaseTest;
import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.io.Resources;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestUpload extends BaseTest {
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext wac;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    File file = new File(Resources.getResourcesFromClasspath("upload/img.png"));

    @Test
    public void testCommonUpload() throws Exception {
        byte[] bytes = FileHelper.openAsByte(file);
        String filename = "abldj75zav.png";
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", filename, MediaType.MULTIPART_FORM_DATA_VALUE, bytes);
        ResultActions andDo = mockMvc.perform(multipart("/upload/local").file(mockMultipartFile)).andExpect(status().isOk()).andExpect(content().string(filename))
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
        String filename = "abldj75zav.png";

        boolean uploadFile = nsoHttpUpload.upload(filename, bytes);
        assertTrue(uploadFile);
    }

    @Test
    public void testNsoWeb() throws Exception {
        byte[] bytes = FileHelper.openAsByte(file);
        String filename = "abldj75zav.png";
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", filename, MediaType.MULTIPART_FORM_DATA_VALUE, bytes);
        ResultActions andDo = mockMvc.perform(multipart("/upload/nso").file(mockMultipartFile)).andExpect(status().isOk()).andExpect(content().string(filename))
                .andDo(print());

        System.out.println(andDo);
    }

    @Autowired(required = false)
    OssUpload ossUpload;

    @Test
    public void testOss() {
        byte[] bytes = FileHelper.openAsByte(file);
        String filename = "abldj75zav.png";

        assertTrue(ossUpload.upload(filename, bytes));
    }
}
