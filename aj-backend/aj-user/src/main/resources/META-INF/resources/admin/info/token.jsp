<%@ page pageEncoding="UTF-8" import="java.sql.Connection, com.ajaxjs.user.admin.jsp.JspHelper" %>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="/ajaxjs" %>
<%
	String sql = "SELECT t.*, u.username AS userName, c.name AS clientName, z.name AS tenantName FROM auth_access_token t "+
	"LEFT JOIN user u ON t.userId = u.id " +
	"LEFT JOIN sys_tenant z ON u.tenantId = z.id " +  
	"LEFT JOIN auth_client_details c ON t.Id = c.id ";
	JspHelper.parepreInfoSql(request, sql, "t.id");
	
	Connection conn = JspHelper.init(request);
%>
<myTag:info namespace="token" namespace_chs="Token" date_style="1" field_style="1">
	<tr>
		<td>AccessToken</td>
		<td>${info.accessToken}</td>
	</tr>
	<tr>
		<td>租户</td>
		<td>${info.tenantName}</td>
	</tr>
	<tr>
		<td>用户</td>
		<td>${info.userName}#${info.userId}</td>
	</tr>
	<tr>
		<td>权限范围</td>
		<td>${info.scope}</td>
	</tr>
	<tr>
		<td>有效期至</td>
		<td>
			<script> 
			;(function(){  
				document.write(new Date(new Date().getTime() + ${info.expiresIn}).format('yyyy-MM-dd hh:mm'));
			})();
			</script>
		</td>
	</tr> 
</myTag:info>

<%
	conn.close();
%>