package com.ajaxjs.nlp;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.DicAnalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TfIdfUtil {
    // 文档表
    private List<String> documents;
    // 文档与词汇列表
    private List<List<String>> documentWords;
    // 文档词频统计表
    private List<Map<String, Integer>> docuementTfList;
    // 词汇出现文档数统计表
    private Map<String, Double> idfMap;
    // 是否只抽取名词
    private boolean onlyNoun = false;

    public TfIdfUtil(List<String> documents) {
        this.documents = documents;
    }

    public List<Map<String, Double>> eval() {
        this.splitWord();
        this.calTf();
        this.calIdf();
        return calTfIdf();
    }

    /**
     * 获取所有文档数，用于逆文档频率 IDF 的计算
     *
     * @return 所有文档数
     */
    private int getDocumentCount() {
        return documents.size();
    }

    /**
     * 对每一个文档进行词语切分
     * 此方法不接受参数，也不返回值，但会修改类级别的变量documentWords
     * documentWords会变为一个列表的列表，每个子列表包含一个文档中切分出的词语
     */
    private void splitWord() {
        documentWords = new ArrayList<>();

        for (String document : documents) { // 遍历所有文档
            Result splitWordRes = DicAnalysis.parse(document); // 对文档进行词语切分
            List<String> wordList = new ArrayList<>();

            for (Term term : splitWordRes.getTerms()) { // 遍历切分结果中的每个词语
                if (onlyNoun) { // 如果只保留名词
                    // 判断词语性质是否为名词，并收集到wordList中
                    if (term.getNatureStr().equals("n") || term.getNatureStr().equals("ns") || term.getNatureStr().equals("nz"))
                        wordList.add(term.getName());
                } else wordList.add(term.getName()); // 如果不只保留名词，则收集所有词语
            }

            documentWords.add(wordList); // 将处理过的词语列表添加到documentWords中
        }
    }

    /**
     * 计算每个文档的词频
     * 它遍历每个文档的词列表，计算每个词在文档中的出现次数，然后将这些信息存储在文档词频列表中。
     */
    private void calTf() {
        docuementTfList = new ArrayList<>();

        for (List<String> wordList : documentWords) { // 遍历每个文档的词组
            Map<String, Integer> countMap = new HashMap<>();

            for (String word : wordList) { // 遍历当前文档中的所有词
                if (countMap.containsKey(word)) countMap.put(word, countMap.get(word) + 1);
                else countMap.put(word, 1); // 计算词频
            }

            docuementTfList.add(countMap); // 将当前文档的词频映射添加到列表中
        }
    }

    /**
     * 计算逆文档频率 IDF
     * IDF值表示一个词语的重要性，它与该词语在文档集中出现的文档数成反比。
     * 这个方法不接受参数，也没有返回值，它会计算并存储每个词语的IDF值。
     */
    private void calIdf() {
        int documentCount = getDocumentCount(); // 获取文档总数
        idfMap = new HashMap<>();
        // 统计词语在多少文档里面出现了
        Map<String, Integer> wordAppearendMap = new HashMap<>();

        // 遍历所有文档的词频统计，汇总每个词语出现的文档数
        for (Map<String, Integer> countMap : docuementTfList) {
            for (String word : countMap.keySet()) {
                if (wordAppearendMap.containsKey(word)) wordAppearendMap.put(word, wordAppearendMap.get(word) + 1);
                else wordAppearendMap.put(word, 1);
            }
        }

        // 对每个词语进行计算，求其IDF值，并存储到idfMap中
        for (String word : wordAppearendMap.keySet()) {
            double idf = Math.log(documentCount / (wordAppearendMap.get(word) + 1));
            idfMap.put(word, idf);
        }
    }

    private List<Map<String, Double>> calTfIdf() {
        List<Map<String, Double>> tfidfRes = new ArrayList<>();

        for (Map<String, Integer> docuementTfMap : docuementTfList) {
            Map<String, Double> tfIdf = new HashMap<>();

            for (String word : docuementTfMap.keySet()) {
                double tfidf = idfMap.get(word) * docuementTfMap.get(word);
                tfIdf.put(word, tfidf);
            }

            tfidfRes.add(tfIdf);
        }

        return tfidfRes;
    }
}
