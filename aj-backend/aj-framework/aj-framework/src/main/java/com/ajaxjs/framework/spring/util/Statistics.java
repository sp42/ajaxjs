package com.ajaxjs.framework.spring.util;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
class Statistics {
    private String beanName;

    private long beforeInstantiationTime;

    private long afterInstantiationTime;

    private long beforeInitializationTime;

    private long afterInitializationTime;

    public long calculateTotalCostTime() {
        return calculateInstantiationCostTime() + calculateInitializationCostTime();
    }

    public long calculateInstantiationCostTime() {
        return afterInstantiationTime - beforeInstantiationTime;
    }

    public long calculateInitializationCostTime() {
        return afterInitializationTime - beforeInitializationTime;
    }

    public String toConsoleString() {
        return "\t" + getBeanName() + "\t" + calculateTotalCostTime() + "\t\n";
    }
}
