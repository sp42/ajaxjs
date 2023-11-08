package com.ajaxjs.iam.user.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserUtils {
    /**
     * 测试 8421 码是否包含 v
     *
     * @param v   当前权限值
     * @param all 同值
     * @return true=已包含
     */
    public static boolean testBCD(int v, int all) {
        return (v & all) == v;
    }

    /**
     * 验证 email 是否合法正确
     */
    private final static String EMAIL_REGEXP = "^([a-z0-9A-Z]+[-|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    public final static Pattern EMAIL_REG = Pattern.compile(EMAIL_REGEXP);

    /**
     * 是否合法的邮件
     *
     * @param email 待测试的邮件地址
     * @return true 表示为合法邮件
     */
    public static boolean isValidEmail(String email) {
        return email.matches(EMAIL_REGEXP);
    }

    /**
     * 验证手机号码是否合法正确
     */
    private static final String PHONE_REGEXP = "^1[3-8]\\d{9}$";

    public final static Pattern PHONE_REG = Pattern.compile(PHONE_REGEXP);

    /**
     * 是否合法的手机号码，仅限中国大陆号码
     *
     * @param phoneNo 待测试的手机号码
     * @return true 表示为手机号码
     */
    public static boolean isValidPhone(String phoneNo) {
        return phoneNo.matches(PHONE_REGEXP);
    }

    /**
     * 解析地址
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
            row = new LinkedHashMap<>();
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

    public static void main(String[] args) {
        List<Map<String, String>> table = addressResolution("浙江省杭州市拱墅区湖墅南路湖墅新村4幢");
        System.out.println(table);
        System.out.println(table.get(0).get("province"));
        System.out.println(table.get(0).get("city"));
        System.out.println(table.get(0).get("county"));
        System.out.println(table.get(0).get("town"));
        System.out.println(table.get(0).get("village"));
    }
}
