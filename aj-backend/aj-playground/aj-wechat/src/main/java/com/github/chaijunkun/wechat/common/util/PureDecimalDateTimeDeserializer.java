package com.github.chaijunkun.wechat.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PureDecimalDateTimeDeserializer extends JsonDeserializer<Date> {

    private static final String format = "yyyyMMddHHmmss";

    private static final SimpleDateFormat sdf = new SimpleDateFormat(format);

    @Override
    public Date deserialize(JsonParser parser, DeserializationContext context) {
        try {
            return sdf.parse(parser.getText());
        } catch (Exception e) {
            Calendar ca = Calendar.getInstance();
            ca.set(1970, Calendar.JANUARY, 1, 0, 0, 0);

            return ca.getTime();
        }
    }
}
