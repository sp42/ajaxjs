package com.ajaxjs.framework.spring.util;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
class BeanPostProcessStatistics {

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
        StringBuilder consoleString = new StringBuilder();
        long totalCostTime = calculateTotalCostTime();

        consoleString
                .append("\t")
                .append(getBeanName())
                .append("\t")
                .append(totalCostTime)
                .append("\t\n");

        return consoleString.toString();

    }

}
