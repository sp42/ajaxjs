<%@page import="com.ajaxjs.util.WebHelper"%>
<%@ page pageEncoding="UTF-8" import="java.util.*, com.ajaxjs.net.http.Get, com.ajaxjs.util.JspBack, com.ajaxjs.sql.JdbcHelper" %>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="/ajaxjs" %>
<%
	JspBack.getInfo(request, "role");
	JspBack.getList(request, "/data_service/app/list", "appList");
%>
<myTag:info namespace="role" namespace_chs="角色" date_style="3" field_style="15">
	<tr>
		<td width="150">角色编码</td>
		<td><input type="text"  name="code" value="${info.code}" /></td>
	</tr>
	<tr>
		<td width="150">角色类型</td>
		<td><input type="text"  name="type" value="${info.type}" /> <br /> </td>
	</tr>
	<tr>
		<td width="150">所属应用</td>
		<td>
			<select style="width:300px;" name="appId">
				<c:foreach items="${appList}">
					<option value="${item.id}" ${item.id == info.appId ? 'selected' : ''}>${item.name}</option>				
				</c:foreach>
			</select>
		</td>
	</tr>
</myTag:info>