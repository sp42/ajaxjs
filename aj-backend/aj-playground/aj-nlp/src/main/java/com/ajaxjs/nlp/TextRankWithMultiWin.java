/**
 * Author: WuLC
 * Date:   2016-05-23 16:04:42
 * Last modified by:   WuLC
 * Last Modified time: 2016-05-24 16:00:11
 * Email: liangchaowu5@gmail.com
 * Function: integrate results of TextRank algorithm with different size of co-occurance window
 * Input: title and content of a document
 * Output: keywords of  the document
 */
package com.ajaxjs.nlp;

import java.util.*;

public class TextRankWithMultiWin {
    private static int keywordNum = 5;

    /**
     * 设置要提取的关键字数量
     *
     * @param sysKeywordNum 整数类型，指定要提取的关键字的数量
     */
    public static void setKeywordNumber(int sysKeywordNum) {
        keywordNum = sysKeywordNum;
    }

    /**
     * integrate the results of TextRank algorithm  with different co-occurance window
     *
     * @param title(String):   title of the document
     * @param content(String): content of the document
     * @param minWindow(int):  the minimum size of co-occurance window
     * @param maxWindow(int):  the maximum size of co-occurance window
     * @return keywords of the document
     */
    public static List<String> integrateMultiWindow(String title, String content, int minWindow, int maxWindow) {
        Map<String, Float> tempKeywordScore;
        Map<String, Float> allKeywordScore = new HashMap<>();
        String key;
        Float value;

        for (int i = minWindow; i <= maxWindow; i++) {
            TextRank.setWindowSize(i); // set the size of co-occurance window
            tempKeywordScore = TextRank.getWordScore(title, content);
            Iterator<Map.Entry<String, Float>> it = tempKeywordScore.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry<String, Float> entry = it.next();
                key = entry.getKey();
                value = entry.getValue();
                if (allKeywordScore.containsKey(key)) allKeywordScore.put(key, allKeywordScore.get(key) + value);
                else allKeywordScore.put(key, value);
            }
        }
        // sort the result in terms of the score of each word
        List<Map.Entry<String, Float>> entryList = new ArrayList<>(allKeywordScore.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<String, Float>>() {
            @Override
            public int compare(Map.Entry<String, Float> c1, Map.Entry<String, Float> c2) {
                return c2.getValue().compareTo(c1.getValue());
            }
        });

        List<String> fileKeywords = new ArrayList<>();

        for (int j = 0; j < keywordNum; j++)
            fileKeywords.add(entryList.get(j).getKey());

        return fileKeywords;
    }

}

