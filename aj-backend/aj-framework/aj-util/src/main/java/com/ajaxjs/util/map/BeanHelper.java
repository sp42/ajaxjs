package com.ajaxjs.util.map;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Java Bean 工具类
 *
 * @author Frank Cheung sp42@qq.com
 */
public class BeanHelper {
    /**
     * 打印 bean，方便调试
     *
     * @param bean Bean
     */
    public static void print(Object bean) {
        Class<?> c = bean.getClass();
        StringBuilder sb = new StringBuilder(c.getName() + ".toString：\n");
        Field[] fields = c.getDeclaredFields();
        Field.setAccessible(fields, true);

        try {
            for (Field field : fields) {
                if (null != field.get(bean)) {
//					String fieldType = String.valueOf(field.getType());
                    String tmp = field.getName() + "=" + field.get(bean) + "\n";
                    sb.append(tmp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(sb);
    }

    public static <T> T jsonStr2Bean(String jsonStr, Class<T> beanClz) {
        Map<String, Object> map = JsonHelper.parseMapClean(jsonStr);

        return MapTool.map2Bean(map, beanClz, true);
    }
}
