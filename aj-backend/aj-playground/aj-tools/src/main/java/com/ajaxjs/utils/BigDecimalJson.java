//package com.ajaxs.utils;
//
//import java.io.IOException;
//import java.math.BigDecimal;
//
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonSerializer;
//import com.fasterxml.jackson.databind.SerializerProvider;
//
//public class BigDecimalJson {
//    /**
//     * 标注序列化BigDecimal为字符串
//     */
//    public class AlexBigDecimalSerializer extends JsonSerializer<BigDecimal> {
//        @Override
//        public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers)
//                throws IOException, JsonProcessingException {
//            if (value != null)
//                gen.writeString(value.toString());
//            else
//                gen.writeNull();
//        }
//    }
//}
