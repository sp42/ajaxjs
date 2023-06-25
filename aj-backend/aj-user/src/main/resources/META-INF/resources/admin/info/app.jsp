<%@ page pageEncoding="UTF-8" import="com.ajaxjs.util.JspBack" %>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="/ajaxjs" %>
<%
	JspBack.getInfo(request, "app");
	JspBack.getList(request, "/data_service/system/list", "sys_list");
%>
<myTag:info namespace="app" namespace_chs="应用" date_style="3" field_style="15">
	<tr>
		<td width="150">客户端 id</td>
		<td><input type="text"  name="clientId" value="${info.clientId}" style="width:65%"  /><button onclick="genId();return false;">生成 Id</button></td>
	</tr>
	<tr>
		<td width="150">客户端密钥</td>
		<td><input type="text"  name="clientSecret" value="${info.clientSecret}" style="width:65%" /><button onclick="genPsw();return false;">生成密钥</button> <br /><span
			class="note">请注意保密客户端密钥</span></td>
	</tr>
	<tr>
		<td width="150">应用类型</td>
		<td>
			<label>
				<input type="radio" name="type" value="HTML" ${info.type == 'HTML' ? 'checked' : ''} /> HTML
			</label>
			<label>
				<input type="radio" name="type" value="REST_API" ${info.type == 'REST_API' ? 'checked' : ''}/> REST API
			</label>
			<label>
				<input type="radio" name="type" value="RPC_SERVICE" ${info.type == 'RPC_SERVICE' ? 'checked' : ''}/> RPC SERVICE
			</label>
			<label>
				<input type="radio" name="type" value="MISC" ${info.type == 'MISC' ? 'checked' : ''}/> 其他
			</label>
		</td>
	</tr> 
	<tr>
		<td width="150">所属系统</td>
		<td>
			<select style="width:300px;" name="sysId">
				<c:foreach items="${sys_list}">
					<option value="${item.id}" ${item.id == info.sysId ? 'selected' : ''}>${item.name}</option>				
				</c:foreach>
			</select>
		</td>
	</tr>
	<script>
		var str = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'.split('');
		
		function generateMixed(n) { 
		     var res = ""; 
		     for(var i = 0; i < n ; i ++) {
		         var id = Math.ceil(Math.random()*48);
		         res += str[id];
		     }
		     
		     return res;
		}
		
		function genId() {
			document.querySelector('input[name="clientId"]').value = generateMixed(15);
		}
		
		function genPsw() {
			document.querySelector('input[name="clientSecret"]').value = generateMixed(26);
		}
	</script>
</myTag:info>