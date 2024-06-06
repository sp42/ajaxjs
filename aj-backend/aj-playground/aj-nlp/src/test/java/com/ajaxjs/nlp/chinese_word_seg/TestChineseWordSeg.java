package com.ajaxjs.nlp.chinese_word_seg;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;

public class TestChineseWordSeg {
    @Test
    public void test() throws IOException {
        // 提前准备需要分词的语言
        String t = "你好，我现在还刚刚接触数据结构，所以还不是太了解!";

        // 创建一个分词对象
        try (Analyzer a = new IKAnalyzer(true); StringReader r = new StringReader(t)) {
            // 对读入的语言开始进行分词操作
            TokenStream to = a.tokenStream("", r);
            // 获得CharTermAttribute类
            CharTermAttribute te = to.getAttribute(CharTermAttribute.class);

            // 依次遍历分词数据，注意要转换成字符串类型
            while (to.incrementToken())
                System.out.print(te.toString() + "，");
        }
    }
}
