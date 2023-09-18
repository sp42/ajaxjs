package com.ajaxjs.util.io;

import org.junit.Test;

public class TestZipHelper {
    @Test
    public void testFileZip() throws Exception {
        ZipHelper.zip("c:/temp/", "c:\\temp2\\bar\\test.zip");
    }

    //	@Test
    public void testSpeicalFileZip() throws Exception {
        ZipHelper.zip("c:\\temp", "c:\\temp2\\test.zip", file -> {
            System.out.println(file);
            if ("c:\\temp\\foo".equals(file.toString())) {
                return false;
            }
            return true;
        });
    }

    //	@Test
    public void testFileUnZip() throws Exception {
        ZipHelper.unzip("c:\\temp2\\", "c:\\temp2\\test.zip");
    }

    @Test
    public void testZip() {
        ZipHelper.zip("C:\\temp\\ajaxjs-security", "C:\\temp\\dd.zip");
    }

    @Test
    public void testIsZipFile() {
        boolean isZipFile = ZipHelper.isZipFile("C:\\temp\\short-contract-template.docx");
        System.out.println(isZipFile);
    }

}
