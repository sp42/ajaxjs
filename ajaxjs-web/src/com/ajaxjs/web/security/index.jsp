<%@page import="com.ajaxjs.web.security.wrapper.CookieRequest"%>
<%!public String getCookieByName(String name, Cookie[] cookies) {
		for (Cookie cookie : cookies) {
			if (name.equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}%>
<%
	// CookieRequest cr = (CookieRequest)request;
%>
<%
	//=getCookieByName("name", request.getCookies())
%>

<%
	//xss params filter  
	//url:  
	System.out.println(request.getParameter("xssparam")); //output:  
	System.out.println(request.getParameterMap().toString());

	//cookie white list output filter  
	System.out.println(request.getCookies().toString());
	response.addCookie(new Cookie("name", "valName"));//valid  
	response.addCookie(new Cookie("clrf", "valName\r\n<script>"));//valid  
	try {
		response.addCookie(new Cookie("invalidName", "invalidvalName"));//not valid, throw runtimeexception  
	} catch (Exception e) {
		e.printStackTrace();
	}

	//cookie maxsize filter  
	response.addCookie(new Cookie("id", java.nio.ByteBuffer.allocate(4 * 1024 + 2).toString()));//valid  

	//head security filter  
	response.setHeader("aaa\r\nbbb", "ccc\r\\ddd\n");

	//session store to cookie  
	System.out.println(request.getSession().getAttribute("sescookie"));
	request.getSession().setAttribute("sescookie", "sessioncookiestoretest");

	//rediction filter  
	//response.sendRedirect("http://www.163.com");//failed  

	//status filter  
	response.setStatus(404, "<script>alert(1)</script>");
%>