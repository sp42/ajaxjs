package com.ajaxjs.framework.shop.alipay;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ajaxjs.framework.shop.alipay.core.OneLevelOnlyXML;
import com.ajaxjs.framework.shop.alipay.model.StringPair;

/**
 * 请求数据
 * 
 * @author Frank Cheung
 *
 */
public class AlipayRequestData {
	private Map<String, String> data = new HashMap<>();
	
	private Map<String, String> reqData = new HashMap<>();

	public void setString(String key, String value) {
		data.put(key, value);
	}

	public void setReqString(String key, String value) {
		reqData.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public List<StringPair> getSortedParameters() {
		List<StringPair> ret = new LinkedList<>();
		String reqDataRootElement = null;

		for (Entry<String, String> param : data.entrySet()) {
			if ("req_data".equals(param.getKey())) {
				reqDataRootElement = param.getValue();
				continue;
			}

			ret.add(new StringPair(param.getKey(), param.getValue()));
		}

		if (reqDataRootElement != null) {
			OneLevelOnlyXML reqXML = new OneLevelOnlyXML();
			reqXML.createRootElement(reqDataRootElement);

			for (Entry<String, String> req : reqData.entrySet())
				reqXML.createChild(req.getKey(), req.getValue());

			String reqData = reqXML.toXMLString();
			ret.add(new StringPair("req_data", reqData));
		}

		Collections.sort(ret);

		return ret;
	}
}
