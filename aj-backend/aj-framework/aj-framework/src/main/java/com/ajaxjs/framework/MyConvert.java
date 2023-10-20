package com.ajaxjs.framework;

import com.ajaxjs.util.convert.ConvertBasicValue;
import com.ajaxjs.util.convert.ConvertComplexValue;
import com.ajaxjs.util.convert.EntityConvert;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class MyConvert extends ConvertComplexValue {
    private volatile static ConvertBasicValue convertValue;

    public static ConvertBasicValue getConvertValue() {
        if (convertValue == null) {
            synchronized (ConvertBasicValue.class) {
                if (convertValue == null) {
                    convertValue = new ConvertBasicValue();
//                    convertValue.setParseMapAsBean((map, clz) -> Convert.map2Bean(map, clz, true));
//                    convertValue.setParseJsonMapAsBean(JsonHelper::parseMapAsBean);
//                    convertValue.setParseJsonAsMap(JsonHelper::parseMap);
//                    convertValue.setParseList(JsonHelper::parseList);
//                    convertValue.setBeanClz(IBaseModel.class);
                }
            }
        }

        return convertValue;
    }

    public static void set() {
        ConvertComplexValue c = new MyConvert();
        c.setBeanClz(IBaseModel.class);

        ConvertComplexValue.INSTANCE = c;
    }

    @Override
    protected Map<String, Object> parseJsonAsMap(String value) {
        return EntityConvert.json2map(value);
    }

    @Override
    protected <T> T parseJsonMapAsBean(String value, Class<T> clz) {
        return EntityConvert.json2bean(value, clz);
    }

    @Override
    protected List<Map<String, Object>> parseList(String value) {
        return EntityConvert.json2MapList(value);
    }
}
