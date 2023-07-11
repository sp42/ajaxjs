package com.ajaxjs.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class AddressUtils {
    /**
     * 解析地址
     * java正则表达式通过java.util.regex包下的Pattern类与Matcher类实现(建议在阅读本文时,打开java
     * API文档,当介绍到哪个方法时,查看java API中的方法说明,效果会更佳).
     * Pattern类用于创建一个正则表达式,也可以说创建一个匹配模式,它的构造方法是私有的,不可以直接创建,但可以通过Pattern.complie(String
     * regex)简单工厂方法创建一个正则表达式
     * <a href="https://blog.csdn.net/qq_40083897/article/details/83048587">...</a>
     *
     * @param address
     * @return
     */
    public static List<Map<String, String>> addressResolution(String address) {
        /*
         * java.util.regex是一个用正则表达式所订制的模式来对字符串进行匹配工作的类库包。它包括两个类：Pattern和Matcher Pattern
         *    一个Pattern是一个正则表达式经编译后的表现模式。 Matcher
         *    一个Matcher对象是一个状态机器，它依据Pattern对象做为匹配模式对字符串展开匹配检查。
         *    首先一个Pattern实例订制了一个所用语法与PERL的类似的正则表达式经编译后的模式，然后一个Matcher实例在这个给定的Pattern实例的模式控制下进行字符串的匹配工作。
         */
        String regex = "(?<province>[^省]+自治区|.*?省|.*?行政区|.*?市)(?<city>[^市]+自治州|.*?地区|.*?行政单位|.+盟|市辖区|.*?市|.*?县)(?<county>[^县]+县|.+区|.+市|.+旗|.+海域|.+岛)?(?<town>[^区]+区|.+镇)?(?<village>.*)";
        Matcher m = Pattern.compile(regex).matcher(address);
        String province, city, county, town, village;
        List<Map<String, String>> table = new ArrayList<>();
        Map<String, String> row;

        while (m.find()) {
            row = new LinkedHashMap<String, String>();
            province = m.group("province");
            row.put("province", province == null ? "" : province.trim());
            city = m.group("city");
            row.put("city", city == null ? "" : city.trim());
            county = m.group("county");
            row.put("county", county == null ? "" : county.trim());
            town = m.group("town");
            row.put("town", town == null ? "" : town.trim());
            village = m.group("village");
            row.put("village", village == null ? "" : village.trim());
            table.add(row);
        }

        return table;
    }

}
