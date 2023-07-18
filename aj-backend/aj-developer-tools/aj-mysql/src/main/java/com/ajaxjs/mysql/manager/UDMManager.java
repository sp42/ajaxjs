/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache License.
 * See the accompanying LICENSE file for terms.
 */
package com.ajaxjs.mysql.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ajaxjs.mysql.config.MyPerfContext;
import com.ajaxjs.mysql.model.MetricsSubscribers;
import com.ajaxjs.mysql.model.UserDefinedMetrics;
import com.ajaxjs.mysql.model.alert.AlertDefinition;
import com.ajaxjs.mysql.model.alert.subscription.AlertSubscribers;
import com.ajaxjs.mysql.model.alert.subscription.Subscription;

import lombok.Data;

/**
 * Manage user defined metrics and db association
 *
 * @author xrao
 */
@Data
public class UDMManager {

	private static Logger logger = Logger.getLogger(UDMManager.class.getName());

	// UDM definition. key is the name
	private Map<String, UserDefinedMetrics> udms = java.util.Collections.synchronizedSortedMap(new java.util.TreeMap<>());
	private Object udmUpdateLock = new Object();
	private MetricsSubscribers metricsSubscriptions = new MetricsSubscribers();

	// User defined alerts
	private Map<String, AlertDefinition> alerts = java.util.Collections.synchronizedSortedMap(new java.util.TreeMap<>());
	private Object alertDefUpdateLock = new Object();
	private AlertSubscribers alertSubscriptions = new AlertSubscribers();

	// udm will be stored under $rootPath/udm
	// each udm is going to have its own josn file with suffix json.
	private static final String UDM_DIR = "udm";// directory to store udm def
	private static final String ALERT_DIR = "alert";// directory to store alert def
	private String rootPath = "myperf_config";
	private static final String UDM_DB_FILE = "udmdb.json";// udm db subscription file

	// mapping to builtin metrics
	// for each builtin MetricsGroup, possibly we take advantage of the same query
	private Map<String, List<String>> builtinMapping = java.util.Collections.synchronizedMap(new HashMap<>());

	public void init() {
		File udmroot = new File(new File(rootPath), UDM_DIR);
		if (!udmroot.exists())
			udmroot.mkdirs();
		File alertroot = new File(new File(rootPath), ALERT_DIR);
		if (!alertroot.exists())
			alertroot.mkdirs();
		this.loadUDM();
		this.loadAlertDef();
	}

	public void loadSubscriptions(MyPerfContext context) {
		this.alertSubscriptions.setContext(context);
		this.metricsSubscriptions.setContext(context);
		this.alertSubscriptions.load();
		this.metricsSubscriptions.load();
	}

	public UserDefinedMetrics getUDMByName(String name) {
		return udms.getOrDefault(name, null);
	}

	public AlertDefinition getAlertByName(String name) {
		return alerts.getOrDefault(name, null);
	}

	public List<String> getUDMsAttachedToBuiltinMetrics(String metricsGroup, String group, String host) {
		if (!this.builtinMapping.containsKey(metricsGroup))
			return null;

		List<String> subList = metricsSubscriptions.getSubscribedUDMs(group, host);
		if (subList == null || subList.size() == 0)
			return null;

		Set<String> mlist = new HashSet<>();
		for (String s : builtinMapping.get(metricsGroup))
			mlist.add(s);

		List<String> outList = new ArrayList<>(mlist.size());
		for (String s : subList) {
			if (mlist.contains(s))
				outList.add(s);
		}

		return outList;
	}

	/**
	 * Alerts attached to a metric for a give group/host
	 */
	public List<Subscription> getAlertsAttachedToBuiltinMetrics(String metricsGroup, String group, String host) {
		List<Subscription> attachedList = new ArrayList<>();
		List<Subscription> subs = this.getAlertSubscriptions().getSubscriptions(group, host);

		if (subs != null && subs.size() > 0) {
			for (Subscription sub : subs) {
				AlertDefinition def = this.getAlerts().get(sub.alertName);
				if (def != null && AlertDefinition.SOURCE_METRICS.equals(def.getSource()) && def.getMetricName() != null) {
					String m = def.getMetricName();
					int idx = m.lastIndexOf(".");
					if (idx > 0) {
						String grpName = m.substring(0, idx);
						if (metricsGroup.equals(grpName))
							attachedList.add(sub);
					}
				}
			}
		}

		return attachedList;
	}

	/**
	 * Return a list of alerts for the given host using global status
	 */
	public List<Subscription> getAlertsAttachedToGlobalStatus(String group, String host) {
		List<Subscription> attachedList = new ArrayList<>();
		List<Subscription> subs = getAlertSubscriptions().getSubscriptions(group, host);

		if (subs != null && subs.size() > 0) {
			for (Subscription sub : subs) {
				AlertDefinition def = getAlerts().get(sub.alertName);
				if (def != null && AlertDefinition.SOURCE_GLOBAL_STATUS.equals(def.getSource()) && def.getMetricName() != null) {
					attachedList.add(sub);
				}
			}
		}
		return attachedList;
	}

	public boolean updateUdmDbSubscription(String group, String host, String metricsGroup, String metricsSubGroup, boolean subscribe) {
		MetricsSubscribers.Subscription sub = new MetricsSubscribers.Subscription();
		sub.group = group;
		sub.host = host;
		sub.mGroup = metricsGroup;
		sub.mSubGroup = metricsSubGroup;

		boolean status = false;
		if (subscribe)
			status = this.getMetricsSubscriptions().addSubscription(sub);
		else
			status = this.getMetricsSubscriptions().deleteSubscription(sub);
		return status;
	}

	public boolean updateAlertDbSubscription(String group, String host, String alertName, boolean subscribe) {
		Subscription sub = new Subscription();
		sub.group = group;
		sub.host = host;
		sub.alertName = alertName;

		boolean status = false;
		if (subscribe)
			status = this.getAlertSubscriptions().addSubscription(sub);
		else
			status = this.getAlertSubscriptions().deleteSubscription(sub);
		return status;
	}

	public boolean isMetricsGroupSubscribed(String group, String host, String mGroup, String mSubGroup) {
		MetricsSubscribers.Subscription subscript = this.metricsSubscriptions.getSubscription(group, host, mGroup, mSubGroup);
		return subscript != null;
	}

	private void loadUDM() {
		File root = new File(new File(rootPath), UDM_DIR);
		if (!root.exists()) {
			logger.info("There is no user defined metrics at " + root.getAbsolutePath());
			return;
		}

		File[] udmFiles = root.listFiles(new java.io.FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name != null && name.endsWith(".json") && !name.equals(UDM_DB_FILE))
					return true;
				else
					return false;
			}
		});

		if (udmFiles == null || udmFiles.length == 0) {
			logger.info("There is no UDM degfned.");
			return;
		}
		for (File udmF : udmFiles) {
			// now load one by one
			InputStream in = null;
			try {
				in = new FileInputStream(udmF);
				UserDefinedMetrics udm = UserDefinedMetrics.createFromJson(in);
				if (udm == null)
					logger.warning("Failed to parse udm filr " + udmF.getName());
				else {
					this.udms.put(udm.getName(), udm);
					String src = udm.getSource();
					if (!"SQL".equalsIgnoreCase(src)) {
						if (!this.builtinMapping.containsKey(src))
							this.builtinMapping.put(src, new ArrayList<>());
						this.builtinMapping.get(src).add(udm.getName());
					}
				}
			} catch (Exception ex) {
				logger.log(Level.WARNING, "Error when parsing udm " + udmF.getName(), ex);
			} finally {
				if (in != null)
					try {
						in.close();
					} catch (Exception ex) {
					}
			}
		}
	}

	private void loadAlertDef() {
		File root = new File(new File(rootPath), ALERT_DIR);
		if (!root.exists()) {
			logger.info("There is no user defined alerts at " + root.getAbsolutePath());
			return;
		}

		File[] alertFiles = root.listFiles((dir, name) -> name != null && name.endsWith(".json"));

		if (alertFiles == null || alertFiles.length == 0) {
			logger.info("There is no ALERT defined.");
			return;
		}
		for (File alertF : alertFiles) {
			// now load one by one
			InputStream in = null;
			try {
				in = Files.newInputStream(alertF.toPath());
				AlertDefinition alertDef = AlertDefinition.createFromJson(in);
				if (alertDef == null)
					logger.warning("Failed to parse alert file " + alertF.getName());
				else
					this.alerts.put(alertDef.getName(), alertDef);
			} catch (Exception ex) {
				logger.log(Level.WARNING, "Error when parsing aert def " + alertF.getName(), ex);
			} finally {
				if (in != null)
					try {
						in.close();
					} catch (Exception ex) {
					}
			}
		}
	}

	private void storeUDM(UserDefinedMetrics udm) {
		String fileName = udm.getName() + ".json";
		File root = new File(new File(rootPath), UDM_DIR);
		File udmFile = new File(root, fileName);

		if (udmFile.exists())
			throw new RuntimeException("UDM file " + fileName + " already exists.");

		FileWriter fw = null;
		try {
			fw = new FileWriter(udmFile);
			fw.write(udm.toJSON(false));
			fw.flush();
			fw.close();
		} catch (Exception ex) {
			throw new RuntimeException("Exception when store UDM file " + fileName + ": " + ex.getMessage());
		} finally {
			if (fw != null)
				try {
					fw.close();
				} catch (Exception iex) {
				}
		}
	}

	private void storeAlert(AlertDefinition alertDef) {
		String fileName = alertDef.getName() + ".json";
		File root = new File(new File(rootPath), ALERT_DIR);
		File udmFile = new File(root, fileName);
		if (udmFile.exists()) {
			logger.info("Overwrite alert def file: " + udmFile.getPath());
			// throw new UserDefinedMetrics.UserDefinedMetricsException("ALERT file
			// "+fileName +" already exists.");
		}

		FileWriter fw = null;
		try {
			fw = new FileWriter(udmFile);
			fw.write(alertDef.toJSON(false));
			fw.flush();
			fw.close();
		} catch (Exception ex) {
			throw new RuntimeException("Exception when store ALERT file " + fileName + ": " + ex.getMessage());
		} finally {
			if (fw != null)
				try {
					fw.close();
				} catch (Exception iex) {
				}
		}
	}

	/**
	 * Add a new UDM. UDM with a name already used is not allowed for now, before we
	 * will use generic format to store data.
	 */
	public void addUDM(UserDefinedMetrics udm) {
		if (udm == null)
			return;
		synchronized (udmUpdateLock) {
			// while we are updating, don't want other to do the same. Avoid duplicates
			if (this.udms.containsKey(udm.getName()))
				throw new RuntimeException("UDM " + udm.getName() + " already exists.");
			// we assume validation and test already done before invoke this method
			// store it first
			this.storeUDM(udm);
			this.udms.put(udm.getName(), udm);
			String src = udm.getSource();
			if (!"SQL".equalsIgnoreCase(src)) {
				if (!this.builtinMapping.containsKey(src))
					this.builtinMapping.put(src, new ArrayList<>());
				this.builtinMapping.get(src).add(udm.getName());
			}
		}
	}

	public void addAlertDefinition(AlertDefinition alertDef) {
		if (alertDef == null)
			return;

		synchronized (alertDefUpdateLock) {
			// while we are updating, don't want other to do the same. Avoid duplicates
			// if(this.alerts.containsKey(alertDef.getName()))
			// throw new UserDefinedMetrics.UserDefinedMetricsException("Alert
			// "+alertDef.getName()+" already exists.");
			// we assume validation and test already done before invoke this method
			// store it first
			// if existing, overwrite
			storeAlert(alertDef);
			alerts.put(alertDef.getName(), alertDef);
		}
	}
}
