package com.batch_read;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * 专门用于读txt、csv等文本文件的FileParser，它在EnumerationEngineeFactory被调用
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public class TxtEnumerationTask extends EnumerationEnginee {
	public TxtEnumerationTask(BlockingQueue<Map> queue, String txtFileName, String colNames, boolean skipHeader, BatchDTO taskContext) {
		super(queue, txtFileName, colNames, taskContext.isHeadSkip(), taskContext);
	}

	@Override
	public void run() {
		try {
			enumerate(super.fileName, super.colNames);
		} catch (Exception e) {
//			logger.error("read txtFileName error, parse excel quit because :" + e.getMessage(), e);
			try {
				Thread.interrupted();
			} catch (Exception ee) {
			}
		} finally {
			try {
				queue.put(DUMMY);
			} catch (Exception ex) {
			}
		}

	}

	public void enumerate(String txtFileName, String strKeys) throws Exception {
		FileInputStream is = null;
//		StringBuilder sb = new StringBuilder();
		String a_line = "";
//		String[] columnNames = null;
//		String[] cellValues = null;
		Map dataRow = new HashMap();
		int i = 0;

		try {
			File f = new File(txtFileName);
			if (f.exists()) {
				is = new FileInputStream(new File(txtFileName));
				BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				if (skipHeader)
					br.readLine();

				while ((a_line = br.readLine()) != null) {
					if (a_line.trim().length() > 0) {
						String[] data = a_line.split(",");
						for (int index = 0; index < data.length; index++)
							dataRow.put(String.valueOf(index), data[index]);

						dataRow = MapUtil.sortByValue(dataRow);
						queue.put(dataRow);
						dataRow = new HashMap();
						i++;
					}
				}
			}
		} catch (Exception e) {
			throw new Exception("import was interrupted, error happened in " + i + "  row", e);
		} finally {
			try {
				if (is != null) {
					is.close();
					is = null;
				}
			} catch (Exception e) {
			}
		}
	}
}