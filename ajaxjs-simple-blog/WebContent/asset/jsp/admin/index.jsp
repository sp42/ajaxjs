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
                 <li>
                     <h3>文章管理</h3>
                     <ul>
                         <li>
                             <a target="iframepage" href="Article/">文章列表</a>
                         </li>
                         <li>
                             <a target="iframepage" href="Article/create">新建文章</a>
                         </li>
                     </ul>
                 </li>
                 <li class="news">
           
                     <h3>分类管理</h3>
                     <ul>
                         <li>
                             <a target="iframepage" href="../service/video/edit/list">分类管理</a>
                         </li>
                     </ul>
                 </li>

             </ul>
		</section>
		<section class="iframe">
			<iframe src="?action=workbench" name="iframepage"></iframe>
		</section>
	    <script src="asset/js/widget/acMenu.js"></script>
	    <script src="asset/js/admin.js"></script>
	</body>
</html>