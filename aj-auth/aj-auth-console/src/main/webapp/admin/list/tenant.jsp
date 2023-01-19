<%@ page pageEncoding="UTF-8" import="com.ajaxjs.util.JspHelper"%>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="/ajaxjs"%>
<%
	String sql = "SELECT * FROM sys_tenant WHERE 1=1";

if (request.getParameter("keyword") != null) 
	sql = sql.replace("1=1", "1=1 AND name LIKE '%" + JspHelper.safeGet(request, "keyword") + "%'");

	JspHelper.parepreListSql(request, sql, "tenant", "租户");
%>
<myTag:list namespace="${namespace}" namespace_chs="${namespace_chs}">
	<table class="aj-table even">
		<thead>
			<tr>
				<th>#</th>
				<th style="min-width: 200px;">${namespace_chs}名称</th>
				<th>联系人</th>
				<th>联系方式</th>
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
					<td>${item.contact}</td>
					<td>${item.contacts}</td>
					<myTag:list-common-rol style="31" item="${item}" namespace="${namespace}" namespace_chs="${namespace_chs}" />
				</tr>
			</c:foreach>
		</tbody>
	</table>
</myTag:list>