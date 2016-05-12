package com.ajaxjs.javatools.sql;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class Utils {
	/**
	 * 查询数据库是否有某表 
	 * @param conn
	 * @param tableName
	 * @return
	 */
	public static boolean getAllTableName(Connection conn, String tableName) {  
	    String[] types = { "TABLE" };  
	    DatabaseMetaData dbMetaData = null;
	    
		try {
			dbMetaData = conn.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}  
		
        try(ResultSet tabs = dbMetaData.getTables(null, null, tableName, types);) {
			if (tabs.next()) return true;    
		} catch (SQLException e) {
			e.printStackTrace();
		}  
        
	    return false;  
	}
	
	/**
	 * 传入Java对象 自动创建动态表
	 * 字段的支持只有int和Integer，double和Double还有String，同时对于String统一创建为了Varchar(100)的字段 表会统一创建一个id自增主键
	 * @param tableName
	 * @param obj
	 * @param noCol
	 * @return
	 */
	public static String createTable(String tableName, Object obj, Map<String, ?> noCol){     
        StringBuilder sb = new StringBuilder("");     
        sb.append("CREATE TABLE `" + tableName + "` (");     
        sb.append(" `id` int(11) NOT NULL AUTO_INCREMENT,");  
        
        Class<?> c = obj.getClass();  
        Field field[] = c.getDeclaredFields();  
        
        for (Field f : field) {  
            if(noCol.get(f.getName()) == null){  
                String type = f.getType().toString();  
                if(type.equals("class java.lang.String")){// Str  
                     sb.append("`" + f.getName() + "` varchar(100) DEFAULT NULL,");     
                }else if(type.equals("int") || type.equals("class java.lang.Integer")){// int  
                    sb.append("`" + f.getName() + "` int(11) DEFAULT NULL,");     
                }else if(type.equals("double") || type.equals("class java.lang.Double")){// double  
                    sb.append("`" + f.getName() + "` double DEFAULT NULL,");     
                }  
            }  
        }  
        
        sb.append(" `tableName` varchar(255) DEFAULT NULL,");  
        sb.append(" PRIMARY KEY (`id`)");     
        sb.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;");     
        
        return sb.toString();     
    }   
}
