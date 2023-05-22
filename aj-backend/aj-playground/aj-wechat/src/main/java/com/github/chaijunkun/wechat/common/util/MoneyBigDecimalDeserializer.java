package com.github.chaijunkun.wechat.common.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * 参与金融计算的BigDecimal类型反序列化器
 * @author chaijunkun
 * @since 2015年4月17日
 */
public class MoneyBigDecimalDeserializer extends JsonDeserializer<BigDecimal> {
	
	private static final DecimalFormat format;
	
	static {
		format = new DecimalFormat("0.00");
		format.setParseBigDecimal(true);
	}

	@Override
	public BigDecimal deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		try {
			return ((BigDecimal)format.parse(jp.getText())).setScale(2, RoundingMode.HALF_UP);
		} catch (ParseException e) {
			throw new JsonParseException(e.getMessage(), jp.getCurrentLocation(), e);
		}
	}

}
