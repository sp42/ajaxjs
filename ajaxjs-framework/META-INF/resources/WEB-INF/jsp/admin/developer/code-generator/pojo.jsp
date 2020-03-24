<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*, com.ajaxjs.util.ReflectUtil"%><%
	String beanName = request.getAttribute("beanName").toString();
	List<Map<String, String>> fields = (List<Map<String, String>>)request.getAttribute("fields");
%><%!
	public static String getName(String sqlType) {
		String[] arr = sqlType.split("，|,|\\.|。");

		return arr[0];
	}

	public static String toJavaType(String sqlType) {
		String t = "void";

		if (sqlType.indexOf("varchar") != -1 || sqlType.indexOf("char") != -1 || sqlType.indexOf("text") != -1)
			t = "String";
		else if (sqlType.indexOf("datetime") != -1)
			t = "java.util.Date";
		else if (sqlType.indexOf("bigint") != -1)
			t = "Long";
		else if (sqlType.indexOf("int") != -1 || sqlType.indexOf("date") != -1)
			t = "Integer";
		else if (sqlType.indexOf("float") != -1)
			t = "Float";
		else if (sqlType.indexOf("double") != -1)
			t = "Double";
		else if (sqlType.indexOf("decimal") != -1)
			t = "java.math.BigDecimal";

		return t;
	}
%>package ${packageName}.model;

import com.ajaxjs.framework.BaseModel;

/**
 * ${tablesComment}
 */
public class <%=ReflectUtil.firstLetterUpper(beanName)%> extends BaseModel  {
	private static final long serialVersionUID = 1L;
	<%
		for (Map<String, String> i : fields) {
			String name = i.get("name");
			if("content".equals(name) || "name".equals(name) || "createDate".equals(name) || "stat".equals(name) 
					|| "updateDate".equals(name) || "id".equals(name) || "uid".equals(name))
				continue;
			
			request.setAttribute("info", i);
			request.setAttribute("name", getName(i.get("comment").toString()));
			request.setAttribute("type", toJavaType(i.get("type").toString()));
			request.setAttribute("firstLetterUpperName", ReflectUtil.firstLetterUpper(i.get("name").toString()));
				
	%>
	/**
	 * ${name}
	 */
	private ${type}${' '}${info.name};
	
	/**
	 * 设置${name}
	 
	 * @param ${info.name}  
	 */
	public void set${firstLetterUpperName}(${type}${' '}${info.name}) {
		this.${info.name} = ${info.name};
	}
	
	/**
	 * 获取${name}
	 
	 * @return ${name}
	 */	
	public ${type} get${firstLetterUpperName}() {
		return ${info.name};
	}
	<%
		}
	%>
}