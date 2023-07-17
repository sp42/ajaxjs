/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache License.
 * See the accompanying LICENSE file for terms.
 */
package com.ajaxjs.mysql.model;

import java.util.ArrayList;

public class MetricsResultObject implements CustomResultObject {
    private static final long serialVersionUID = 1L;

    private java.util.List<Metric> metrics;

    @Override
    public String getName() {
        return "metrics";//fixed the name
    }

    @Override
    public String getValueJsonString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        if (this.metrics != null) {
            boolean first = true;
            for (Metric m : this.metrics) {
                if (!first) sb.append(",");
                else first = false;
                sb.append("\"").append(m.getName()).append("\"");
                sb.append(":{");
                sb.append("\"shortName\":\"").append(m.getShortName()).append("\"");
                sb.append(",\"inc\":").append(m.isIncremental() ? "1" : "0");
                sb.append(",\"unit\":\"").append(m.getMetricUnit()).append("\"");
                if (m.getAverageTimeUnit() != null)
                    sb.append(",\"avg\":\"").append(m.getAverageTimeUnit()).append("\"");
                if (m.getAdjustment() != null)
                    sb.append(",\"adj\":").append(m.getAdjustment());
                if (m.getChartDisplayUnit() != null)
                    sb.append(",\"display\":\"").append(m.getChartDisplayUnit()).append("\"");
                if (m.getDescription() != null)
                    sb.append(",\"description\":\"").append(m.getDescription()).append("\"");
                sb.append("}\r\n");
            }
        }
        sb.append("}");

        return sb.toString();
    }

    public java.util.List<Metric> getMetrics() {
        return metrics;
    }

    public void setMetrics(java.util.List<Metric> metrics) {
        this.metrics = metrics;
    }

    //will prefix metricsGroupname
    public void setMetrics(java.util.List<Metric> metrics, String metricsGroupName) {
        if (metrics != null && metricsGroupName != null && !metricsGroupName.isEmpty() && !"globalstatus".equals(metricsGroupName)) {
            for (Metric m : metrics)
                m.setName(metricsGroupName + "." + m.getName());
        }

        this.metrics = metrics;
    }

    public void addMetric(Metric m) {
        if (metrics == null) metrics = new ArrayList<>();
        metrics.add(m);
    }
}
