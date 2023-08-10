package com.ajaxjs.developertools.tools.mysql;

import com.ajaxjs.developertools.tools.LicenseCopyUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TestLicenseCopy {
    @Test
    public void test() {
        try {
            new LicenseCopyUtils().processLicenseHeader(new File("src/main/java"), new FileInputStream("license.txt"), "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
