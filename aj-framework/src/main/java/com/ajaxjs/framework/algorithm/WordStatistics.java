package com.ajaxjs.framework.algorithm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

/**
 * 统计一篇英文中的单词数目并按单词出现频率排序并写入文件
 * 
 * 对于这个问题，思路是很明确的，首先从文件中读取文章形成一个字符串，然后用正则模式将字符串以.和一个空格进行分割（PS:模式是：“(\\.)?
 * ”，注意问号后面有个空格，解释：每个单词之间有0个或者一个点加上一个空格）形成Map<key,value>集合，key表示单词，value表示每个单词出现的频率；
 * 
 * 用自定义的compare()方法对Map中的value进行比较排序，最后将结果写入文件
 * 
 * @author https://www.zifangsky.cn/163.html
 *
 */
public class WordStatistics {
	private BufferedReader bufferedReader = null;
	private BufferedWriter bufferedWriter = null;

	/**
	 * 从指定路径读取英文文章，并形成Map集合
	 */
	public Map<String, Integer> readFile() {
		// 读文件
		StringBuffer sb = new StringBuffer();

		try {
			bufferedReader = new BufferedReader(new FileReader(new File("F:\\text1.txt"))); // 文件路径可自定义
			String line = "";
			while ((line = bufferedReader.readLine()) != null)
				sb.append(line);

			bufferedReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 生成<单词,次数>键值对
		Pattern pattern = Pattern.compile("(\\.)? ");
		String[] words = pattern.split(sb.toString());
		Map<String, Integer> word_map = new HashMap<>();

		for (String s : words) {
			if (!word_map.containsKey(s))
				word_map.put(s, 1);
			else {
				int count = word_map.get(s);
				word_map.replace(s, count, count + 1);
			}
		}

		return word_map;
	}

	/**
	 * 按单词的出现频率排序并输出到words.txt文件中
	 */
	public void sortAndWrite(Map<String, Integer> word_map) {
		// 排序
		List<Map.Entry<String, Integer>> list = new ArrayList<>(word_map.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});

		// 写入文件
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(new File("F:\\words.txt")));
			bufferedWriter.write("一共出现了 " + word_map.size() + " 个单词，每个单词和它出现的频率分别是：");
			bufferedWriter.flush();
			bufferedWriter.newLine();
			for (Map.Entry<String, Integer> mapping : list) {
				bufferedWriter.write(mapping.getKey() + " : " + mapping.getValue());
				bufferedWriter.flush();
				bufferedWriter.newLine();
			}

			bufferedWriter.close();
			System.out.println("Work Out");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
