package com.ajaxjs.util.regexp;

import java.util.regex.Matcher;

/**
 * 抽象的字符串替换接口
 * <p>
 * 主要是添加了 $(group) 方法来替代 matcher.group(group)
 *
 * @author yeyong
 */
public abstract class AbstractReplaceCallBack implements ReplaceCallBack {
    protected Matcher matcher;

    @Override
    public String replace(String text, int index, Matcher matcher) {
        this.matcher = matcher;

        try {
            return doReplace(text, index, matcher);
        } finally {
            this.matcher = null;
        }
    }

    /**
     * 将 text 转化为特定的字串返回
     *
     * @param text    指定的字符串
     * @param index   替换的次序
     * @param matcher Matcher 对象
     */
    public abstract String doReplace(String text, int index, Matcher matcher);

    /**
     * 获得 matcher 中的组数据，等同于 matcher.group(group)
     * <p>
     * 该函数只能在{@link #doReplace(String, int, Matcher)} 中调用
     */
    protected String $(int group) {
        String data = matcher.group(group);

        return data == null ? "" : data;
    }

}