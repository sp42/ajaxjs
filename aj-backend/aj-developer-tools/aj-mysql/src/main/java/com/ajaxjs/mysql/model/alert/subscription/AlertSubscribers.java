/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache License.
 * See the accompanying LICENSE file for terms.
 */
package com.ajaxjs.mysql.model.alert.subscription;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ajaxjs.mysql.config.MyPerfContext;

import lombok.Data;

/**
 * Records of (group, host, alert)
 *
 * @author xrao
 */
@Data
public class AlertSubscribers {
	private Map<String, GroupSubscriptions> groupSubscriptions = new HashMap<>();// all subscriptions, grouped by server group
	private MyPerfContext context; // framework context to access metrics db

	/**
	 * Load subscriptions from DB. Return false if failed.
	 *
	 * @return
	 */
	public boolean load() {
		if (this.context.getMetricDb() != null) {
			try {
				List<Subscription> subs = this.context.getMetricDb().loadAlertSubscriptions();
				synchronized (this.groupSubscriptions) {
					this.groupSubscriptions.clear();// remove old data
					for (Subscription sub : subs) {
						String group = sub.group;
						if (!this.groupSubscriptions.containsKey(group)) {
							this.groupSubscriptions.put(group, new GroupSubscriptions());
						}
						this.groupSubscriptions.get(group).addSubscription(sub);
					}
				}

				return true;
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("Failed to load alert subscriptions");
				return false;
			}
		}
		return false;
	}

	public boolean addSubscription(Subscription sub) {
		if (sub == null || sub.group == null || sub.group.isEmpty() || sub.alertName == null || sub.alertName.isEmpty())
			return false;

		boolean status = this.context.getMetricDb() != null ? this.context.getMetricDb().upsertAlertSubscription(sub) : false;
		if (!status)
			return false;

		synchronized (this.groupSubscriptions) {
			String group = sub.group;
			if (!this.groupSubscriptions.containsKey(group))
				this.groupSubscriptions.put(group, new GroupSubscriptions());

			this.groupSubscriptions.get(group).addSubscription(sub);
		}

		return true;
	}

	public boolean deleteSubscription(Subscription sub) {
		boolean status = context.getMetricDb() != null ? context.getMetricDb().deleteAlertSubscription(sub) : false;
		if (!status)
			return false;

		synchronized (groupSubscriptions) {
			String group = sub.group;
			if (this.groupSubscriptions.containsKey(group))
				this.groupSubscriptions.get(group).deleteSubscription(sub);
		}

		return true;
	}

	/**
	 * Check if a server has subscribed an alert
	 */
	public Subscription getSubscription(String group, String host, String alertName) {
		synchronized (groupSubscriptions) {
			if (!groupSubscriptions.containsKey(group))
				return null;
			return groupSubscriptions.get(group).getSubscription(host, alertName);
		}
	}

	public List<Subscription> getSubscriptions(String group, String host) {
		synchronized (groupSubscriptions) {
			if (!groupSubscriptions.containsKey(group))
				return null;
			return groupSubscriptions.get(group).getSubscription(host);
		}
	}
}
