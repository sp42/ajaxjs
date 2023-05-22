package com.github.chaijunkun.wechat.common.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * 二进制布尔反序列化器
 * @author chaijunkun
 * @since 2015年7月8日
 */
public class BinaryBooleanDeserializer extends JsonDeserializer<Boolean>{

	@Override
	public Boolean deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return parser.getIntValue() == 0 ? false : true;
	}

}
