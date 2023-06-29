package com.ajaxjs.office;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    /**
     * Java文件下载中文名不显示&乱码
     * <a href="https://cloud.tencent.com/developer/article/1483095">...</a>
     *
     * @param fileName 文件名，可以包含中文的
     * @return 文件名
     */
    public static String encodeFileName(String fileName) {
        return new String(fileName.getBytes(), StandardCharsets.ISO_8859_1);
    }

    public static String now() {
        return new SimpleDateFormat("yyyy年M月d日").format(new Date());
    }

    public static String getFileName(String filePath) {
        int startIndex = filePath.lastIndexOf("/") + 1;
        int endIndex = filePath.lastIndexOf(".");

        return filePath.substring(startIndex, endIndex);
    }
}
