package com.ajaxjs.web.rate_limiter;

import com.ajaxjs.web.rate_limiter.config.FileRuleConfigSource;
import com.ajaxjs.web.rate_limiter.config.RuleConfig;
import com.ajaxjs.web.rate_limiter.config.RuleConfigSource;
import com.ajaxjs.web.rate_limiter.model.ApiLimit;
import org.springframework.util.Assert;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 接口限流类
 * <p>
 * <a href="https://blog.csdn.net/noaman_wgs/article/details/107031814">...</a>
 * <a href="https://blog.csdn.net/noaman_wgs/article/details/107032061">...</a>
 * <a href="https://github.com/GenshenWang/inspire-demo/blob/master/Ch7_SentinelExe/src/main/java/com/wgs/sentinel/v2/parser/JsonRuleConfigParser.java">...</a>
 */
public class RateLimiter {
    private final RateLimitRule rule;

    /**
     * 每个API内存中存储限流计数器, key为 api:url
     */
    private final ConcurrentHashMap<String, RateLimitAlg> counters = new ConcurrentHashMap<>();

    public RateLimiter() {
        RuleConfigSource ruleConfigSource = new FileRuleConfigSource();
        RuleConfig ruleConfig = ruleConfigSource.load();
        Assert.isTrue(ruleConfig != null, "Load from yaml file, RuleConfig is null");
        this.rule = new RateLimitRule(ruleConfig);
    }

    /**
     * 判断接口是否限流
     *
     * @return true: 不限流; false: 限流
     */
    public boolean limit(String appId, String url) throws InterruptedException {
        // 接口未配置限流, 直接返回
        ApiLimit apiLimit = rule.getApiLimit(appId, url);

        if (apiLimit == null)
            return true;

        String counterKey = appId + ":" + url;
        RateLimitAlg rateLimitAlg = counters.get(counterKey);

        if (rateLimitAlg == null) {
            // 没有计数器, 就构造一个
            RateLimitAlg rateLimitCounterNew = new FixedWindowRateLimitAlg(apiLimit.getLimit());
            RateLimitAlg rateLimitCounterOld = counters.putIfAbsent(counterKey, rateLimitCounterNew);

            if (rateLimitCounterOld == null)
                rateLimitAlg = rateLimitCounterNew;
        }

        // 固定窗口统计, 判断是否超过限流阈值
        assert rateLimitAlg != null;
        return rateLimitAlg.tryAcquire();

    }

    public static void main(String[] args) throws InterruptedException {
        RateLimiter rateLimiter = new RateLimiter();

        for (int i = 0; i < 10; i++) {
            boolean b = rateLimiter.limit("app1", "/v1/user");
            System.out.println("/v1/user接口限流结果: " + b);
        }

        System.out.println("=====");

        for (int i = 0; i < 10; i++) {
            boolean b = rateLimiter.limit("app1", "/v1/order");
            System.out.println("/v1/order接口限流结果: " + b);
        }

        System.out.println("=====");
        for (int i = 0; i < 10; i++) {
            boolean b = rateLimiter.limit("app2", "/v1/login");
            System.out.println("/v1/login接口限流结果:" + b);
        }

    }
}