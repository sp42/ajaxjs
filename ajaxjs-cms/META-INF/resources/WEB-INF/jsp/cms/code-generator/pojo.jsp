<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*, com.ajaxjs.cms.utils.codegenerators.Utils"%><%
	String beanName = request.getAttribute("beanName").toString();
	List<Map<String, String>> fields = (List<Map<String, String>>)request.getAttribute("fields");
%>package ${packageName}.model;

import com.ajaxjs.framework.BaseModel;

public class <%=Utils.firstLetterUpper(beanName)%> extends BaseModel  {
	private static final long serialVersionUID = 1L;
	<%
		for (Map<String, String> i : fields) {
			String name = i.get("name");
			if("content".equals(name) || "name".equals(name) || "createDate".equals(name) 
					|| "updateDate".equals(name) || "id".equals(name) || "uid".equals(name))
				continue;
			
			request.setAttribute("info", i);
			request.setAttribute("name", Utils.getName(i.get("comment").toString()));
			request.setAttribute("type", Utils.toJavaType(i.get("type").toString()));
			request.setAttribute("firstLetterUpperName", Utils.firstLetterUpper(i.get("name").toString()));
				
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