package com.ajaxjs.utils.map;

import com.ajaxjs.util.ObjectHelper;
import org.springframework.util.StringUtils;

import java.util.*;

public class JsonUtils {
    /**
     * 这是一个更加的 json 解析方法 就算是{'a:a{dsa}':"{fdasf[dd]}"} 这样的也可以处理
     * 当然{a:{b:{c:{d:{e:[1,2,3,4,5,6]}}}}}更可以处理
     *
     * @param jsonStr 合法格式的 json 字符串
     * @return 有可能 map 有可能是 list
     */
    public static Object json2Map(String jsonStr) {
        if (!StringUtils.hasText(jsonStr))
            return null;

        Stack<Map<String, Object>> maps = new Stack<>(); // 用来保存所有父级对象
        Stack<List<Object>> lists = new Stack<>(); // 用来表示多层的 list 对象
        Stack<Boolean> isList = new Stack<>();// 判断是不是 list
        Stack<String> keys = new Stack<>(); // 用来表示多层的 key

        boolean hasQuotation = false; // 是否有引号
        String keytmp;
        Object valuetmp = null;
        StringBuilder sb = new StringBuilder();

        char[] cs = jsonStr.toCharArray();

        for (char c : cs) {
            if (c == ' ') // 忽略空格
                continue;

            if (hasQuotation) {
                if (hasQuotation(c))
                    sb.append(c);
                else
                    hasQuotation = false;

                continue;
            }

            switch (c) {
                case '{': // 如果是 { map 进栈

                    maps.push(new HashMap<>());
                    isList.push(false);
                    continue;
                case '\'':
                case '\"':
                    hasQuotation = true;
                    continue;
                case ':':// 如果是：表示这是一个属性建，key 进栈

                    keys.push(sb.toString());
                    sb = new StringBuilder();
                    continue;
                case '[':

                    lists.push(new ArrayList<>());
                    isList.push(true);
                    continue;
                case ',':
                    /*
                     * 这是一个分割，因为可能是简单地 string 的键值对，也有可能是 string=map 的键值对，因此 valuetmp 使用 object 类型；
                     * 如果 valuetmp 是 null 应该是第一次，如果 value 不是空有可能是 string，那是上一个键值对，需要重新赋值 还有可能是 map
                     * 对象，如果是 map 对象就不需要了
                     */
                    if (sb.length() > 0)
                        valuetmp = sb.toString();

                    sb = new StringBuilder();
                    boolean listis = isList.peek();

                    if (!listis) {
                        keytmp = keys.pop();

                        if (valuetmp instanceof String)
                            maps.peek().put(keytmp, ObjectHelper.toJavaValue(valuetmp.toString())); // 保存 Map 的 Value
                        else
                            maps.peek().put(keytmp, valuetmp);
                    } else
                        lists.peek().add(valuetmp);

                    continue;
                case ']':

                    isList.pop();

                    if (sb.length() > 0)
                        valuetmp = sb.toString();

                    sb = new StringBuilder();
                    lists.peek().add(valuetmp);
                    valuetmp = lists.pop();

                    continue;
                case '}':

                    isList.pop();
                    // 这里做的和，做的差不多，只是需要把 valuetmp=maps.pop(); 把 map 弹出栈
                    keytmp = keys.pop();

                    if (sb.length() > 0)
                        valuetmp = sb.toString();

                    sb = new StringBuilder();
                    maps.peek().put(keytmp, valuetmp);
                    valuetmp = maps.pop();

                    continue;
                default:
                    sb.append(c);
            }

        }

        return valuetmp;
    }

    /**
     * 是否引号字符串，JSON 支持 " 和 ' 两种的字符串字面量
     *
     * @param c 传入的字符
     * @return 是否引号字符串
     */
    private static boolean hasQuotation(char c) {
        return c != '\"' && c != '\'';
    }
}
