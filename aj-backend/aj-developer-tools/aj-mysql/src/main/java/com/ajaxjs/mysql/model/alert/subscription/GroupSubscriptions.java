package com.ajaxjs.mysql.model.alert.subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class GroupSubscriptions {
	private List<Subscription> subscriptions = new ArrayList<>();// group level subscription
	private Map<String, HostSubscriptions> hostsSubscriptions = new HashMap<>();// key is the hostname
	private Map<String, Subscription> subscriptionsMap = new HashMap<>(); // internal index

	public void addSubscription(Subscription sub) {
		if (sub == null)
			return;
		if (sub.host == null || sub.host.isEmpty())
			this.subscriptions.add(sub);
		else {
			if (!this.hostsSubscriptions.containsKey(sub.host))
				this.hostsSubscriptions.put(sub.host, new HostSubscriptions());
			this.hostsSubscriptions.get(sub.host).addSubscription(sub);
			this.subscriptionsMap.put(sub.alertName, sub);
		}
	}

	public void deleteSubscription(Subscription sub) {
		if (sub == null)
			return;
		if (sub.host == null || sub.host.isEmpty()) {
			for (int i = this.subscriptions.size() - 1; i >= 0; i--) {
				Subscription sub2 = this.subscriptions.get(i);

				if (sub2.alertName.equals(sub.alertName)) {
					this.subscriptions.remove(i);
					this.subscriptionsMap.remove(sub.alertName);
					break;
				}
			}
		} else if (this.hostsSubscriptions.containsKey(sub.host)) {
			this.hostsSubscriptions.get(sub.host).deleteSubscription(sub);
		}
	}

	public Subscription getSubscription(String host, String alertName) {
		Subscription sub = null;
		if (this.hostsSubscriptions.containsKey(host))
			sub = hostsSubscriptions.get(host).getSubscription(alertName);
		if (sub == null)
			sub = this.subscriptionsMap.get(alertName);

		return sub;
	}

	public List<Subscription> getSubscription(String host) {
		HashSet<String> alertnames = new HashSet<>();
		List<Subscription> mysubs = new ArrayList<>();

		if (this.hostsSubscriptions.containsKey(host)) {
			for (Subscription sub : this.hostsSubscriptions.get(host).getSubscriptions()) {
				mysubs.add(sub);
				alertnames.add(sub.alertName);
			}
		}

		// group level
		for (Subscription sub : subscriptions) {
			if (!alertnames.contains(sub.alertName)) {
				mysubs.add(sub);
				alertnames.add(sub.alertName);
			}
		}

		return mysubs;
	}
}
