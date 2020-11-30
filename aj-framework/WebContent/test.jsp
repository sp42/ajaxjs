<%@page import="com.ajaxjs.util.CommonUtil"%>
<%@page import="com.ajaxjs.net.http.NetUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	String respCode = request.getParameter("g-recaptcha-response");
	if(!CommonUtil.isEmptyString(respCode)) {
		String secret = "6LclfLMZAAAAAD6XcxnUpBL0qHKYWijay7-lGpOf";
		String params = String.format("secret=%s&response=%s", secret, respCode);
		
		String json = NetUtil.post("https://www.recaptcha.net/recaptcha/api/siteverify", params);
		System.out.println(json);
		
	}
%>
${param['g-recaptcha-response']}sds
	<form action="" method="post" id="demo-form">

		<label for="name">Name:</label> <input name="name" required><br />

		<label for="email">Email:</label> <input name="email" type="email"
			required><br />

		 <button class="g-recaptcha" data-sitekey="6LclfLMZAAAAAKC3YUTP4E3Ylc0PSvfnpneRePAH" 
        data-action='submit'>Submit</button>

	</form>
	 <script>
   function onSubmit(token) {
     document.getElementById("demo-form").submit();
   }
 </script>
<script src='https://www.recaptcha.net/recaptcha/api.js'></script>
</body>
</html>