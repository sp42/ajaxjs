package com.ajaxjs.web.rate_limiter.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public class YamlRuleConfigParser implements RuleConfigParser {
    @Override
    public RuleConfig parse(InputStream in) {
        if (in != null)
            return new Yaml().loadAs(in, RuleConfig.class);

        return null;
    }
}