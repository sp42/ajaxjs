package com.ajaxjs.framework;

import com.ajaxjs.util.convert.Convert;
import com.ajaxjs.util.convert.ConvertValue;
import com.ajaxjs.util.convert.JsonHelper;
import com.ajaxjs.util.convert.MapTool;

/**
 *
 */
public class MyConvert {
    private volatile static ConvertValue convertValue;

    public static ConvertValue getConvertValue() {
        if (convertValue == null) {
            synchronized (ConvertValue.class) {
                if (convertValue == null) {
                    convertValue = new ConvertValue();
                    convertValue.setParseMapAsBean((map, clz) -> Convert.map2Bean(map, clz, true));
                    convertValue.setParseJsonMapAsBean(JsonHelper::parseMapAsBean);
                    convertValue.setParseJsonAsMap(JsonHelper::parseMap);
                    convertValue.setParseList(JsonHelper::parseList);
                    convertValue.setBeanClz(IBaseModel.class);
                }
            }
        }

        return convertValue;
    }
}
