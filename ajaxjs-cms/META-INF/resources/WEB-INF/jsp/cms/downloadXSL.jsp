<%@ page import="java.util.*, java.io.*" pageEncoding="utf-8" contentType="application/msexcel"%>
<%
	//response.setHeader("Content-disposition","inline; filename=videos.xls");  
	response.setHeader("Content-disposition", "attachment; filename=test.xls");
	//以上这行设定传送到前端浏览器时的档名为test.xls  
	//就是靠这一行，让前端浏览器以为接收到一个excel档
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>">
	<title>spring jdbc test</title>
	<meta charset="utf-8" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
</head>
<body>
	<br>
	<table border="1" width="100%">
		<tr>
			<td>id</td>
			<td>name</td>
		</tr>
		<tr>
			<td>440105198309060315</td>
			<td>你好 我是中国人</td>
		</tr>
	</table>
</body>
</html>
