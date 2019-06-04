package com.ajaxjs.javatools.net.pagecache.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 对参数进行处理
 * 
 * @author
 *
 */
public class UrlKeyCreator {
	public static String getUrlKey(String url, String urlPattern, Map<String, String[]> params) {
		StringBuilder sb = new StringBuilder(url);
		List<String> includeParams = PageCacheGlobalConfig.getUrlIncludeParams().get(urlPattern), excludeParams = PageCacheGlobalConfig.getUrlExcludeParams().get(urlPattern);

		String paramsKey = "";
		if (includeParams != null) { // 先合并参数，然后剔除参数
			paramsKey = getIncludeParamsKey(params, includeParams);
		} else if (excludeParams != null) {
			paramsKey = getExcludeParamsKey(params, excludeParams);
		}

		sb.append(paramsKey); // url + params
		return sb.toString();
	}

	/**
	 * 并集，大家都有的 item，并 generalParams()
	 * 
	 * @param params
	 * @param includeParams
	 * @return
	 */
	private static String getIncludeParamsKey(Map<String, String[]> params, List<String> includeParams) {
		Map<String, String[]> valueMap = new HashMap<>();

		for (String includeParam : includeParams) {
			if (params.containsKey(includeParam))
				valueMap.put(includeParam, params.get(includeParam));
		}

		return generalParams(valueMap);
	}

	/**
	 * 从 params 中排除那些在 excludeParams 的参数，并 generalParams()
	 * 
	 * @param params
	 * @param excludeParams
	 * @return
	 */
	private static String getExcludeParamsKey(Map<String, String[]> params, List<String> excludeParams) {
		Map<String, String[]> valueMap = new HashMap<>(params);

		for (String excludeParam : excludeParams) {
			if (params.containsKey(excludeParam))
				valueMap.remove(excludeParam);
		}

		return generalParams(valueMap);
	}

	/**
	 * 常规化？？？
	 * 
	 * @param valueMap
	 * @return
	 */
	private static String generalParams(Map<String, String[]> valueMap) {
		List<Map.Entry<String, String[]>> mappingList = new ArrayList<>(valueMap.entrySet());
		Collections.sort(mappingList, new Comparator<Map.Entry<String, String[]>>() {
			public int compare(Map.Entry<String, String[]> mapping1, Map.Entry<String, String[]> mapping2) {
				return mapping1.getKey().compareTo(mapping2.getKey());
			}
		});

		StringBuilder sb = new StringBuilder();

		for (Map.Entry<String, String[]> entry : mappingList) {
			sb.append(entry.getKey() + "&");
			List<String> valueList = Arrays.asList(entry.getValue());
			Collections.sort(valueList);

			for (String value : valueList)
				sb.append(value + ";");
		}

		return sb.toString();
	}

	/**
	 * 返回符合正则的 url UrlKeyMatcher
	 * 
	 * @param url
	 * @return
	 */
	public static String getMatchUrlPattern(String url) {
		for (String urlPattern : PageCacheGlobalConfig.getUrlPattern()) {
			if (Pattern.matches(urlPattern, url)) // 正则匹配
				return urlPattern;
		}
		return null;
	}

}
