package com.ajaxjs.web.rate_limiter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Api限流规则
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiLimit {
    private static final int DEFAULT_UNIT_SECONDS = 1;

    private String api;

    private int limit;

    private int unit = DEFAULT_UNIT_SECONDS;

    public ApiLimit(String api, int limit) {
        this.api = api;
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "ApiLimit{api='" + api + '\'' + ", limit=" + limit + ", unit=" + unit + '}';
    }
}