package com.ajaxjs.framework.spring.response;

import lombok.Data;

import java.nio.charset.StandardCharsets;

@Data
public class ResponseResult {
    private Integer status;

    private Integer total;

    private String errorCode;

    private String message;

    private String data;

    private final static String ERR = "{\"status\": %s, \"errorCode\": \"%s\", \"message\": \"%s\"}";

    private final static String OK = "{\"status\": %s, \"message\": \"%s\", \"data\": %s}";

    private final static String OK_PAGE = "{\"status\": %s, \"message\": \"%s\", \"total\": %s, \"data\": %s}";

    /**
     * 字符串原文输出。在返回的字符串前面加上这个字符串即可识别。
     */
    public static final String PLAIN_TEXT_OUTPUT = "PLAIN_TEXT_OUTPUT";

    @Override
    public String toString() {
        String str;

        if (errorCode != null)
            str = String.format(ERR, "0", errorCode, message == null ? "未知异常" : message);
        else if (data != null) {

            if (total != null)
                str = String.format(OK_PAGE, "1", message == null ? "分页数据" : message, total, data);
            else
                str = String.format(OK, "1", message == null ? "操作成功" : message, data);
        } else
            str = String.format(OK, "1", "null", message == null ? "操作成功" : message);

        return str;
    }


    /**
     * 方便 Spring 转换器使用
     *
     * @return
     */
    public byte[] getBytes() {
        return toString().getBytes(StandardCharsets.UTF_8);
    }
}
