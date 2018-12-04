<%@tag pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<%@attribute name="left" required="true" fragment="true" description="插入左侧菜单内容"%>
<%@attribute name="body" required="true" fragment="true"description="插入正文内容"%>
<%@attribute name="banner" required="true" type="String" description="Banner 图片"%>
<%@attribute name="bodyClass" required="false" type="String" description="body 标签样式"%>
<!DOCTYPE html>
<html>
<head>
	<%@include file="/jsp/common/head.jsp"%>
</head>
<body class="${bodyClass}">
	<div class="top">
		<commonTag:page type="navMenu2" />
	</div>

	<div class="centerWidth">
		<img src="${banner}" width="100%" style="margin-top: 5px;" />
		<commonTag:page type="anchor" />
		<h2>${PAGE.node.name}</h2>

		<div class="body">
			<div class="left">
				<jsp:invoke fragment="left" />
			</div>
			<div class="right">
				<jsp:invoke fragment="body" />
			</div>
		</div>
	</div>
	<div class="bottom">
	</div>
</body>
</html>