package com.ajaxjs.web.rate_limiter;

import com.ajaxjs.web.rate_limiter.config.RuleConfig;
import com.ajaxjs.web.rate_limiter.model.ApiLimit;

import java.util.HashMap;

/**
 * 支持快速查询 ApiLimit
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
    private final HashMap<String, ApiLimit> map = new HashMap<>();

    public RateLimitRule(RuleConfig ruleConfig) {
        ruleConfig.getConfigs().forEach(appRuleConfig -> {
            String appId = appRuleConfig.getAppId();
            appRuleConfig.getLimits().forEach(apiLimit -> map.put(appId + ":" + apiLimit.getApi(), apiLimit));
        });
    }

    public ApiLimit getApiLimit(String appId, String api) {
        return map.get(appId + ":" + api);
    }
}