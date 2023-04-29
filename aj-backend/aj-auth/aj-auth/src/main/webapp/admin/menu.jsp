<%@ page pageEncoding="UTF-8" import="java.util.*,com.ajaxjs.util.JspBack"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<%@include file="/common/head.jsp"%>
<style>
html, body {
	height: 100%;
	overflow: hidden;
}

body {
	border-right: 1px solid lightgray;
	background: url(../images/user.png) top left no-repeat;
	background-size: 90px;
}

h1 {
	text-align: center;
	margin: 50px 0;
	padding-left: 50px;
	font-weight: bold;
}

ul {
	padding: 2%;
	width: 100%;
	font-size: 10pt;
}

li {
	padding: 6% 0;
	border-top: 1px solid lightgray;
	text-align: center;
}

li:first-child {
	border-top: 0;
}

li a {
	display: block;
}

li.inline a {
	display: inline-block;
}

.copyright {
	position: fixed;
	bottom: 2%;
	left: 15%;
	font-size: 9pt;
	color: gray;
}

.copyright a {
	text-decoration: underline;
}
</style>
</head>
<body>
	<h1>Auth 管理中心</h1>
	<ul>
	<%
		JspBack.getList(request, "/data_service/tenant/list", "tenant");
		List<Map<String, Object>> list = (List<Map<String, Object>>)request.getAttribute("tenant");
	%>
		<li class="inline">切换租户： 
			<select class="tenantSelect" onchange="changeTenant(this)">
				<option value="">不分租户</option>
	 		<%
	 		if(list!=null)
	 		for (Map<String, Object> map : list) {%>
				<option value="<%=map.get("id")%>"><%=map.get("name")%></option>
			<%}%> 
			</select> 
			<br /> <br /> <a target="center" href="main.jsp">首页</a> | <a href="javascript:logout();">登出</a>
		</li>
		<li class="inline"><a target="center" href="list/user.jsp">用户</a> | <a target="center" href="list/org.jsp">组织</a> | <a target="center" href="list/tenant.jsp">租户</a></li>
		<li class="inline"><a target="center" href="list/role.jsp">角色</a> | <a target="center" href="list/function.jsp">功能</a> | <a target="center" href="list/api.jsp">API</a></li>
		<li class="inline"><a target="center" href="list/system.jsp">系统</a> | <a target="center" href="list/client.jsp">应用</a></li>
		<li><a target="center" href="list/token.jsp">Token 管理</a></li>
		<li><a target="center" href="list/token.jsp">用户登录日志</a></li>
	</ul>

	<script>
		setTimeout(()=>{			
			// 显示选中项
			var tenantId = localStorage.getItem("tenantId");
			
			if (tenantId != null) {
				var option = document.querySelector('.tenantSelect option[value="' + tenantId + '"]');
			
				if (option)
					option.setAttribute('selected', true);
			}
		}, 500);
		
		function changeTenant(selectEl) {
			var tenantId = selectEl.options[selectEl.selectedIndex].value;
			
			if (tenantId) 
				localStorage.setItem("tenantId", tenantId);// 存储
			else 
				window.localStorage.removeItem('tenantId');				
			
			window.parent.frames.center.location.reload();
		}
	</script>

	<div class="copyright">
		©2022 隶属于 <a href="https://gitee.com/sp42_admin/ajaxjs" target="_blank">AJAXJS</a>
		框架
	</div>
	<script>
		function logout(){
			if(confirm('确定退出吗？')) {
				alert('成功退出');
			}
		}
	</script>
</body>
</html>