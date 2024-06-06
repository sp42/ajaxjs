/**
 * Author: WuLC
 * Date:   2016-05-22 17:46:15
 * Last modified by:   WuLC
 * Last Modified time: 2016-05-23 23:31:25
 * Email: liangchaowu5@gmail.com
 * ***********************************************************
 * Function:get keywords of file through TF-IDF algorithm
 * Input: path of directory of files that need to extract keywords
 * Output: keywords of each file
 */

package com.ajaxjs.nlp;

import com.ajaxjs.nlp.parsedoc.ReadDir;
import com.ajaxjs.nlp.parsedoc.ReadFile;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.common.Term;

import java.util.*;

public class TFIDF {
    private static int keywordsNumber = 5;

    /**
     * change the number of keywords,default 5
     *
     * @param keywordNum number of keywords that need to be extracted
     */
    public static void setKeywordsNumber(int keywordNum) {
        keywordsNumber = keywordNum;
    }

    /**
     * 计算文件内容中每个词的TF（Term Frequency）值
     *
     * @param fileContent 文件的内容
     * @return 以“词:TF值”为键值对的 Map
     */
    public static Map<String, Float> getTF(String fileContent) {
        // 使用HanLP对文件内容进行分词
        List<Term> terms = HanLP.segment(fileContent);
        ArrayList<String> words = new ArrayList<>();

        // 筛选出需要包含的词
        for (Term t : terms) {
            if (TFIDF.shouldInclude(t)) words.add(t.word);
        }

        // 统计每个词的出现次数
        Map<String, Integer> wordCount = new HashMap<>();
        Map<String, Float> TFValues = new HashMap<>();

        for (String word : words) {
            // 对于每个词，记录其出现次数
            if (wordCount.get(word) == null) wordCount.put(word, 1);
            else wordCount.put(word, wordCount.get(word) + 1);
        }

        int wordLen = words.size();
        // 遍历词频统计结果，计算TF值
        Iterator<Map.Entry<String, Integer>> iter = wordCount.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry<String, Integer> entry = iter.next();
            // 计算词的TF值并存储
            TFValues.put(entry.getKey(), Float.parseFloat(entry.getValue().toString()) / wordLen);
        }

        return TFValues;
    }

    /**
     * 判断一个词是否属于停用词
     *
     * @param term 需要判断的词
     * @return 如果该词是停用词，则返回  false；否则返回 true
     */
    public static boolean shouldInclude(Term term) {
        // 利用 CoreStopWordDictionary 判断该词是否应被包含（即是否不属于停用词）
        return CoreStopWordDictionary.shouldInclude(term);
    }

    /**
     * 计算目录下每个文件中每个词的 TF 值
     *
     * @param dirPath 目录的路径
     * @return 文件路径与其对应的词-TF值映射的映射
     */
    public static Map<String, Map<String, Float>> tfForDir(String dirPath) {
        Map<String, Map<String, Float>> allTF = new HashMap<>(); // 初始化存储所有文件的词频信息的映射
        List<String> filelist = ReadDir.readDirFileNames(dirPath); // 获取目录中所有文件的列表

        // 遍历文件列表
        for (String file : filelist) {
            // 读取文件内容
            String content = ReadFile.loadFile(file); // 需要修改ReadFile类的loadFile方法以适应不同的文件读取需求
            Map<String, Float> dict = TFIDF.getTF(content);// 计算文件中每个词的TF值
            allTF.put(file, dict); // 将文件的词频信息添加到总映射中
        }

        return allTF;
    }

    /**
     * 计算目录下每个单词的IDF值
     *
     * @param dirPath 目录的路径
     * @return 包含“单词:IDF值”的键值对集合
     */
    public static Map<String, Float> idfForDir(String dirPath) {
        // 读取目录中的文件列表
        List<String> fileList = ReadDir.readDirFileNames(dirPath);
        int docNum = fileList.size(); // 文档数量
        Map<String, Set<String>> passageWords = new HashMap<>();

        // 为每篇文档提取不重复的单词
        for (String filePath : fileList) {
            List<Term> terms;
            Set<String> words = new HashSet<>();
            String content = ReadFile.loadFile(filePath); // 加载文件内容
            terms = HanLP.segment(content); // 分词

            // 筛选并收集单词
            for (Term t : terms) {
                if (TFIDF.shouldInclude(t))
                    words.add(t.word);
            }

            passageWords.put(filePath, words);
        }

        // 计算单词在文档集合中的出现频率
        Map<String, Integer> wordPassageNum = new HashMap<>();

        for (String filePath : fileList) {
            Set<String> wordSet = passageWords.get(filePath);

            // 统计单词出现次数
            for (String word : wordSet) {
                if (wordPassageNum.get(word) == null)
                    wordPassageNum.put(word, 1);
                else
                    wordPassageNum.put(word, wordPassageNum.get(word) + 1);
            }
        }

        Map<String, Float> wordIDF = new HashMap<>();
        // 计算每个单词的IDF值
        Iterator<Map.Entry<String, Integer>> iter_dict = wordPassageNum.entrySet().iterator();

        while (iter_dict.hasNext()) {
            Map.Entry<String, Integer> entry = iter_dict.next();
            float value = (float) Math.log(docNum / (Float.parseFloat(entry.getValue().toString())));
            wordIDF.put(entry.getKey(), value);
        }

        return wordIDF;
    }

    /**
     * 计算目录下每个文件中每个单词的TF-IDF值
     *
     * @param dirPath(String): 目录的路径
     * @return 文件路径与其包含单词的TF-IDF值的映射
     */
    public static Map<String, Map<String, Float>> getDirTFIDF(String dirPath) {
        // 计算目录下每个文件的词频(TF)
        Map<String, Map<String, Float>> dirFilesTF = TFIDF.tfForDir(dirPath);
        // 计算目录下所有文件中每个单词的逆文档频率(IDF)
        Map<String, Float> dirFilesIDF = TFIDF.idfForDir(dirPath);
        // 初始化存储TF-IDF值的映射
        Map<String, Map<String, Float>> dirFilesTFIDF = new HashMap<>();
        // 获取目录下的所有文件名
        List<String> fileList = ReadDir.readDirFileNames(dirPath);
        Map<String, Float> singlePassageWord;

        // 遍历每个文件，计算其单词的TF-IDF值
        for (String filePath : fileList) {
            Map<String, Float> temp = new HashMap<>();
            // 获取当前文件的词频映射
            singlePassageWord = dirFilesTF.get(filePath);
            Iterator<Map.Entry<String, Float>> it = singlePassageWord.entrySet().iterator();

            // 计算当前文件中每个单词的TF-IDF值
            while (it.hasNext()) {
                Map.Entry<String, Float> entry = it.next();
                String word = entry.getKey();
                Float TFIDF = entry.getValue() * dirFilesIDF.get(word); // TF * IDF
                temp.put(word, TFIDF);
            }

            // 将当前文件的TF-IDF值映射添加到结果中
            dirFilesTFIDF.put(filePath, temp);
        }

        return dirFilesTFIDF;
    }

    /**
     * 获取某个目录下每个文件的关键词
     *
     * @param dirPath 目录的路径
     * @return 包含每个文件路径及其对应关键词的映射
     */
    public static Map<String, List<String>> getKeywords(String dirPath) {
        // 读取目录下的所有文件名
        List<String> fileList = ReadDir.readDirFileNames(dirPath);
        // 计算目录下每个文件中每个词的TF-IDF值
        Map<String, Map<String, Float>> dirTFIDF = TFIDF.getDirTFIDF(dirPath);
        Map<String, List<String>> keywordsForDir = new HashMap<>();

        for (String file : fileList) {
            // 获取单个文件的TF-IDF值
            Map<String, Float> singlePassageTFIDF;
            singlePassageTFIDF = dirTFIDF.get(file);
            // 根据TF-IDF值降序排序关键词
            List<Map.Entry<String, Float>> entryList = new ArrayList<>(singlePassageTFIDF.entrySet());

            Collections.sort(entryList, new Comparator<Map.Entry<String, Float>>() {
                @Override
                public int compare(Map.Entry<String, Float> c1, Map.Entry<String, Float> c2) {
                    return c2.getValue().compareTo(c1.getValue());
                }
            });

            // 提取关键词
            List<String> systemKeywordList = new ArrayList<>();
            // 注意：这里似乎遗漏了关键词数量的控制变量keywordsNumber，需要从外部传入或定义
            for (int k = 0; k < keywordsNumber; k++) {
                try {
                    systemKeywordList.add(entryList.get(k).getKey());
                } catch (IndexOutOfBoundsException e) {
                    // 捕获索引越界异常，但未处理，可能需要添加日志记录或错误处理逻辑
                }
            }

            keywordsForDir.put(file, systemKeywordList);
        }

        return keywordsForDir;
    }


}
