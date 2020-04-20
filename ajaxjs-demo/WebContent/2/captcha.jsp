<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>图片验证码</title>
<link rel="stylesheet" type="text/css" href="../common/main.css" />
</head>
<body>
	<fieldset>
		<legend>图片验证码演示 </legend>
		<form action="../../FileUpload" enctype="multipart/form-data" method="POST">
			名  &nbsp;&nbsp;  称：<input type="text" name="username" size="20" class="ajaxjs-input" placeholder="请输入名称" required="required" />
			<br />
			<br />
			验证码：<input type="text" name="your-email" size="20" class="ajaxjs-input" placeholder="请输入右侧验证码" required="required" />
			<img src="../Captcha?d=888" style="cursor: pointer;" onclick="this.src=this.src.replace(/d=\d+/, 'd=' + new Date().valueOf());" />
			<br />
			<br />
			<span style="font-size:9pt; color:gray">点击验证码图片刷新</span>
			<br />
			<br />
			<br />
			<center>
				<button class="ajaxjs-btn">提交</button>
			</center>
		</form>
	</fieldset>
		
 	<%@include file="../../WEB-INF/jsp/footer.jsp"%>
</body>
</html>