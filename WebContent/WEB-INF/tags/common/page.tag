<%@tag pageEncoding="UTF-8" description="Page HTML"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<%@attribute name="type" type="String" required="true" description="指定哪种 HTML 片断"%>

<%-- 导航 --%>
<c:if test="${type == 'navMenu'}">
	<ul>
		<li><a href="${ctx}/">首页</a></li>
	<c:foreach items="${SITE_STRU.navBar}" var="menu">
		<li ${SITE_STRU.isCurrentNode(menu, pageContext.request) ? ' class="selected"' : ''}>
			<a href="${ctx}/${menu.id}/">${menu.name}</a>
		</li>
	</c:foreach>
	</ul>
</c:if>

<%-- 导航（封装版） --%>
<c:if test="${type == 'navMenu2'}">
<%@taglib prefix="commonTags" tagdir="/WEB-INF/tags/common"%>
<header>
	<div class="right">
		<commonTags:page type="search" />
	</div>
	<div class="right" style="margin: 1.5% 2% 0 0;">
		<a href="${ctx}/user/login.jsp">会员登录</a>
	</div>
	<nav>
		<commonTags:page type="navMenu" />
	</nav>
	<h1>
		<a href="${ctx}/">
			<img src="${ctx}/asset/images/logo.png" class="logoPic" />
			${_config.clientFullName}
		</a>
	</h1>
</header>
</c:if>

<style type="text/css">
	nav.top {
	    border-bottom: 1px solid white;
	}
	.imgBanner{
	    text-align:center;
	    width:100%;
	    background-color:#f4faf4;
	}
</style>

<%-- 二级菜单 --%>
<c:if test="${type == 'secondLevelMenu'}">
	<ul>
	<c:foreach items="${SITE_STRU.getMenu(pageContext.request)}" var="menu">
		<li ${SITE_STRU.isCurrentNode(menu, pageContext.request) ? ' class="selected"' : ''}>
			<a href="${ctx}${menu.fullPath}/">${menu.name}</a>
		</li>
	</c:foreach>
	</ul>
</c:if>

<%-- 次级菜单 --%>
<c:if test="${type == 'submenu'}">
	<ul>
	<c:foreach items="${PAGE.node.children}" var="menu">
		<li ${SITE_STRU.isCurrentNode(menu, pageContext.request) ? ' class="selected"' : ''}>
			<a href="${ctx}/${menu.fullPath}/">${menu.name}</a>
		</li>
	</c:foreach>
	</ul>
</c:if>

<%-- 定位，当没有 PAGE.node 时完全不显示该控件 --%>
<c:if test="${type == 'anchor' && PAGE.node != null}">
<nav class="anchor">
	您在位置 ：
	<a href="${ctx == '' ? '' : ctx}/">首页 </a>
	
	<%-- MVC模式下，url 路径还是按照 JSP 的而不是 Servlet 的，我们希望统一的路径是按照 Servlet 的，故所以这里 Servlet 优先 --%>
	
	<c:foreach items="${PAGE.node.supers}" var="_super">
		»<a href="${ctx}${_super.split(':')[0]}">${_super.split(':')[1]}</a>
	</c:foreach>
	»<a href="${ctx}${PAGE.node.fullPath}">${PAGE.node.name}</a>
	
	<%-- 如果有分类的话，先显示分类 （适合列表的情形）--%>
	<c:if test="${not empty catalog}">
		» ${catalog.name}
	</c:if>
	<%-- 实体之名称，如果有的话 --%>
	<c:if test="${not empty info}">
		» ${info.name}
	</c:if>
</nav>
</c:if>

