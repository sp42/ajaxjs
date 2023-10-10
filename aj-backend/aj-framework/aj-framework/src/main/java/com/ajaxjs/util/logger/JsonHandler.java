package com.ajaxjs.util.logger;

import java.util.logging.*;

public class JsonHandler extends Handler {
    @Override
    public void publish(LogRecord record) {
        // 格式化日志记录
        String formattedLog = getFormatter().format(record);

        // 将日志输出到控制台或其他目标
        System.out.print(formattedLog);
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }
}
