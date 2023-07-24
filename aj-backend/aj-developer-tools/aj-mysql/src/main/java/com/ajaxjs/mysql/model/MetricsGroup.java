/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache License.
 * See the accompanying LICENSE file for terms.
 */
package com.ajaxjs.mysql.model;

import lombok.Data;

import java.util.*;

/**
 * MetricsGroup groups related metrics, for example, metrics can be gathered by
 * a single SQL call. It can contains zero or more sub groups for storage
 * purpose. Each sub group, if exists, will be stored inside a table with the
 * name defined in the sub group definition.
 *
 * @author xrao
 */
@Data
public class MetricsGroup {
	private String groupName;// the name will be also used for storage table name.
	private boolean auto = true;// if false, we need manually enable this metric group for a specific db server

	// A top level MetricsGroup can either contains a list of metrics, or a list of
	// sub metrics group,
	// but not both. If sub groups are presented, the data will be stored per sub
	// group and the storage
	// table names will be the groupname of the sub groups.
	private List<Metric> metrics;
	private Map<String, Metric> metricsNameMap;

	private List<MetricsGroup> subGroups = new ArrayList<>();

	private boolean multipleMetricsPerRow = true;// if db returns one row with all required metrics
	private String keyColumn; // some metrics can be per user based (user stats), per disk based, or per
								// network interface based, etc.
	// this will be used to store the key to identify the user, or disk, or network
	// interface
	// For this case, no subgroup is allowed.
	private String metricNameColumn;// column name for the metrics name
	private String metricValueColumn;// column name for the metrics value
	private String dbType = "MYSQL";
	// The sql used to retrieve the data, either a sql handler id for predefined
	// sql,
	// or full sqlText, but not both, should be specified.
	private String sql;
	private String sqlText;
	private boolean storeInCommonTable = false;// store the metrics in a common shared table
	// better no to be used for subgroup

	private String targetTable;// to save into target metric table, with columns like (dbid, metric_id, ts,
								// value)

	private MetricsGroup parentGroup;// Since we allow subGroups, each subGroup should have a way to access its
										// parent group
	private boolean udmFlagged;// flag this is user defined metrics group, not builtin

	public MetricsGroup() {
		this.metrics = new ArrayList<>();
		this.metricsNameMap = new HashMap<>();
	}

	public MetricsGroup(String groupName) {
		super();
		this.groupName = groupName;
	}

	/**
	 * Note the returned list is a unmodified list
	 */
	public List<Metric> getMetrics() {
		return Collections.unmodifiableList(metrics);
	}

	public void addMetrics(Metric metric) {
		metrics.add(metric);
		metricsNameMap.put(metric.getName(), metric);
	}

	public MetricsGroup getSubGroupByName(String name) {
		for (MetricsGroup grp : this.subGroups) {
			if (grp.getGroupName().equals(name))
				return grp;
		}

		return null;
	}

	private int length;

	public void calculateLength() {
		int l = 0;
		for (Metric m : this.metrics)
			l += m.getDataType().getLength();

		this.length = l;
	}

	/**
	 * If the content ever changed, need re-calculate it first
	 */
	public int getLength() {
		return this.length;
	}

	public boolean isStoreInCommonTable() {
		return storeInCommonTable;
	}

	public void addSubGroups(MetricsGroup subGroup) {
		if (subGroup == null)
			return;

		subGroup.setParentGroup(this);
		subGroups.add(subGroup);
	}

	public boolean isAuto() {
		return auto;
	}

	public boolean isMultipleMetricsPerRow() {
		return multipleMetricsPerRow;
	}

	public boolean isUdmFlagged() {
		return udmFlagged;
	}

	public Metric getMetricByName(String metricName) {
		return this.metricsNameMap.get(metricName);
	}

	/**
	 * Table name of the metrics storage
	 */
	public String getSinkTableName() {
		if (this.isStoreInCommonTable()) {
			if (this.targetTable != null)
				return this.targetTable;
			else
				return "METRIC_GENERIC";
		} else if (this.udmFlagged)
			return "UDM_" + this.groupName.toUpperCase();

		return (this.dbType + "_" + this.groupName).toUpperCase();
	}

	public String getMetricFullName(String metricName) {
		if (!this.metricsNameMap.containsKey(metricName))
			return null;

		String prefix;
		if (this.udmFlagged)
			prefix = "UDM." + this.groupName;// udm
		else if (this.parentGroup == null)
			prefix = this.groupName + ".";// top level
		else
			prefix = this.parentGroup.getGroupName() + "." + this.groupName;

		return prefix + "." + metricName;
	}
}
