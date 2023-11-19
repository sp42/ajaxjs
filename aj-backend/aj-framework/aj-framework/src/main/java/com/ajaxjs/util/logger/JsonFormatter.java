package com.ajaxjs.util.logger;

import com.ajaxjs.util.ObjectHelper;
import com.ajaxjs.util.convert.ConvertToJson;

import java.util.Map;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class JsonFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        // 设置日志记录的级别、消息和时间戳等信息
        Map<String, Object> map = ObjectHelper.hashMap("level", record.getLevel().getName(),
                "message", formatMessage(record), "timestamp", record.getMillis());

        // 获取异常信息，如果有的话
        Throwable throwable = record.getThrown();

        if (throwable != null)
            map.put("exception", throwable.toString());

        // 返回格式化后的JSON字符串
        return ConvertToJson.toJson(map) + System.lineSeparator();
    }
}
