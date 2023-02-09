package com.ajaxjs.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 简单读取CSV文件的工具
 * https://blog.csdn.net/howroad/article/details/91866107
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public class CsvUtils {

	/** 默认分隔字符 */
	public static final char DEFAULT_DELIMITER = ',';

	/** 默认编码集 */
	public static final Charset DEFAULT_CHARSET = Charset.defaultCharset();

	/** 空字符串 */
	private static final String EMPTY = "";

	/** 单引号字符 */
	private static final char HYPHEN = '"';

	/** 占位字符 */
	private static final char PLACEHOLDER = '\u02B0';

	/** 占位字符串 */
	private static final String PLACEHOLDER_STR = String.valueOf(PLACEHOLDER);

	/** 分隔符 */
	private static String delimiter = String.valueOf(DEFAULT_DELIMITER);

	/** 分隔符字符 */
	private static char delimiter_char = DEFAULT_DELIMITER;

	/** 编码集 */
	private static Charset charset = DEFAULT_CHARSET;

	/** 是否忽略第一行 */
	private static boolean omitFirstLine = false;

	/** 获得表格元素 */
	public static Map<Integer, Map<String, String>> getElementsFromCsv(String location) {
		return getElementsFromCsv(location, false);
	}

	/**
	 * @param location  The location
	 * @param mapTitles true == map attributes to the titles (if any) found on the
	 *                  top level of the csv.
	 * @return a map containing the line, and its mapping wihting that line. Simple.
	 * @throws FileNotFoundException _
	 */
	public static Map<Integer, Map<String, String>> getElementsFromCsv(String location, boolean mapTitles) {
		return getElementsFromCsv(new File(location), mapTitles);
	}

	public static Map<Integer, Map<String, String>> getElementsFromCsv(final File file) {
		return getElementsFromCsv(file, false);
	}

	/**
	 * @param file      The file
	 * @param mapTitles true == map attributes to the titles (if any) found on the
	 *                  top level of the csv.
	 * @return a map containing the line, and its mapping wihting that line. Simple.
	 */
	public static Map<Integer, Map<String, String>> getElementsFromCsv(File file, boolean mapTitles) {
		try (BufferedReader input = new BufferedReader(new FileReader(file));) {
			Map<Integer, Map<String, String>> entries = new TreeMap<>();
			List<String> titles = new ArrayList<>();
			String currentLine;
			boolean completedFirstLine = false/* , omitted = false */;
			int counter = 0, position = 0;

			while ((currentLine = input.readLine()) != null) {
				if (!"".equals(currentLine)) {
					currentLine = massageLine(currentLine);
					if (mapTitles) {

						if (!completedFirstLine) {
							if (currentLine.contains(delimiter))
								Collections.addAll(titles, currentLine.split(delimiter));
							else
								throw new RuntimeException(String.format("Unable to find {%s} as delimiter. Wrong delimiter?", delimiter));

							completedFirstLine = true;
						}
					}

					final Map<String, String> entryLine = new TreeMap<>();

					if (omitFirstLine) {
						omitFirstLine = false;
						continue;
					}

					for (String entry : currentLine.split(delimiter)) {
						if (entry.contains(PLACEHOLDER_STR))
							entry = entry.replaceAll(PLACEHOLDER_STR, delimiter);

						entryLine.put(((mapTitles) ? titles.get(position) : String.valueOf(position)),
								((charset != DEFAULT_CHARSET) ? new String(entry.getBytes(), charset) : entry));
						position++;
					}

					entries.put(counter, entryLine);
					position = 0;
					counter++;
				}
			}

			return entries;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return Collections.emptyMap();
	}

	private static String massageLine(final String line) {
		if (!"".equals(line)) {
			if (line.contains("\"")) {
				ArrayList<Character> modifiedLine = new ArrayList<>();
				boolean withinHyphenSequence = false;

				for (int i = 0; i < line.length(); i++) {
					if (line.charAt(i) == HYPHEN)
						withinHyphenSequence = ((!withinHyphenSequence));

					if (withinHyphenSequence) {
						if (line.charAt(i) == delimiter_char)
							modifiedLine.add(PLACEHOLDER);
						else
							modifiedLine.add(line.charAt(i));
					} else
						modifiedLine.add(line.charAt(i));
				}

				return getStringFromList(modifiedLine);
			}

			return line;
		}

		return EMPTY;
	}

	private static String getStringFromList(ArrayList<Character> list) {
		StringBuilder sb = new StringBuilder();
		for (char s : list)
			sb.append(s);

		return sb.toString();
	}

	public static void setDelimiter(final String del) {
		if (!"".equals(del)) {
			delimiter = del;
			delimiter_char = del.charAt(0);
		}
	}

	public static void setCharset(final Charset chSet) {
		charset = chSet;
	}

	public static void setOmitFirstLine(final boolean choice) {
		omitFirstLine = choice;
	}

//	/**
//	 * 
//	 * @Description:组装Excel中的标题行
//	 * @param fileName 文件路径
//	 * @param sheetId  sheet页，从0开始
//	 * @param beginRow 始读行，从0开始
//	 * @param beginCol 始读列，从0开始
//	 * @return List<RecordMap>
//	 * @author luhao
//	 * @since：2019年6月13日 下午2:48:01
//	 */
//	public static List<RecordMap> getRecordMapFromCsv(String fileInput, int sheetId, int beginRow, int beginCol) {
//		List<RecordMap> result = new ArrayList<RecordMap>();
//		try {
//			Map<Integer, Map<String, String>> elementsFromCsv = getElementsFromCsv(fileInput, false);
//			Map<String, String> line = elementsFromCsv.get(beginRow);
//			int cols = line.size();
//			for (int i = beginCol; i < cols && beginCol < cols; i++) {
//				RecordMap recordMap = new RecordMap();
//				recordMap.setColName(line.get(String.valueOf(i)));
//				recordMap.setColIndex(i);
//				result.add(recordMap);
//			}
//
//		} catch (FileNotFoundException e) {
//			throw new RuntimeException(e.getMessage());
//		}
//		return result;
//	}
}
