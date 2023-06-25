<%@page import="com.ajaxjs.util.WebHelper"%>
<%@ page pageEncoding="UTF-8" import="java.util.*, com.ajaxjs.net.http.Get, com.ajaxjs.util.JspBack, com.ajaxjs.sql.JdbcHelper" %>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="/ajaxjs" %>
<%
	JspBack.getInfo(request, "api");
	JspBack.getList(request, "/data_service/app/list", "appList");
%>
<myTag:info namespace="api" namespace_chs="API接口" date_style="3" field_style="15">
	<tr>
		<td width="150">API 请求相对地址</td>
		<td><input type="text"  name="url" value="${info.url}" placeholder="ANT 风格路径表达式" /></td>
	</tr>
	<tr>
		<td width="150">API接口类型</td>
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