<%@page pageEncoding="UTF-8" isErrorPage="true"%>
<%-- <%@ include file="/WEB-INF/jsp/common/ClassicJSP/util.jsp"%> --%>
<!DOCTYPE html>
<html>
<head>
	<title>错误页面 code：${requestScope['javax.servlet.error.status_code']}</title>
	<style>
		body {
			max-width: 600px;
			min-width: 320px;
			margin: 0 auto;
			padding-top: 2%;
		}
		
		textarea {
			width: 100%;
			min-height: 300px;
			outline:none;
			border:1px solid gray;
			padding:1%;
		}
		
		h1 {
			text-align: right;
			color: lightgray;
		}
		
		div {
			margin-top: 1%;
		}
	</style>
</head>
<body>
	<h1>抱 歉！</h1>
	<div style="padding:2% 0;text-indent:2em;">尊敬的用户：我们致力于提供更好的服务，但人算不如天算，有些错误发生了，希望是在控制的范围内……如果问题重复出现，请向系统管理员反馈。</div>
	<textarea><%
	System.out.println("ssssssssss");
           %></textarea>
	<div>
		<center>
			<a href="${pageContext.request.contextPath}">回首页</a> | <a href="javascript:history.go(-1);">上一页</a>
		</center>
	</div>
</body>
</html>