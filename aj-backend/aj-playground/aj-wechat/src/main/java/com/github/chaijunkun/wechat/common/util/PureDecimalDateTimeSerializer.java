package com.github.chaijunkun.wechat.common.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class PureDecimalDateTimeSerializer extends JsonSerializer<Date> {
	
	private static final String format = "yyyyMMddHHmmss";
	
	private static SimpleDateFormat sdf = new SimpleDateFormat(format);

	@Override
	public void serialize(Date date, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonProcessingException {
		gen.writeString(sdf.format(date));
	}

}
