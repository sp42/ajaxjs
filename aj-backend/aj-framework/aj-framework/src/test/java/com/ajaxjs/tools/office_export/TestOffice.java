package com.ajaxjs.tools.office_export;

import com.ajaxjs.util.io.FileHelper;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TestOffice {
    @Test
    public void test() {
        File docxFile = new File("C:\\code\\car-short-rental\\src\\main\\resources\\short-contract-template.docx");
//        zip(docxFile, "c:\\temp\\output.docx", stream);
    }

    public static ByteArrayOutputStream p(String path) {
        File file = new File(path);

        try (FileInputStream fis = new FileInputStream(file); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1)
                bos.write(buffer, 0, len);

            return bos;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Test
    public void replaceXsl() {
        String newXml = "C:\\code\\car-short-rental\\src\\main\\resources\\META-INF\\resources\\short-trade-new.xml";

        Export e = new Export();
        e.setIsXsl(true);
        e.setOfficeZip("C:\\code\\car-short-rental\\src\\main\\resources\\short-trade.xlsx");
        e.setOutputPath("C:\\temp\\test.xlsx");
        e.zip(p(newXml));
//        e.zip(new ByteArrayServletOutputStream(p(newXml)));
    }
}
