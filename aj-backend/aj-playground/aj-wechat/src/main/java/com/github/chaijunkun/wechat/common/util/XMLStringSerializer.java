package com.github.chaijunkun.wechat.common.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * xml中用来替换大于小于号的
 */
public class XMLStringSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String str, JsonGenerator gen, SerializerProvider provider) throws IOException {
        str = str.replaceAll("&", "&amp;");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        str = str.replaceAll("\"", "&quot;");
        str = str.replaceAll("'", "&apos;");

        gen.writeString(str);
    }

}
