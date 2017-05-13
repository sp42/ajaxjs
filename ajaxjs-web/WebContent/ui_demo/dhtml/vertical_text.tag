<!DOCTYPE HTML>
<html lang="en-US">
<head>
<meta charset="UTF-8">
<title>文字翻转90度</title>
<style type="text/css">
/*reset*/
body {
	margin: 0;
	font: 12px/1.5 '\5b8b\4f53', sans-serif;
	color: #333;
	background: #fff;
}

h1,h2,h3,h4,h5,p,ul,ol,dl,dd {
	margin: 0;
}

button {
	padding: 0;
}

ul,ol {
	padding-left: 0;
	list-style-type: none;
}

a {
	text-decoration: none;
	color: #333;
}

a img {
	border: 0;
}

.test {
	margin: 50px 0 0 100px;
	width: 22px;
	padding-top: 10px;
	height: 100px;
	border: red solid 1px;
	text-align: center;
	line-height: 1.2
}

.money {
	-webkit-transform: rotate(90deg);
	-moz-transform: rotate(90deg);
	-o-transform: rotate(90deg);
	-ms-transform: rotate(90deg);
	transform: rotate(90deg);
	line-height: 22px;
	display: block;
}
</style>
<!--[if lte IE 8]>
    <style>
      .money{writing-mode: tb-rl;text-align:left;}
    </style>
   <![endif]-->
</head>
<body>
	<div class="test">
		测试<span class="money">500QB</span>
	</div>
</body>
</html>