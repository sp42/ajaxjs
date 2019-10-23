<%@page pageEncoding="UTF-8" import="com.ajaxjs.util.logger.LogHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Insert title here</title>
</head>
<body>
	Hi
<%
	LogHelper LOGGER = LogHelper.getLog(this.getClass());
	LOGGER.info("Hi");
	LOGGER.warning("Foo");
%>
</body>
</html>