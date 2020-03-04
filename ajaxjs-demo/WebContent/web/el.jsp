<%@page pageEncoding="UTF-8" %>
<%@page trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title><%="欢迎！"%></title>
    <style> body {margin: 5%;}</style>
</head>
<body>
	<p> 下面是调用的例子。</p>
	<p> // 判断 a 参数是否为 1。如果是返回 current 字符串，表示 1 当前选中，否则输出空字符串 </p>
	${"1" == param.a ? "Current" : "Not current"}
	<br/>
	${empty param.a}     // 判断 a 是否空
	<br/>
	${not empty param.a} // 判断 a 是否非空
	<p>表达式语言内置了对页面对象的快速访问，最常见的是param/paramValues，表示获取request请求的参数或者表单参数（读取GET/POST请求提交的参数），例如：</p>
	<p>// 假设 URL 连接是 test.jsp?a=1&b=2&c=3</p>
	${param.a} // 返回 1 字符串，等同于 <%=request.getParameter("a")%>
	<p>
	// 获取表单值，例如提交的表单中含有 &lt;input type="text" name="name" /&gt;
	</p>
	${param.name}
</body>
</html>