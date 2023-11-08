package com.ajaxjs.net.ftp;

import org.junit.Test;

import java.io.IOException;

public class TestFTP {
    @Test
    public void testUpload() throws IOException {
        UploadFtp client = new UploadFtp("speedtest.tele2.net", 21);
        client.login("anonymous", "anonymous");
        client.upload("c:\\temp\\re.zip", "/upload/re.zip");
        client.closeServer();
    }

    @Test
    public void testDownload() throws IOException {
        UploadFtp ftp = new UploadFtp("speedtest.tele2.net", 21);
        ftp.login("anonymous", "anonymous");
        ftp.getFile("/1KB.zip", "c:\\temp\\re.zip");
    }
}
