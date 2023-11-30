package com.ajaxjs.framework.spring.response;

import com.ajaxjs.framework.IBaseModel;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.util.WebHelper;
import com.ajaxjs.util.convert.ConvertToJson;
import com.ajaxjs.util.convert.EntityConvert;
import com.ajaxjs.util.convert.MapTool;
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
import java.util.Collections;
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
        boolean isContainerType = Map.class.isAssignableFrom(clazz) || List.class.isAssignableFrom(clazz) || IBaseModel.class.isAssignableFrom(clazz) || clazz == Result.class;

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
//            log.info("POST >>>>" + str);

            if (str.startsWith("[") || str.startsWith("{")) { // 识别 JSON
                if (isListParams) {
                    return EntityConvert.json2MapList(str);
                } else {
                    Map<String, Object> parseMap = EntityConvert.json2map(str);

                    if (!isJavaBean)
                        return parseMap;
                    else
                        return EntityConvert.map2Bean(parseMap, clazz, true);// raw body json to bean
                }
            } else {
                if (!StringUtils.hasText(str))
                    throw new IllegalArgumentException("提交了空的字符串");

                str = WebHelper.uriDecode(str);

                Map<String, Object> parseMap = MapTool.toMap(str.split("&"), null);

                return EntityConvert.map2Bean(parseMap, clazz, true);
            }
        }

        return null;
    }

    @Override
    protected void writeInternal(Object result, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        byte[] output;

        if (result instanceof String && ((String) result).startsWith(ResponseResult.PLAIN_TEXT_OUTPUT)) {
            // 字符串原文输出
            String str = (String) result;
            str = str.replace(ResponseResult.PLAIN_TEXT_OUTPUT, "");

            output = str.getBytes();
        } else if (result instanceof Result) {
            Result<?> r = (Result<?>) result;
            Object r2 = r.getResult();

            if (r2 != null) {
                output = ConvertToJson.toJson(r2).getBytes();
            } else
                output = new byte[]{};
        } else {
            /*
             * Spring Boot 如果 RestController 中返回 null，则不会走进自定义 HttpMessageConverter
             * https://www.v2ex.com/t/452195
             */
            ResponseResult resultWrapper = new ResponseResult();

            String msg = "操作成功";

            if (result instanceof PageResult) { // 分页总数
                PageResult<?> p = (PageResult<?>) result;

                PageDTO dto = new PageDTO();
                dto.setRows(p);
                dto.setTotal(p.getTotalCount());

                String json = ConvertToJson.toJson(dto);
                resultWrapper.setData(json);
                // 旧框架
                //                resultWrapper.setTotal(p.getTotalCount());
            } else if (result.equals(MyResponseBodyAdvice.NULL_DATA)) {
                msg = "找不到数据，查询为空";
                resultWrapper.setData("null");
            } else if (result.equals(Collections.emptyList())) {
                msg = "找不到数据，查询为空";
                resultWrapper.setData("[]");

            } else {
                String json = ConvertToJson.toJson(result);
                resultWrapper.setData(json);
            }

            resultWrapper.setMessage(msg);
            outputMessage.getHeaders().setContentType(CONTENT_TYPE);
            output = resultWrapper.getBytes();
        }

        try (OutputStream out = outputMessage.getBody()) {
            out.write(output);
        }
    }
}