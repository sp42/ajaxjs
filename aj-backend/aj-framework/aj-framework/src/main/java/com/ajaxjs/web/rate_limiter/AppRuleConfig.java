package com.ajaxjs.web.rate_limiter;

import lombok.Data;

import java.util.List;

@Data
public class AppRuleConfig {
    private String appId;

    private List<ApiLimit> limits;

    public AppRuleConfig() {
    }

    public AppRuleConfig(String appId, List<ApiLimit> apiLimitList) {
        this.appId = appId;
        this.limits = apiLimitList;
    }
}
