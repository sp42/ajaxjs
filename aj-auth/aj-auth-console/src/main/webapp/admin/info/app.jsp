<%@ page pageEncoding="UTF-8" import="com.ajaxjs.util.JspBack" %>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="/ajaxjs" %>
<%
	JspBack.getInfo(request, "app");

	String url = com.ajaxjs.util.WebHelper.getLocalService(request);
	java.util.Map <String, Object> info = com.ajaxjs.net.http.Get.api(url + "/data_service/system/list" );
	request.setAttribute("sys_list", info.get("data"));
%>
<myTag:info namespace="app" namespace_chs="应用" date_style="3" field_style="15">
	<tr>
		<td width="150">应用 id</td>
		<td><input type="text"  name="clientId" value="${info.clientId}" /></td>
	</tr>
	<tr>
		<td width="150">客户端密钥</td>
		<td><input type="text"  name="clientSecret" value="${info.clientSecret}" /> <br /><span
			class="note">请注意保密客户端密钥</span></td>
	</tr>
<%-- 	<tr>
		<td width="150">回调地址</td>
		<td><input type="text"  name="redirecUri" value="${info.redirecUri}" /></td>
	</tr> --%>
	<tr>
		<td width="150">租户</td>
		<td>
			<select style="width:300px;" name="sysId">
				<c:foreach items="${sys_list}">
					<option value="${item.id}" ${item.id == info.sysId ? 'selected' : ''}>${item.name}</option>				
				</c:foreach>
			</select>
		</td>
	</tr>
</myTag:info>