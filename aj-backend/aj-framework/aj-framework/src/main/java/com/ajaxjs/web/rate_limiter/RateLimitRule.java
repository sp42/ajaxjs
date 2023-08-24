package com.ajaxjs.web.rate_limiter;

import java.util.HashMap;
import java.util.List;

/**
 * @author: wanggenshen
 * @date: 2020/6/23 00:12.
 * @description: 支持快速查询 ApiLimit
 * <p>
 * TODO:
 * (1) 精准匹配优化: 二分查找算法优化
 * (2) 支持前缀匹配: 使用Trie树实现
 * (3) 支持模糊匹配: 实现难度较高
 */
public class RateLimitRule {
    /**
     * key : appId + api, value: limit
     */
    private HashMap<String, ApiLimit> map = new HashMap<>();

    public RateLimitRule(RuleConfig ruleConfig) {
        List<AppRuleConfig> configs = ruleConfig.getConfigs();
        configs.forEach(appRuleConfig -> {
            String appId = appRuleConfig.getAppId();
            appRuleConfig.getLimits().forEach(apiLimit -> {
                String key = appId + ":" + apiLimit.getApi();
                map.put(key, apiLimit);
            });
        });
    }

    public ApiLimit getApiLimit(String appId, String api) {
        return map.get(appId + ":" + api);
    }
}