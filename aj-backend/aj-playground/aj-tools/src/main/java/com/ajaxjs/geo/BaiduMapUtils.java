package com.ajaxjs.geo;

import com.ajaxjs.net.http.Get;
import com.ajaxjs.util.StrUtil;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 根据经纬度解析出地址，反之根据地址解析出经纬度
 * <a href="https://blog.csdn.net/qq_40083897/article/details/85233997?spm=1001.2014.3001.5502">...</a>
 * <a href="https://lbsyun.baidu.com/index.php?title=webapi/guide/webservice-geocoding-abroad">...</a>
 */
public class BaiduMapUtils {
    private static final String URL = "http://api.map.baidu.com/geocoder/v2/";
    /**
     * 用户申请注册的key，自v2开始参数修改为“ak”，之前版本参数为“key”
     **/
    private static final String AK = "***************";
    /**
     * 若用户所用ak的校验方式为sn校验时该参数必须sn生成
     **/
    private static final String SK = "***************";

    /**
     * 百度地图通过经纬度来获取地址,传入参数纬度lat、经度lng
     *
     * @param lat
     * @param lng
     * @return
     */
    public static Map<String, Object> getCity(String lat, String lng) {
        Map<String, String> paramsMap = new LinkedHashMap<String, String>();
        paramsMap.put("location", lat + "," + lng);
        paramsMap.put("output", "json");
        paramsMap.put("pois", "1");
        paramsMap.put("ak", AK);
        String sn = getSN(paramsMap);
        paramsMap.put("sn", sn);
        String params = toQueryString(paramsMap);

        return Get.api(URL + params);
    }

    /**
     * 百度地图通过地址来获取经纬度，传入参数address
     *
     * @param address
     * @return
     */
    public static Map<String, Object> getLngAndLat(String address) {
        Map<String, String> paramsMap = new LinkedHashMap<>();
        paramsMap.put("address", address);
        paramsMap.put("output", "json");
        paramsMap.put("ak", AK);
        String sn = getSN(paramsMap);
        paramsMap.put("sn", sn);
        String params = toQueryString(paramsMap);

        return Get.api(URL + params);
    }

    //sn就是要计算的，sk不需要在url里出现，但是在计算sn的时候需要sk（假设sk=yoursk）
    private static String getSN(Map<String, String> paramsMap) {
        // 调用下面的toQueryString方法，对LinkedHashMap内所有value作utf8编码，拼接返回结果address=%E7%99%BE%E5%BA%A6%E5%A4%A7%E5%8E%A6&output=json&ak=yourak
        String wholeStr = "/geocoder/v2/?" + toQueryString(paramsMap) + SK;

        // 对paramsStr前面拼接上/geocoder/v2/?，后面直接拼接yoursk得到/geocoder/v2/?address=%E7%99%BE%E5%BA%A6%E5%A4%A7%E5%8E%A6&output=json&ak=yourakSK
        return DigestUtils.md5DigestAsHex(Objects.requireNonNull(StrUtil.urlEncode(wholeStr)).getBytes());
    }

    // 对Map内所有value作utf8编码，拼接返回结果
    private static String toQueryString(Map<?, ?> data) {
        StringBuilder queryString = new StringBuilder();

        try {
            for (Map.Entry<?, ?> pair : data.entrySet()) {
                queryString.append(pair.getKey()).append("=");
                queryString.append(URLEncoder.encode((String) pair.getValue(), "UTF-8")).append("&");
            }

            if (queryString.length() > 0)
                queryString.deleteCharAt(queryString.length() - 1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return queryString.toString();
    }
}
