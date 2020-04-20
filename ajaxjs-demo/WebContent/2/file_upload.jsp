<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>文件上传演示</title>
	<link rel="stylesheet" type="text/css" href="../common/main.css" />
</head>
<body>
<form action="../FileUpload/" enctype="multipart/form-data" method="POST">
	<fieldset>
		<legend>
			文件上传演示
		</legend>
			请选择文件: <input type="file" name="myfile" /><br /> <br />
			<button class="ajaxjs-btn">上传文件</button>
	</fieldset>
	
	<footer>
		Powered by <a href="https://framework.ajaxjs.com/framework/" target="_blank">AJAXJS Framework</a>.
	</footer>
	
</form>
</body>
</html>