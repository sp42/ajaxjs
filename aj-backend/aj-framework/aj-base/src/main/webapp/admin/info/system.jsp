<%@page import="com.ajaxjs.util.WebHelper"%>
<%@ page pageEncoding="UTF-8" import="java.util.*, com.ajaxjs.net.http.Get, com.ajaxjs.util.JspBack, com.ajaxjs.sql.JdbcHelper" %>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="/ajaxjs" %>
<%
	JspBack.getInfo(request, "system");
%>
<myTag:info namespace="system" namespace_chs="系统" date_style="3" field_style="15">
	<tr>
		<td width="150">开发厂商</td>
		<td><input type="text"  name="devFirm" value="${info.devFirm}" /></td>
	</tr>
	<tr>
		<td width="150">联系人及联系方式</td>
		<td><input type="text"  name="contact" value="${info.contact}" /> <br /> </td>
	</tr>
</myTag:info>