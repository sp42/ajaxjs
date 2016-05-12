/**
 * Copyright 2015 Frank Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.javatools.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.ajaxjs.core.LogFactory;

/**
 * 表信息
 * @author Frank Cheung 参考 http://www.cnblogs.com/lbangel/p/3487796.html
 *
 */
public class MetaInfo {
	private final static Logger LOGGER = LogFactory.getLog(MetaInfo.class);
	
	/**
	 * 数据库名称
	 */
	public String name;
	
	/**
	 * 数据库元数据
	 */
	public DatabaseMetaData metadata;
	
	/**
	 * 数据库下面所有的表
	 */
	public Table[] tables;
	
	/**
	 * 显示元数据信息
	 * 
	 * @param conn
	 *            数据连接对象
	 */
    public MetaInfo(Connection conn){
		try {
			metadata = conn.getMetaData();
			System.out.println("数据库 " + metadata.getURL() + " 的元数据如下：");  
			
			// 显示元数据信息  
			System.out.println("驱动: " + metadata.getDriverName());  
			System.out.println("驱动版本号: " + metadata.getDriverVersion());  
			System.out.println("登陆用户名: " + metadata.getUserName());  
			System.out.println("数据库产品名: " + metadata.getDatabaseProductName());  
			System.out.println("数据库产品版本号: " + metadata.getDatabaseProductVersion()); 
			
			name = metadata.getDatabaseProductName();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		tables = getTables(metadata);
    }
    
	/**
	 * 获取所有表
	 * 
	 * @param metadata
	 *            元数据对象
	 * @return 表数组
	 */
    private static Table[] getTables(DatabaseMetaData metadata) {
    	List<Table> tablesList = new ArrayList<Table>();
        try(ResultSet rs = metadata.getTables(null, "%", "%", new String[]{"TABLE"});){
        	while (rs.next()) {
        		String tableName = rs.getString("TABLE_NAME");
        		tablesList.add(new Table(metadata, tableName));
        	}
        } catch (SQLException e) {
			e.printStackTrace();
		}
        
        return tablesList.toArray(new Table[tablesList.size()]);
    }
    
	/**
	 * 表信息
	 * 
	 * @author Frank Cheung
	 */
	public static class Table {
		/**
		 * 表名称
		 */
		public String tableName;
		public String tableComment;
		
		/**
		 * 表所有的列
		 */
		public Field[] cols;
 
		/**
		 * 初始化 Table，显示元数据信息  
		 * 
		 * @param metadata
		 *            元数据对象
		 * @param tableName
		 *            表名
		 */
		public Table(DatabaseMetaData metadata, String tableName) {
			this.tableName = tableName;
			cols = MetaInfo.getColumns(metadata, tableName);
			getPrimaryKeys(metadata, tableName);
		}
	}

	/**
	 * 获取所有的列
	 * 
	 * @param md
	 *            元数据对象
	 * @param tableName
	 *            表名
	 * @return 列数组
	 */
	public static Field[] getColumns(DatabaseMetaData md, String tableName){
		List<Field> cols = new ArrayList<>();
		
		try(ResultSet col = md.getColumns(null, null, tableName, null);){	 
			while (col.next()) {
				Field f = new Field();
				String name = col.getString("COLUMN_NAME");// 参数值可参考dm.getColumns(catalog,
															// null, tableName,
															// null)的帮助文档
			
				f.setName(name);
	
				// DATA_TYPE int => SQL type from java.sql.Types
				String dataType = col.getString("DATA_TYPE");
				f.setSqlType(new Integer(dataType).intValue());// 如：java.sql.Types.INTEGER
	
				String type = col.getString("TYPE_NAME");// 如:BIGINT
				f.setTypeName(type);
	
				String position = col.getString("ORDINAL_POSITION");// 在表中的位置
				f.setPosition(position);
	
				String size = col.getString("COLUMN_SIZE");// 字段长度//获取COLUMN_SIZE
				f.setSize(size);
	
				String bufferLength = col.getString("BUFFER_LENGTH");// 字段缓冲区大小
				f.setBufferLength(bufferLength);
	
				String decimal = col.getString("DECIMAL_DIGITS");// 精度
				f.setDecimal(decimal);
	
				String defaultValue = col.getString("COLUMN_DEF");
				f.setDefaultValue(defaultValue);
	
				String remark = col.getString("REMARKS");
				f.setRemark(remark);
	
				String nullable = col.getString("NULLABLE");// 取值
															// 0||1,1允许空值,0不允许空值
				if ("0".equals(nullable))
					f.setNullable(false);
	
				if ("1".equals(nullable))
					f.setNullable(true);
				
				cols.add(f);
	       }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return cols.toArray(new Field[cols.size()]);
	}
	
	/**
	 * 获取主键信息
	 * 
	 * @param metadata
	 *            元数据对象
	 * @param tableName
	 *            表名
	 */
	public static String getPrimaryKeys(DatabaseMetaData metadata, String tableName) {
		try (ResultSet rs = metadata.getPrimaryKeys(null, null, tableName);) {
			System.out.println(rs.isClosed());
			if(rs.isClosed()){
				LOGGER.info(String.format("该表%s没有设置任何主键！", tableName));
				return null;
			}else
				return rs.getString("COLUMN_NAME");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
