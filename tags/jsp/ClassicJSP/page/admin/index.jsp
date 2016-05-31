<%@page pageEncoding="UTF-8"%>
<%
	final String userid = "admin", pwd = "123123a";
%>
<%@include file="functions.jsp"%>
<%
	if (!checkAuth(request.getHeader("Authorization"), userid, pwd)) {
		// 如果认证失败,则要求认证 ，不能输入中文
		String msg = "\"Please input your account\"";

		// 发送状态码 401, 不能使用 sendError，坑
		response.setCharacterEncoding("utf-8");
		response.setStatus(401, "Authentication Required");
		// 发送要求输入认证信息,则浏览器会弹出输入框
		response.setHeader("WWW-Authenticate", "Basic realm=" + msg);
%>
<html>
<body>
	非法登录！
</body>
</html>
<%
	return;
	}
%>
<html>
<head>
	<title>欢迎 <%=userid%> 登录</title>
	<link href="style.css" rel="stylesheet" type="text/css" />
	<script>
		function onSubmit(el){
			document.querySelector('input[name=contentBody]').value = htmlEditor.getValue();
			//debugger;
		}
	</script>
</head>
<body>
	<%=userid%>
<%
	request.setAttribute("contentBody", read_jsp_fileContent(getFullPathByRequestUrl(Mappath("page"))));
%>
	<%@include file="htmlEditor.jsp"%>
	<form method="POST" action="action.jsp" onSubmit="onSubmit(this);">
		<input type="hidden" name="contentBody" />
		<div style="padding:1%;text-align: center;">
			<a href="../" target="_blank">浏览页面</a>
			<button>保存</button>
		</div>
	</form>
</body>
</html>