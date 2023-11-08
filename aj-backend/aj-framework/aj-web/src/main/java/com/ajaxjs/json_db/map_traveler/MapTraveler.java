package com.ajaxjs.json_db.map_traveler;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import lombok.Data;

/**
 * 遍历 ListMap 或 MapList
 */
@Data
public class MapTraveler {
	public void traveler(Map<String, Object> map) {
		traveler(map, null, 0);
	}

	@SuppressWarnings("unchecked")
	public void traveler(Map<String, Object> map, Map<String, Object> superMap, int level) {
		if (onMap != null && !onMap.execute(map, superMap, level))
			return;

		for (String key : map.keySet()) {
			Object value = map.get(key);

			if (onNode != null && !onNode.execute(key, value, map, superMap, level))
				return;

			if (value instanceof List || value instanceof Map) {
				if (onNewKey != null)
					onNewKey.accept(key);

				if (value instanceof Map)
					traveler((Map<String, Object>) value, map, level + 1);

				if (value instanceof List) {
					List<?> list = (List<?>) value;

					if (list.size() > 0 && (list.get(0) instanceof Map))
						traveler((List<Map<String, Object>>) value, map, level + 1);
				}

				if (onExitKey != null)
					onExitKey.accept(key);
			}
		}
	}

	/**
	 * 遍历一个 ListMap
	 *
	 * @param list 输入的 ListMa
	 */
	public void traveler(List<Map<String, Object>> list) {
		traveler(list, null, 0);
	}

	/**
	 * 遍历一个 ListMap
	 *
	 * @param list     输入的 ListMap
	 * @param superMap 父级 Map
	 * @param level    深度
	 */
	public void traveler(List<Map<String, Object>> list, Map<String, Object> superMap, int level) {
		for (Map<String, Object> map : list) {
			if (map == null)
				continue;

			traveler(map, superMap, level);
		}
	}

	/**
	 * 遇到新 Key 的回调
	 */
	private Consumer<String> onNewKey;

	/**
	 * 退出这个 key 的回调
	 */
	private Consumer<String> onExitKey;

	/**
	 * 
	 */
	private MapHandler onMap;

	/**
	 * 当遇到一个节点的时候的回调
	 */
	private MapEntryHandler onNode;

}
