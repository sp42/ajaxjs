<%@ page pageEncoding="UTF-8" import="java.sql.Connection, com.ajaxjs.util.JspHelper, com.ajaxjs.sql.JdbcHelper" %>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="/ajaxjs" %>
<%
	String sql = "SELECT * FROM auth_client_details";
	JspHelper.parepreInfoSql(request, sql);
	Connection conn = JspHelper.init(request);
	
	request.setAttribute("TENANT_LIST", JdbcHelper.queryAsMapList(conn, "SELECT * FROM sys_tenant"));
	request.setAttribute("TABLE_NAME", "auth_client_details");
%>
<myTag:info namespace="client" namespace_chs="客户端" date_style="3" field_style="15">
	<tr>
		<td width="150">客户端 id</td>
		<td><input type="text"  name="clientId" value="${info.clientId}" /></td>
	</tr>
	<tr>
		<td width="150">客户端密钥</td>
		<td><input type="text"  name="clientSecret" value="${info.clientSecret}" /> <br /><span
			class="note">请注意保密客户端密钥</span></td>
	</tr>
	<tr>
		<td width="150">回调地址</td>
		<td><input type="text"  name="redirecUri" value="${info.redirecUri}" /></td>
	</tr>
	<tr>
		<td width="150">租户</td>
		<td>
			<select style="width:300px;" name="tenantId">
				<c:foreach items="${TENANT_LIST}">
					<option value="${item.id}" ${item.id == info.tenantId ? 'selected' : ''}>${item.name}</option>				
				</c:foreach>
			</select>
		</td>
	</tr>
</myTag:info>

<%
	conn.close();
%>