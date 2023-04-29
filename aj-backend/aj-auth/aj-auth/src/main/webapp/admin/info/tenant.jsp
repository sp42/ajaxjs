<%@page import="com.ajaxjs.util.WebHelper"%>
<%@ page pageEncoding="UTF-8" import="java.util.*, com.ajaxjs.net.http.Get, com.ajaxjs.util.JspBack, com.ajaxjs.sql.JdbcHelper" %>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="/ajaxjs" %>
<%
/* String sql = "SELECT * FROM sys_tenant";
JspHelper.parepreInfoSql(request, sql);

Connection conn = JspHelper.init(request);
request.setAttribute("TABLE_NAME", "sys_tenant"); */

	JspBack.getInfo(request, "tenant");
%>
<myTag:info namespace="tenant" namespace_chs="租户" date_style="3" field_style="15">
	<tr>
		<td width="150">租户编码</td>
		<td><input type="text"  name="code" value="${info.code}" /></td>
	</tr>
</myTag:info>