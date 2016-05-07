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
package com.ajaxjs.framework.service;

import java.lang.reflect.Field;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.ibatis.session.SqlSession;

import com.ajaxjs.app.App;
import com.ajaxjs.framework.model.BaseModel;
import com.ajaxjs.framework.model.EntityDescription;
import com.ajaxjs.framework.model.FieldDescription;
import com.ajaxjs.util.Reflect;
import com.ajaxjs.util.db.FieldMetaInfo;

/**
 * 生成文档的业务类
 * @author frank
 *
 */
public class DocumentRenderer {
	public static List<Map<String, Object>> getDocument(Class<? extends BaseModel> clz, String tablename) {
		Map<String, FieldMetaInfo> colsInfo = DocumentRenderer.getColumnsMetaInfo(tablename);
		
		// 注解+反射读取 pojo 信息
		List<Map<String, Object>> meta = new ArrayList<>();
		
		List<Field> fields = Reflect.getDeclaredField(clz);
		if(fields.size() > 0){
			for (Field field : fields) {
				if("service".equals(field.getName())){
					continue;
				}
				Map<String, Object> map = new HashMap<>();
				
				map.put("name", field.getName());
				map.put("returnType", field.getType().getCanonicalName());
				map.put("dbMetaInfo", colsInfo.get(field.getName()));
				
				FieldDescription fd = field.getAnnotation(FieldDescription.class);
				if (fd != null) {
					map.put("description", fd.doc());
				}
				
				Size size = field.getAnnotation(Size.class);
				if (size != null) {
					map.put("size", size.min() + "-" + size.max());
				}
				
				NotNull notNull = field.getAnnotation(NotNull.class);
				map.put("nullable", notNull != null ? true: false);
				
				Pattern pattern = field.getAnnotation(Pattern.class);
				map.put("pattern", pattern != null ? pattern.regexp() : "");
				
				meta.add(map);
			}
		}

		return meta;
	}
	
	/**
	 * 读取数据库字段的元信息
	 * 
	 * @param tablename
	 *            表名
	 * @return
	 */
	private static Map<String, FieldMetaInfo> getColumnsMetaInfo(String tablename) {
		SqlSession session = App.sqlSessionFactory.openSession();
		Map<String, FieldMetaInfo> cols = new HashMap<>();
		
		try{
			DatabaseMetaData md = session.getConnection().getMetaData();
			ResultSet col = md.getColumns(null, null, tablename, null);
			
			while (col.next()) {
				FieldMetaInfo field = new FieldMetaInfo();
				field.setName(col.getString("COLUMN_NAME"));
				field.setTypeName(col.getString("TYPE_NAME"));
				field.setSize(col.getString("COLUMN_SIZE"));
				field.setNullable("1".equals(col.getString("NULLABLE")));// 0||1,1允许空值,0不允许空值
				
				cols.put(field.getName(), field);
			}
			
			col.close();
        } catch (SQLException e) {
			e.printStackTrace();
		}
		
		session.close();
		
		return cols;
	}

	public static String[] getEntityInfo(Class<? extends BaseModel> clazz) {
		EntityDescription annotation = clazz.getAnnotation(EntityDescription.class);
		String[] strs = new String[2];
		
		if(annotation != null) {
			strs[0] = annotation.doc();
			strs[1] = "".equals(annotation.extraHTML_path()) ? null : annotation.extraHTML_path();
		}
		
		return strs;
	}
}
