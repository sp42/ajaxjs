package com.ajaxjs.framework.spring.filter;

import com.ajaxjs.util.convert.ConvertBasicValue;
import com.ajaxjs.util.convert.MapTool;
import com.ajaxjs.util.io.StreamHelper;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class GetPutData extends HttpServletRequestWrapper {
    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public GetPutData(HttpServletRequest request) {
        super(request);
    }

    private Map<String, String[]> putRequestData;

    private final static String PUT = "PUT";

    @Override
    public Map<String, String[]> getParameterMap() {
        if (PUT.equals(getMethod())) {
            if (putRequestData == null) {
                putRequestData = getPutRequestData2();
//                Map<String, Object> map = getPutRequestData();
//
//                if (CollectionUtils.isEmpty(map))
//                    putRequestData = null;
//                else {
//                    putRequestData = new HashMap<>();
//
//                    for (String key : map.keySet())
//                        putRequestData.put(key, new String[]{map.get(key).toString()});
//                }
            }

            return putRequestData;
        } else
            return super.getParameterMap();
    }

    /**
     * 获取 PUT 请求所提交的内容。Servlet 没有 PUT 获取表单，要自己处理 Servlet 不能获取 PUT 请求内容
     * 在标准的 HTTP/1.1 规范中，并没有明确规定 PUT 请求应该如何传递数据
     *
     * @return 参数、值集合
     */
    Map<String, Object> getPutRequestData() {
        try (InputStream in = getInputStream()) {
            String params = StreamHelper.byteStream2string(in);

            if (StringUtils.hasText(params))
                return MapTool.toMap(params.split("&"), v -> ConvertBasicValue.toJavaValue(StringUtils.uriDecode(v, StandardCharsets.UTF_8)));
            else return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    Map<String, String[]> getPutRequestData2() {
        try (InputStream in = getInputStream()) {
            String params = StreamHelper.byteStream2string(in);

            return decodeFormData(params);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解码表单数据
     * 表单形式进行编码的字符串，如何转换为 Map<String, String[]>？
     *
     * @param data 表单数据
     * @return 参数、值集合
     */
    Map<String, String[]> decodeFormData(String data) {
        Map<String, String[]> map = new HashMap<>();
        StringTokenizer tokenizer = new StringTokenizer(data, "&");

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            int index = token.indexOf("=");

            if (index != -1) {
                String key = token.substring(0, index);
                String value = token.substring(index + 1);
                String[] values = map.get(key);

                if (values == null)
                    values = new String[1];

                values[0] = StringUtils.uriDecode(value, StandardCharsets.UTF_8);
                map.put(key, values);
            }
        }

        return map;
    }
}
