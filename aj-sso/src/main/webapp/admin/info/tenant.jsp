<%@ page pageEncoding="UTF-8" import="java.sql.Connection, com.ajaxjs.util.JspHelper" %>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="/ajaxjs" %>
<%
	String sql = "SELECT * FROM sys_tenant";
	JspHelper.parepreInfoSql(request, sql);

	Connection conn = JspHelper.init(request);
%>
<myTag:info namespace="tenant" namespace_chs="租户" date_style="3" field_style="15">
	<tr>
		<td width="150">联系人</td>
		<td><input type="text" value="${info.contact}" /></td>
	</tr>
	<tr>
		<td width="150">联系方式</td>
		<td><input type="text" value="${info.contacts}" /></td>
	</tr>
</myTag:info>

<%
	conn.close();
%>