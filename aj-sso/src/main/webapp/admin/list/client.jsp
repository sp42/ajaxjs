<%@ page pageEncoding="UTF-8" import="com.ajaxjs.util.JspHelper"%>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="/ajaxjs"%>
<%
	String sql = "SELECT c.*, t.name AS tenantName FROM auth_client_details c LEFT JOIN sys_tenant t ON c.tenantId = t.id";
	JspHelper.parepreListSql(request, sql, "client", "客户端");
%>
<myTag:list namespace="${namespace}" namespace_chs="${namespace_chs}">
	<table class="aj-table">
		<thead>
			<tr>
				<th>#</th>
				<th>客户端 id</th>
				<th style="min-width: 200px;">客户端名称</th>
				<th>租户</th>
				<th>状态</th>
				<th>创建日期</th>
				<th>修改日期</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:foreach items="${list}" var="item">
				<tr>
					<td>${item.id}</td>
					<td>${item.clientId}</td>
					<td>${item.name}</td>
					<td>${item.tenantName}</td>
					<myTag:list-common-rol style="31" item="${item}" namespace="${namespace}" namespace_chs="${namespace_chs}" />
				</tr>
			</c:foreach>
		</tbody>
	</table>
</myTag:list>