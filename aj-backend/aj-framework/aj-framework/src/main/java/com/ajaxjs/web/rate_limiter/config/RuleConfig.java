package com.ajaxjs.web.rate_limiter.config;

import com.ajaxjs.web.rate_limiter.model.AppRuleConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * - AppRuleConfig
 * -  ApiLimit
 * <p>
 * configs:          对应RuleConfig
 * appId: app-1    对应AppRuleConfig
 * limits:
 * api: /v1/user   对应ApiLimit
 * limit: 100
 * unit：60
 * api: /v1/order
 * limit: 50
 * appId: app-2
 * limits:
 * api: /v1/user
 * limit: 50
 * api: /v1/order
 * limit: 50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleConfig {
    private List<AppRuleConfig> configs;
}