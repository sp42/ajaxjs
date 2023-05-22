package com.github.chaijunkun.wechat.common.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 参与金融计算的 BigDecimal 类型序列化器
 */
public class MoneyBigDecimalSerializer extends JsonSerializer<BigDecimal> {
    private static final DecimalFormat format;

    static {
        format = new DecimalFormat("0.00");
        format.setRoundingMode(RoundingMode.HALF_UP);
    }

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeNumber(format.format(value));
    }

}
