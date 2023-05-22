package com.github.chaijunkun.wechat.common.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class DateTimeDeserializer extends JsonDeserializer<Date> {
	
	private static final String format = "yyyy-MM-dd HH:mm:ss";
	
	private static SimpleDateFormat sdf = new SimpleDateFormat(format);

	@Override
	public Date deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
		try{
			return sdf.parse(parser.getText());
		}catch (Exception e) {
			Calendar ca= Calendar.getInstance();
			ca.set(1970, Calendar.JANUARY, 1, 0, 0, 0);
			return ca.getTime();
		}
	}
}
