<%@ page pageEncoding="UTF-8" import="java.sql.Connection, com.ajaxjs.util.JspHelper, com.ajaxjs.sql.JdbcHelper"%>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="/ajaxjs"%>
<%
	String sql = "SELECT u.* FROM user u ";
	JspHelper.parepreInfoSql(request, sql, "u.id");
	
	Connection conn = JspHelper.init(request);
	request.setAttribute("TENANT_LIST", JdbcHelper.queryAsMapList(conn, "SELECT * FROM sys_tenant"));
	request.setAttribute("TABLE_NAME", "user");
%>
<myTag:info namespace="user" namespace_chs="用户" date_style="3" field_style="15" two_cols="true">
	<tr>
		<td>昵称</td>
		<td><input type="text" name="name" value="${info.name}" /></td>
		<td>真实姓名</td>
		<td><input type="text" name="realname" value="${info.realname}" /></td>
	</tr>
	<tr>
		<td>性别</td>
		<td>
			<label><input type="radio" name="gender" value="1" ${info.gender == 1 ? 'checked' : ''} /> 男</label> 
			<label><input type="radio" name="gender" value="2" ${info.gender == 2 ? 'checked' : ''} /> 女</label> 
			<label><input type="radio" name="gender" value="0" ${info.gender == 0 ? 'checked' : ''} /> 未知</label>
		</td>
		<td>生日</td>
		<td><input type="text" name="phone" value="${info.birthday}" /></td>
	</tr>
	<tr>
		<td>电话</td>
		<td><input type="text" name="phone" value="${info.phone}" /></td>
		<td>Email</td>
		<td><input type="text" name="email" value="${info.email}" /></td>
	</tr>
	<tr> 
		<td>地区</td>
		<td colspan="3"><input type="text" name="location" value="${info.location}" /></td>
	</tr>
	<tr>
		<td>租户</td>
		<td>
			<select style="width:180px;margin:10px 0;">  
				<c:foreach items="${TENANT_LIST}">
					<option value="${item.id}" ${item.id == info.id ? 'selected' : ''}>${item.name}</option>				
				</c:foreach>
			</select>
		</td>
		<td>组织</td>
		<td></td>
	</tr>

</myTag:info>

<%
conn.close();
%>