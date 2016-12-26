<%@page pageEncoding="UTF-8" import="sun.misc.BASE64Decoder, java.io.*"%>
<%@include file="functions.jsp"%>
<%
	if (request.getMethod().equalsIgnoreCase("POST")) {
		String contentBody = request.getParameter("contentBody"), path = Mappath("page");
		save_jsp_fileContent(path, contentBody);
		out.println("<script>alert('修改成功！');window.location = document.referrer;</script>");
	} else {
		out.println("method error");
	}
%>