<%@ page pageEncoding="UTF-8" import="com.ajaxjs.util.JspBack, com.ajaxjs.user.admin.jsp.JspHelper"%>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="/ajaxjs"%>
<%
/* String sql = "SELECT c.*, t.name AS tenantName FROM auth_client_details c LEFT JOIN sys_tenant t ON c.tenantId = t.id WHERE 1=1";

if (request.getParameter("tenantId") != null) 
	sql = sql.replace("1=1", "1=1 AND c.tenantId = " + JspHelper.safeGet(request, "tenantId"));

if (request.getParameter("keyword") != null) {
	String k = JspHelper.safeGet(request, "keyword");
	sql = sql.replace("1=1", "1=1 AND (c.name LIKE '%" + k + "%' OR c.clientId LIKE '%" + k + "%')");
}

JspHelper.parepreListSql(request, sql, "client", "应用"); */
	JspHelper.getJspHelper(request);
	JspBack.list(request, "app", "应用");
%>
<myTag:list namespace="${namespace}" namespace_chs="${namespace_chs}">
	<script>
		tenantFilter();
	</script>
	
	<table class="aj-table even">
		<thead>
			<tr>
				<th>#</th>
				<th style="min-width: 200px;">${namespace_chs}名称</th>
				<th>${namespace_chs} id</th>
				<th>${namespace_chs}类型</th>
				<th>所属系统</th>
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
					<td>${item.name}</td>
					<td>${item.clientId}</td>
					<td>${item.type}</td>
					<td>${item.sysName}</td>
					<myTag:list-common-rol style="31" item="${item}" namespace="${namespace}" namespace_chs="${namespace_chs}" />
				</tr>
			</c:foreach>
		</tbody>
	</table>
</myTag:list>