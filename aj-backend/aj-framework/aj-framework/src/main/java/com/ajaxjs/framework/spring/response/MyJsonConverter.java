package com.ajaxjs.framework.spring.response;

import com.ajaxjs.framework.IBaseModel;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.util.map.MapTool;
import com.ajaxjs.web.WebHelper;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 统一返回 JSON
 *
 * @author Frank Cheung sp42@qq.com
 */
public class MyJsonConverter extends AbstractHttpMessageConverter<Object> {
    /**
     * 对于 POST Raw Body 的识别，通常是 JSON
     */
    private static final MediaType CONTENT_TYPE = new MediaType("application", "json");

    /**
     * 对于 POST 标准表单的识别
     */
    private static final MediaType CONTENT_TYPE_FORM = new MediaType("application", "x-www-form-urlencoded");

    /**
     *
     */
    private static final MediaType CONTENT_TYPE_FORM2 = new MediaType("multipart", "form-data");

    public MyJsonConverter() {
        super(CONTENT_TYPE, CONTENT_TYPE_FORM, CONTENT_TYPE_FORM2);
    }

    @Override
    protected boolean supports(Class<?> clazz /* 控制器方法的参数 */) {
        boolean isContainerType = Map.class.isAssignableFrom(clazz) || List.class.isAssignableFrom(clazz) || IBaseModel.class.isAssignableFrom(clazz);

        if (isContainerType) // 优化：出现频率较高，放在前面
            return true;

        boolean isBolType = clazz == Boolean.class || clazz == boolean.class;
        boolean isIntType = clazz == Integer.class || clazz == int.class;
        boolean isLongType = clazz == Long.class || clazz == long.class;
        boolean isString = clazz == String.class;

        return isBolType || isIntType || isLongType || isString;
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        boolean isMapParams = clazz == Map.class;
        boolean isListParams = clazz == List.class;
        boolean isJavaBean = IBaseModel.class.isAssignableFrom(clazz);

        // 对于 @RequestBody 有效
        if (isMapParams || isJavaBean || isListParams) {
            String str = StreamUtils.copyToString(inputMessage.getBody(), StandardCharsets.UTF_8);
            System.out.println("POST >>>>" + str);

            if (str.startsWith("[") || str.startsWith("{")) { // 识别 JSON
                if (isListParams) {
                    return JsonHelper.parseList(str);
                } else {
                    Map<String, Object> parseMap = JsonHelper.parseMap(str);

                    if (!isJavaBean)
                        return parseMap;
                    else
                        return MapTool.map2Bean(parseMap, clazz, true);// raw body json to bean
                }
            } else {
                if (!StringUtils.hasText(str))
                    throw new IllegalArgumentException("提交了空的字符串");

                str = WebHelper.uriDecode(str);

                Map<String, Object> parseMap = MapTool.toMap(str.split("&"), null);

                return MapTool.map2Bean(parseMap, clazz, true);
            }
        }

        return null;
    }

    @Override
    protected void writeInternal(Object result, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        if (result instanceof String && ((String) result).startsWith(ResponseResult.PLAIN_TEXT_OUTPUT)) {
            // 字符串原文输出
            String str = (String) result;
            str = str.replace(ResponseResult.PLAIN_TEXT_OUTPUT, "");

            try (OutputStream out = outputMessage.getBody()) {
                out.write(str.getBytes());
            }
        } else {
            /*
             * Spring Boot 如果 RestController 中返回 null，则不会走进自定义 HttpMessageConverter
             * https://www.v2ex.com/t/452195
             */
            ResponseResult resultWrapper = new ResponseResult();

            if (result instanceof PageResult) { // 分页总数
                PageResult<?> p = (PageResult<?>) result;

                PageDTO dto = new PageDTO();
                dto.setRows(p);
                dto.setTotal(p.getTotalCount());

                String json = JsonHelper.toJson(dto);
                resultWrapper.setData(json);
                // 旧框架
//                resultWrapper.setTotal(p.getTotalCount());
            } else {
                String json = JsonHelper.toJson(result);
                resultWrapper.setData(json);
            }


            resultWrapper.setMessage("操作成功");
//		MediaType.APPLICATION_JSON_UTF8
            outputMessage.getHeaders().setContentType(CONTENT_TYPE);

            try (OutputStream out = outputMessage.getBody()) {
                out.write(resultWrapper.getBytes());
            }
        }
    }
}