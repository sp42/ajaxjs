package com.ajaxjs.spring.response;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import com.ajaxjs.framework.IBaseModel;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.spring.easy_controller.ServiceProxy;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.util.map.MapTool;

/**
 * 统一返回 JSON
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class MyJsonConverter extends AbstractHttpMessageConverter<Object> {
	private static final MediaType CONTENT_TYPE = new MediaType("application", "json");

	public MyJsonConverter() {
		super(CONTENT_TYPE);
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		boolean isContainerType = Map.class.isAssignableFrom(clazz) || List.class.isAssignableFrom(clazz)
				|| IBaseModel.class.isAssignableFrom(clazz);

		if (isContainerType) // 优化：出现频率较高，放在前面
			return true;

		boolean isBolType = clazz == Boolean.class || clazz == boolean.class;
		boolean isIntType = clazz == Integer.class || clazz == int.class;
		boolean isLongType = clazz == Long.class || clazz == long.class;

		return isBolType || isIntType || isLongType;
	}

	/**
	 * 从请求中读取该类型的方法参数
	 */
	@Override
	protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {

		boolean isMapParams = clazz == Map.class;
		boolean isListParams = clazz == List.class;
		boolean isJavaBean = IBaseModel.class.isAssignableFrom(clazz);
//		System.out.println("readInternal:::" + inputMessage);
//		System.out.println("readInternal:::" + isJavaBean);

		// 对于 @RequestBody 有效
		if (isMapParams || isJavaBean || isListParams) {
			String jsonStr = StreamUtils.copyToString(inputMessage.getBody(), Charset.forName("UTF-8"));

			if (isListParams) {
				List<Map<String, Object>> parseList = JsonHelper.parseList(jsonStr);
				return parseList;
			} else {
				Map<String, Object> parseMap = JsonHelper.parseMap(jsonStr);

				if (!isJavaBean)
					return parseMap;
				else
					// raw body json to bean
					return MapTool.map2Bean(parseMap, clazz, true);
			}
		}

		return null;

	}

	@Override
	protected void writeInternal(Object result, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		/*
		 * Spring Boot 如果 RestController 中返回 null，则不会走进自定义 HttpMessageConverter
		 * https://www.v2ex.com/t/452195 暂时无解，请不要返回 null
		 */
		ResponseResult resultWarpper = new ResponseResult();
		String json = JsonHelper.toJson(result);
		resultWarpper.setData(json);

		if (result instanceof PageResult) { // 分页总数
			PageResult<?> p = (PageResult<?>) result;
			resultWarpper.setTotal(p.getTotalCount());
		}

		String comment = ServiceProxy.ACTION_COMMNET.get();
		if (StringUtils.hasText(comment))
			resultWarpper.setMessage(comment);

		outputMessage.getHeaders().setContentType(CONTENT_TYPE);

		try (OutputStream out = outputMessage.getBody();) {
			out.write(resultWarpper.getBytes());
		}
	}
}