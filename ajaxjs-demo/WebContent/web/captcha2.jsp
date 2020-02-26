<%@page pageEncoding="UTF-8" import="com.ajaxjs.web.captcha.CaptchaController"%>
<!DOCTYPE html>
<html>
<head>
	<!-- 通用头部 -->
	<%@include file="/WEB-INF/jsp/head.jsp"%>
	<title>图片验证码-MVC</title>
	<link rel="stylesheet" type="text/css" href="../common/main.css" />
</head>
<body>
	<fieldset>
		<legend>图片验证码演示 </legend>
		<form action="../CheckCaptcha-MVC" method="POST">
			名  &nbsp;&nbsp;  称：<input type="text" name="username" size="20" class="ajaxjs-input" placeholder="请输入名称" required="required" />
			<br />
			<br />
			<aj-page-captcha field-name="${CaptchaController.CAPTCHA_CODE}"></aj-page-captcha>
			<br />
			<br />
			<span style="font-size:9pt; color:gray">点击验证码图片刷新</span>
			<br />
			<br />
			<br />
			
			<button class="ajaxjs-btn">提交</button>
		</form>
	</fieldset>
	
	<script>
		 new Vue({el:'form'});
	</script>
	<footer>
		Powered by <a href="https://framework.ajaxjs.com/framework/" target="_blank">AJAXJS Framework</a>.
	</footer>
</body>
</html>