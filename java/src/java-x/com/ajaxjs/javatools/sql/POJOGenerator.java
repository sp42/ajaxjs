package com.ajaxjs.javatools.sql;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * JDBC根据数据库生成POJO
 * http://yuancihang.iteye.com/blog/897605
 * @author yuancihang
 *
 */
public class POJOGenerator {
	public static List<String> getTableNameList(Connection conn) throws SQLException {
		List<String> list = new ArrayList<>();
		DatabaseMetaData dmd = conn.getMetaData();
		ResultSet tables = dmd.getTables(null, dmd.getUserName(), null, new String[] { "TABLE" });

		while (tables.next())
			list.add(tables.getString("TABLE_NAME"));

		return list;
	}

	public static void generatPOJO(Connection conn, String tableName, String packageName, String outputDir) throws SQLException, ClassNotFoundException, IOException {
		DatabaseMetaData dmd = conn.getMetaData();
		ResultSet columns = dmd.getColumns(null, dmd.getUserName(), tableName, null);
		
		StringBuilder classBuilder = new StringBuilder();
		classBuilder.append("package ").append(packageName).append(";").append("\n\n");
		classBuilder.append("public class ").append(buildClassName(tableName)).append("{").append("\n\n");
		
		Map<String, String> fieldMap = new LinkedHashMap<>();
		while (columns.next()) {
			// System.out.println(columns.getString("COLUMN_NAME") + ", " +
			// columns.getInt("DATA_TYPE") + ", " +
			// columns.getString("TYPE_NAME"));
			fieldMap.put(columns.getString("COLUMN_NAME"), getClassNameForJavaType(columns.getInt("DATA_TYPE")));
		}
		
		Set<String> fieldNameSet = fieldMap.keySet();
		for (String fieldName : fieldNameSet) {
			classBuilder.append("\tprivate ").append(fieldMap.get(fieldName))
					.append(" ").append(buildPropertyName(fieldName))
					.append(";\n");
		}
		classBuilder.append("\n");
		
		for (String fieldName : fieldNameSet) {
			String propertyName = buildPropertyName(fieldName);
			Class<?> fieldType = String.class;
			
			if (fieldMap.get(fieldName).equals("Boolean")) 
				fieldType = Boolean.class;
			
			classBuilder.append("\tpublic ").append(fieldMap.get(fieldName)).append(" ").append(ReflectUtil.generatReadMethodName(propertyName, fieldType));
			classBuilder.append("(){\n");
			classBuilder.append("\t\t return this.").append(propertyName).append(";\n\t}\n");
			classBuilder.append("\n");
			classBuilder.append("\tpublic void ").append(ReflectUtil.generatWriteMethodName(propertyName))
					.append("(").append(fieldMap.get(fieldName)).append(" ")
					.append(propertyName).append("){\n");
			classBuilder.append("\t\tthis.").append(propertyName).append(" = ").append(propertyName).append(";\n\t}\n");
			classBuilder.append("\n");
		}
		classBuilder.append("}");

		writeToFile(classBuilder.toString(), new File(new File(outputDir), buildClassName(tableName) + ".java").getAbsolutePath());
	}

	 
	public static void writeToFile(String content, String fullPath)throws IOException {
		writeToFile(content, fullPath, false, "UTF-8");
	}

	/**
	 * 将一个字符串写入一个文本文件中
	 * 
	 * @param content
	 *            String 要写入的字符串
	 * @param fullPath
	 * @param append
	 *            boolean 是追加还是覆盖，true为追加
	 * @param encoding
	 *            String 文本文件的编码
	 * @throws IOException
	 */
	public static void writeToFile(String content, String fullPath, boolean append, String encoding) throws IOException {
		File parent = new File(new File(fullPath).getParent()); // 得到父文件夹
		if (!parent.exists())parent.mkdirs();
		
		FileOutputStream fos = new FileOutputStream(new File(fullPath).getAbsolutePath(), append);
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(fos, encoding));
		pw.print(content);
		pw.close();
		fos.close();
	}

	private static String buildClassName(String tableName) {
		String[] tableNameSections = tableName.split("_");
		StringBuilder tb = new StringBuilder();
		
		for (String tableNameSection : tableNameSections) {
			String tmp = tableNameSection.toLowerCase();
			tb.append(Character.toUpperCase(tmp.charAt(0))).append(tmp.substring(1));
		}
		
		return tb.toString();
	}

	private static String buildPropertyName(String fieldName) {
		String[] fieldNameSections = fieldName.split("_");
		StringBuilder tb = new StringBuilder();
		
		for (int i = 0; i < fieldNameSections.length; i++) {
			String tableNameSection = fieldNameSections[i];
			String tmp = tableNameSection.toLowerCase();
			
			if (i == 0 )tb.append(tmp);
			else tb.append(Character.toUpperCase(tmp.charAt(0))).append(tmp.substring(1));
		}

		if (tb.toString().equals("class")) return "className";
		
		return tb.toString();
	}

	public static void printInfo(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int numberOfColumns = rsmd.getColumnCount();
		for (int i = 1; i <= numberOfColumns; i++) {
			System.out.println("rsmd.getColumnName(i) = " + rsmd.getColumnName(i));
		}
		// while(rs.next()){
		// System.out.println(rs.getString("TABLE_NAME"));
		// }
		// System.out.println("numberOfColumns = " + numberOfColumns);
		// System.out.println("rsmd.getColumnName(1) = " +
		// rsmd.getColumnName(1));
		// System.out.println("rsmd.getColumnLabel(1) = " +
		// rsmd.getColumnLabel(1));
		// System.out.println("rsmd.getColumnType(1) = " +
		// rsmd.getColumnType(1));
		System.out.println("rsmd.getColumnClassName(1) = " + rsmd.getColumnClassName(1));
	}

	static String getClassNameForJavaType(int javaType) {
		switch (javaType){
		case -7:
		case 16:
			/* 733 */return "Boolean";
			/*     */case -6:
			/* 741 */return "Integer";
			/*     */case 5:
			/* 749 */return "Integer";
			/*     */case 4:
			/* 758 */return "Long";
			/*     */case -5:
			/* 763 */return "Long";
			/*     */case 2:
		case 3:
			/* 770 */return "java.math.BigDecimal";
			/*     */case 7:
			/* 773 */return "Float";
			/*     */case 6:
			/*     */
		case 8:
			/* 777 */return "Double";
			/*     */case -1:
		case 1:
		case 12:
			/* 783 */return "String";
			/*     */case -4:
		case -3:
		case -2:
			/* 795 */return "byte[]";
			/*     */case 91:
			/* 801 */return "java.sql.Date";
			/*     */case 92:
			/* 804 */return "java.sql.Time";
			/*     */case 93:
			/* 807 */return "java.sql.Timestamp";
		}
		/* 810 */return "Object";
	}

}
