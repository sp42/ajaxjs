package com.github.chaijunkun.wechat.common.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 二进制布尔序列化器
 * @author chaijunkun
 * @since 2015年7月8日
 */
public class BinaryBooleanSerializer extends JsonSerializer<Boolean>{

	@Override
	public void serialize(Boolean value, JsonGenerator gen,	SerializerProvider serializers) throws IOException, JsonProcessingException {
		gen.writeNumber(false == value ? 0 : 1);
	}

}
