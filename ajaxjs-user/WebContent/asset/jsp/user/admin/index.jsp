<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<html>
	<commonTag:head lessFile="/asset/less/admin.less" title="管理" />
	<body class="admin-shell">
		<header>
			<h1>我的控制面板</h1>
			<menu>
				${userName} 已登录 | <a href="${pageContext.request.contextPath}/" target="_blank">首页</a> | <a href="?action=logout">退出</a> 
			</menu>
		</header>
		<section class="side">
			<div class="rightTop_title">Welcome</div>
			<div class="rightTop"></div>
			<div class="closeBtn" onclick="hideSider(this);"></div>
			<ul class="leftSidebar">
                 <%@include file="/WEB-INF/jsp/admin-menu.jsp" %>
             </ul>
		</section>
		<section class="iframe">
			<iframe src="?action=workbench" name="iframepage"></iframe>
		</section>
	    <script src="asset/js/widget/acMenu.js"></script>
	    <script src="asset/js/admin.js"></script>
	</body>
</html>