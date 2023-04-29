package com.ajaxjs.payment.alipay.model;

import com.ajaxjs.util.StrUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Frank Cheung
 */
public class GroupStringPair {
    public static GroupStringPair parseQueryString(String queryString, String enc) {
        GroupStringPair ret = new GroupStringPair();
        if (queryString == null || queryString.isEmpty())
            return ret;

        StringBuilder key = new StringBuilder();
        StringBuilder value = null;
        boolean isKey = true;

        for (char x : queryString.toCharArray()) {
            if ('&' == x) {
                if (!key.toString().isEmpty())
                    ret.add(StrUtil.urlDecode(key.toString(), enc), value == null ? null : StrUtil.urlDecode(value.toString(), enc));

                key = new StringBuilder();
                value = null;
                isKey = true;
            } else if ('=' == x) {
                isKey = false;
                value = new StringBuilder();
            } else {
                (isKey ? key : value).append(x);
            }
        }

        if (!key.toString().isEmpty()) {
            assert value != null;
            ret.add(StrUtil.urlDecode(key.toString(), enc), StrUtil.urlDecode(value.toString(), enc));
        }

        return ret;
    }

    public static GroupStringPair parsePostBody(final InputStream postBody, final String charset, final String enc) {
        GroupStringPair ret = new GroupStringPair();
        if (postBody == null)
            return ret;

        try (InputStreamReader reader = new InputStreamReader(postBody, charset)) {
            StringBuilder key = new StringBuilder();
            StringBuilder value = null;
            boolean isKey = true;
            char[] buff = new char[1024];
            int size = reader.read(buff);

            while (size != -1) {
                for (int i = 0; i < size; ++i) {
                    if ('&' == buff[i]) {
                        if (!key.toString().isEmpty())
                            ret.add(StrUtil.urlDecode(key.toString(), enc), value == null ? null : StrUtil.urlDecode(value.toString(), enc));

                        key = new StringBuilder();
                        value = null;
                        isKey = true;
                    } else if ('=' == buff[i]) {
                        isKey = false;
                        value = new StringBuilder();
                    } else
                        (isKey ? key : value).append(buff[i]);

                }

                size = reader.read(buff);
            }

            if (!key.toString().isEmpty())
                ret.add(StrUtil.urlDecode(key.toString(), enc), StrUtil.urlDecode(value.toString(), enc));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    private final List<StringPair> entries = new ArrayList<>();

    public void add(String key, String value) {
        entries.add(new StringPair(key, value));
    }

    public String get(String key) {
        for (StringPair entry : entries) {
            if (entry.getFirst().equals(key))
                return entry.getSecond();
        }

        return null;
    }

    public void update(String key, String value) {
        for (StringPair entry : entries) {
            if (entry.getFirst().equals(key))
                entry.setSecond(value);
        }
    }

    public List<StringPair> getOrdered(String... skipKeys) {
        Arrays.sort(skipKeys);
        List<StringPair> ret = new ArrayList<>();

        for (StringPair entry : entries) {
            if (Arrays.binarySearch(skipKeys, entry.getFirst()) >= 0)
                continue;

            ret.add(new StringPair(entry.getFirst(), entry.getSecond()));
        }

        return ret;
    }

    public List<StringPair> getSorted(String... skipKeys) {
        List<StringPair> ret = getOrdered(skipKeys);
        Collections.sort(ret);

        return ret;
    }
}
