<%@ page pageEncoding="UTF-8" import="com.ajaxjs.user.admin.jsp.JspHelper, com.ajaxjs.util.JspBack, com.ajaxjs.user.UserConstant"%>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="/ajaxjs"%>
<%
/* 	String sql = "SELECT u.*, t.name AS tenantName FROM user u LEFT JOIN sys_tenant t ON u.tenantId = t.id";
	
	if (request.getParameter("tenantId") != null) 
		sql += " WHERE tenantId = " + request.getParameter("tenantId");
	
	JspHelper.parepreListSql(request, sql, "user", "用户"); */
	
	//request.setAttribute("SEX_GENDER", UserConstant.SEX_GENDER); 
	JspHelper.getJspHelper(request); 
	JspBack.list(request, "user", "用户");
%>

<myTag:list namespace="${namespace}" namespace_chs="${namespace_chs}" page="true">
	<script>
		tenantFilter();
	</script>
	
	<table class="aj-table even">
		<thead>
			<tr>
				<th>#</th>
				<th>登录名</th>
				<th>昵称</th>
				<th>性别</th>
				<th>手机</th>
				<th>邮件</th>
				<th>租户</th>
				<th>状态</th>
				<th>创建日期</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:foreach items="${PAGE_RESULT}" var="item">
				<tr>
					<td>${item.id}</td>
					<td>${item.username}</td>
					<td>${item.name}</td>
					<td>${SEX_GENDER[item.gender]}</td> 
					<td>${item.phone}</td>
					<td>${item.email}</td>
					<td>${item.tenantName}</td>
					<myTag:list-common-rol style="27" item="${item}" namespace="${namespace}" namespace_chs="${namespace_chs}" />
				</tr>
			</c:foreach>
		</tbody>
	</table>
</myTag:list>