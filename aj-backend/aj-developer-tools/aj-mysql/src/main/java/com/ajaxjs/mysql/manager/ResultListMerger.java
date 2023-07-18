/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache License.
 * See the accompanying LICENSE file for terms.
 */
package com.ajaxjs.mysql.manager;

import java.util.List;
import java.util.logging.Logger;

import com.ajaxjs.mysql.common.ResultRowKeyComparator;
import com.ajaxjs.mysql.model.ColumnDescriptor;
import com.ajaxjs.mysql.model.ColumnInfo;
import com.ajaxjs.mysql.model.ResultList;
import com.ajaxjs.mysql.model.ResultRow;
import com.ajaxjs.mysql.model.Sql;

public class ResultListMerger {
	private static Logger logger = Logger.getLogger(ResultListMerger.class.getName());

	public static ResultList mergeByKey(Sql sqlA, Sql sqlB, ResultList listA, ResultList listB, boolean diffOnly) {
		logger.info("Merge diffonly: " + diffOnly);
		if (listA == null)
			return listB;
		if (listB == null)
			return listA;

		// we will use A to construct column descriptor
		ColumnDescriptor desc = new ColumnDescriptor();
		ColumnDescriptor cmpSortDesc = new ColumnDescriptor();
		// ColumnDescriptor cmpDesc = new ColumnDescriptor();
		ColumnDescriptor descA = listA.getColumnDescriptor();
		ColumnDescriptor descB = listB.getColumnDescriptor();
		// 1. Add all keys first
		int pos = 0;
		List<String> keyList = sqlA.getKeyList();
		List<Integer> keyAIdx = new java.util.ArrayList<>(keyList.size());
		List<Integer> keyBIdx = new java.util.ArrayList<>(keyList.size());
		List<String> compKeyList = new java.util.ArrayList<>(keyList.size() + 1);

		for (String s : keyList) {
			ColumnInfo col = descA.getColumn(s).copy();
			desc.addColumn(col.getName(), col.isNumberType(), pos++);
			keyAIdx.add(descA.getColumnIndex(col.getName()));
			keyBIdx.add(descB.getColumnIndex(col.getName()));
			cmpSortDesc.addColumn(col.getName(), col.isNumberType(), cmpSortDesc.getColumns().size());
			// cmpDesc.addColumn(col.getName(), col.isNumberType(),
			// cmpDesc.getColumns().size());
			compKeyList.add(s);
		}
		compKeyList.add("#COMP");
		cmpSortDesc.addColumn("#COMP", false, cmpSortDesc.getColumns().size());
		// 2. add all value
		List<String> valList = sqlA.getValueList();
		List<Integer> valAIdx = new java.util.ArrayList<>(valList.size());
		List<Integer> valBIdx = new java.util.ArrayList<>(valList.size());

		for (String s : valList) {
			ColumnInfo col = descA.getColumn(s).copy();
			ColumnInfo colA = col.copy();
			colA.setName(col.getName() + "_A");
			colA.setPosition(pos++);
			desc.getColumns().add(colA);
			ColumnInfo colB = col.copy();
			colB.setName(col.getName() + "_B");
			colB.setPosition(pos++);
			desc.getColumns().add(colB);
			valAIdx.add(descA.getColumnIndex(col.getName()));
			valBIdx.add(descB.getColumnIndex(col.getName()));
			cmpSortDesc.addColumn(col.getName(), col.isNumberType(), cmpSortDesc.getColumns().size());
			// cmpDesc.addColumn(col.getName(), col.isNumberType(),
			// cmpDesc.getColumns().size());
		}

		List<ResultRow> listRows = new java.util.ArrayList<>(listA.getRows().size() + listB.getRows().size());

		for (ResultRow row : listA.getRows()) {
			ResultRow newRow = new ResultRow();
			List<String> cols = new java.util.ArrayList<>(row.getColumns().size() + 1);
			// add key first
			for (int idx : keyAIdx) {
				if (idx >= 0)
					cols.add(row.getColumns().get(idx));
				else
					cols.add(null);
			}

			cols.add("A");
			for (int idx : valAIdx) {
				if (idx >= 0)
					cols.add(row.getColumns().get(idx));
				else
					cols.add(null);
			}

			newRow.setColumns(cols);
			listRows.add(newRow);
		}

		for (ResultRow row : listB.getRows()) {
			ResultRow newRow = new ResultRow();
			List<String> cols = new java.util.ArrayList<>(row.getColumns().size() + 1);
			// add key first
			for (int idx : keyBIdx) {
				if (idx >= 0)
					cols.add(row.getColumns().get(idx));
				else
					cols.add(null);
			}
			cols.add("B");
			for (int idx : valBIdx) {
				if (idx >= 0)
					cols.add(row.getColumns().get(idx));
				else
					cols.add(null);
			}
			newRow.setColumns(cols);
			listRows.add(newRow);
		}

		// sort new list
		ResultRowKeyComparator sortcomp = new ResultRowKeyComparator(compKeyList, cmpSortDesc, cmpSortDesc);
		ResultRowKeyComparator comp = new ResultRowKeyComparator(keyList, cmpSortDesc, cmpSortDesc);
		ResultRowKeyComparator valComp = new ResultRowKeyComparator(valList, cmpSortDesc, cmpSortDesc);
		java.util.Collections.sort(listRows, sortcomp);

		// now start to contruct the final list
		ResultList finalList = new ResultList(Math.max(listA.getRows().size(), listB.getRows().size()));
		finalList.setColumnDescriptor(desc);
		int combinedSize = listRows.size();
		int keySize = keyList.size();
		if (combinedSize == 0)
			return finalList;
		ResultRow curRow = listRows.get(0);
		String curComp = curRow.getColumns().get(keySize);
		int idx = 1;

		while (idx <= combinedSize) {
			ResultRow nextRow = idx < combinedSize ? listRows.get(idx) : null;
			String nextComp = nextRow != null ? nextRow.getColumns().get(keySize) : null;
			// System.out.println("comp: "+curComp+", "+nextComp);
			if (nextComp == null || curComp == nextComp || comp.compare(curRow, nextRow) != 0)// from same group, or next row does not have the same key
			{
				ResultRow row = new ResultRow();
				row.setColumnDescriptor(desc);
				List<String> cols = new java.util.ArrayList<>(curRow.getColumns().size() - 1);
				for (int i = 0; i < curRow.getColumns().size(); i++) {
					if (i == keySize)
						continue;
					else if (i < keySize)
						cols.add(curRow.getColumns().get(i));
					else if ("A".equals(curComp)) {
						cols.add(curRow.getColumns().get(i));
						cols.add(null);
					} else if ("B".equals(curComp)) {
						cols.add(null);
						cols.add(curRow.getColumns().get(i));
					}

				}
				row.setColumns(cols);
				finalList.addRow(row);
				curComp = nextComp;
				curRow = nextRow;
				idx++;
			} else // from diff group, with the same key
			{
				if (!diffOnly || valComp.compare(curRow, nextRow) != 0) {
					ResultRow row = new ResultRow();
					row.setColumnDescriptor(desc);
					List<String> cols = new java.util.ArrayList<>(curRow.getColumns().size() - 1);
					for (int i = 0; i < curRow.getColumns().size(); i++) {
						if (i == keySize)
							continue;
						else if (i < keySize)
							cols.add(curRow.getColumns().get(i));
						else if ("A".equals(curComp)) {
							cols.add(curRow.getColumns().get(i));
							cols.add(nextRow.getColumns().get(i));
						} else if ("B".equals(curComp)) {
							cols.add(nextRow.getColumns().get(i));
							cols.add(curRow.getColumns().get(i));
						}

					}
					row.setColumns(cols);
					finalList.addRow(row);
				}

				if (idx == combinedSize - 1)
					break;// at the end
				curRow = listRows.get(idx + 1);// pre fetch one row
				curComp = curRow.getColumns().get(keySize);
				idx = idx + 2;
			}
		}

		return finalList;
	}
}
