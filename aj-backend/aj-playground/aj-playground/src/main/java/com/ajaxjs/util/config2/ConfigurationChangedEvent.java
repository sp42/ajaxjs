//package com.ajaxjs.util.config2;
//
//import org.springframework.context.ApplicationEvent;
//import org.springframework.core.env.PropertySource;
//
//import com.ajaxjs.util.logger.LogHelper;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//
//public class ConfigurationChangedEvent extends ApplicationEvent {
//	private static final LogHelper LOGGER = LogHelper.getLog(ConfigurationChangedEvent.class);
//
//	/**
//	 * Path of the file that changed and triggered this event
//	 */
//	private String path;
//
//	/**
//	 * previous property source of changed config file
//	 */
//	private PropertySource<?> previous;
//
//	/**
//	 * current property source of changed config file
//	 */
//	private PropertySource<?> current;
//
//	/**
//	 * The diff properties, keys are normalized, values are newest values, null
//	 * means the value deleted
//	 */
//	private Map<String, Object> diff;
//
//	ConfigurationChangedEvent(String path, PropertySource<?> previous, PropertySource<?> current, Map<String, Object> diff) {
//		super(path);
//		this.path = path;
//		this.previous = previous;
//		this.current = current;
//		this.diff = diff;
//	}
//
//	/**
//	 * loop current properties and prev properties, find diff removed properties
//	 * won't impact existing bean values
//	 */
//	static Map<String, Object> getPropertyDiff(Map<Object, OriginTrackedValue> prev, Map<Object, OriginTrackedValue> current) {
//		Map<String, Object> diff = new HashMap<>(4);
//		filterAddOrUpdatedKeys(prev, current, diff);
//		filterMissingKeys(prev, current, diff);
//		return diff;
//	}
//
//	private static void filterAddOrUpdatedKeys(Map<Object, OriginTrackedValue> prev, Map<Object, OriginTrackedValue> current, Map<String, Object> diff) {
//		for (Map.Entry<Object, OriginTrackedValue> entry : current.entrySet()) {
//			Object k = entry.getKey();
//			OriginTrackedValue v = entry.getValue();
//
//			if (prev.containsKey(k)) {
//				if (!Objects.equals(v, prev.get(k))) {
//					diff.put(k.toString(), v.getValue());
//					LOGGER.info("found changed key of dynamic config: {}", k);
//				}
//			} else {
//				diff.put(k.toString(), v.getValue());
//				LOGGER.info("found new added key of dynamic config: {}", k);
//			}
//		}
//	}
//
//	private static void filterMissingKeys(Map<Object, OriginTrackedValue> prev, Map<Object, OriginTrackedValue> current, Map<String, Object> diff) {
//		for (Map.Entry<Object, OriginTrackedValue> entry : prev.entrySet()) {
//			Object k = entry.getKey();
//			if (!current.containsKey(k)) {
//				diff.put(k.toString(), null);
//				LOGGER.info("found deleted k of dynamic config: {}", k);
//			}
//		}
//	}
//
//}