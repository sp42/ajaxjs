<%@ page pageEncoding="UTF-8" import="java.sql.Connection, com.ajaxjs.util.JspHelper" %>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="/ajaxjs" %>
<%
	String sql = "SELECT * FROM auth_client_details";
	JspHelper.parepreInfoSql(request, sql);
	
	Connection conn = JspHelper.init(request);
%>
<myTag:info namespace="client" namespace_chs="客户端" date_style="3" field_style="15">
	<tr>
		<td width="150">客户端 id</td>
		<td><input type="text" value="${info.clientId}" /></td>
	</tr>
	<tr>
		<td width="150">客户端密钥</td>
		<td><input type="text" value="${info.clientSecret}" /> <br /><span
			class="note">请注意保密</span></td>
	</tr>
	<tr>
		<td width="150">回调地址</td>
		<td><input type="text" value="${info.redirecUri}" /></td>
	</tr>
	<tr>
		<td width="150">租户</td>
		<td>
		
		</td>
	</tr>
</myTag:info>

<%
	conn.close();
%>