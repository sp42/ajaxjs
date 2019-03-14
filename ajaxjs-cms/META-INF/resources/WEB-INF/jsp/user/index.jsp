<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<head>
		<jsp:include page="/jsp/common/head.jsp"></jsp:include>
	</head>
</head>
<body>
	<%@include file="/WEB-INF/jsp/nav.jsp" %>
	<div class="center" style="text-align:center;">
		<h3 class="jb">信息</h3>
		
		
		<div>${empty param.msg ? '<script>location.href="center";</script>' : param.msg}</div>
		
		<br />
		<br />
		<br />
		<br />
		
	</div>
	<%@include file="/WEB-INF/jsp/footer.jsp" %>
</body>
</html>