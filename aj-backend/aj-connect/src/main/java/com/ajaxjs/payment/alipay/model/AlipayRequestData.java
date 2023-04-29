package com.ajaxjs.payment.alipay.model;


import com.ajaxjs.payment.alipay.OneLevelOnlyXML;

import java.util.*;
import java.util.Map.Entry;

/**
 * 请求数据
 *
 * @author Frank Cheung
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
