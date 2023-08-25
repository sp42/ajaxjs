package com.ajaxjs.web.rate_limiter.config;

import java.io.InputStream;

/**
 * 资源解析, 支持Yaml、Xml、Properties文件解析
 */
public interface RuleConfigParser {
    RuleConfig parse(InputStream in);
}