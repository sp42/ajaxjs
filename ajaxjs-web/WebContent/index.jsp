<%@page pageEncoding="UTF-8" import="java.util.*"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>

<!DOCTYPE html>
<html>
<commonTag:head lessFile="/asset/less/user.less" title="首页">
	<style>
		html, body, table {
			height: 100%;
		}
	</style>
</commonTag:head>
<body>
	<jstl:if test="true">
			Nice Day！
		</jstl:if>
	Hello,
<!-- 	<c:if test="true">World！</c:if> -->

	<br />
	
	<img style="width:60%;max-width: 400px;margin: 0 auto;margin-top:50px;display: block;" src="asset/images/under_construction.jpg" />
	<img src="service.jsp?action=under_construction" />
</body>
</html>