<%@page pageEncoding="UTF-8" import="com.demo.web.Student"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>Insert title here</title>
	</head>
	<body>
		<%=((Student)request.getAttribute("student")).getName()%>
	</body>
</html>