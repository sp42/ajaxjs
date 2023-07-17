package com.ajaxjs.mysql.model.alert.subscription;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class HostSubscriptions {
	private List<Subscription> subscriptions = new ArrayList<>();// host level subscription
	private Map<String, Subscription> subscriptionsMap = new HashMap<>(); // internal index

	public HostSubscriptions() {

	}

	public void addSubscription(Subscription sub) {
		subscriptions.add(sub);
		subscriptionsMap.put(sub.alertName, sub);
	}

	public void deleteSubscription(Subscription sub) {
		if (sub == null)
			return;

		for (int i = subscriptions.size() - 1; i >= 0; i--) {
			Subscription sub2 = subscriptions.get(i);

			if (sub2.alertName.equals(sub.alertName)) {
				subscriptions.remove(i);
				subscriptionsMap.remove(sub.alertName);
				break;
			}
		}
	}

	public Subscription getSubscription(String alertName) {
		return subscriptionsMap.get(alertName);
	}

}
