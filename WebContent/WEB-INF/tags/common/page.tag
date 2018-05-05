<%@tag pageEncoding="UTF-8" description="Page HTML"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<%@attribute name="type" type="String" required="true" description="指定哪种 HTML 片断"%>

<%-- 导航 --%>
<c:if test="${type == 'navMenu'}">
	<ul>
		<li><a href="${pageContext.request.contextPath}/">首页</a></li>
	<c:foreach items="${SITE_STRU.navBar}" var="menu">
		<li ${SITE_STRU.isCurrentNode(menu, pageContext.request) ? ' class="selected"' : ''}>
			<a href="${pageContext.request.contextPath}/${menu.id}/">${menu.name}</a>
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
		<a href="${pageContext.request.contextPath}/user/login.jsp">会员登录</a>
	</div>
	<nav>
		<commonTags:page type="navMenu" />
		
	</nav>
	<h1>
		<a href="${pageContext.request.contextPath}/">
			<img src="${pageContext.request.contextPath}/asset/images/logo.png" class="logoPic" />
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
			<a href="${pageContext.request.contextPath}${menu.fullPath}/">${menu.name}</a>
		</li>
	</c:foreach>
	</ul>
</c:if>

<%-- 次级菜单 --%>
<c:if test="${type == 'submenu'}">
	<ul>
	<c:foreach items="${PAGE.node.children}" var="menu">
		<li ${SITE_STRU.isCurrentNode(menu, pageContext.request) ? ' class="selected"' : ''}>
			<a href="${pageContext.request.contextPath}/${menu.fullPath}/">${menu.name}</a>
		</li>
	</c:foreach>
	</ul>
</c:if>

<%-- 定位，当没有 PAGE.node 时完全不显示该控件 --%>
<c:if test="${type == 'anchor' && PAGE.node != null}">
<nav class="anchor">
	您在位置 ：
	<a href="${pageContext.request.contextPath == '' ? '' : pageContext.request.contextPath}/">首页 </a>
	
	<%-- MVC模式下，url 路径还是按照 JSP 的而不是 Servlet 的，我们希望统一的路径是按照 Servlet 的，故所以这里 Servlet 优先 --%>
	
	<c:foreach items="${PAGE.node.supers}" var="_super">
		»<a href="${pageContext.request.contextPath}${_super.split(':')[0]}">${_super.split(':')[1]}</a>
	</c:foreach>
	»<a href="${pageContext.request.contextPath}${PAGE.node.fullPath}">${PAGE.node.name}</a>
	
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

<%-- 页脚 --%>
<%if(type.equals("footer")) {%>
<footer>
		<div class="sitemap">
			<div>
				<div class="btn" onclick="document.querySelector('.sitemap').toggleCls('open');"></div>
				${SITE_STRU.getSiteMap(pageContext.request)}
			</div>
		</div>

		<div class="copyright">
		<div>
			<jsp:doBody />
			<a href="#">
				<img src="${commonImage}gs.png" height="40" />
			</a> 
			<a href="#">
				<img src="${commonImage}kexin.png" hspace="20" width="90" style="margin-top:15px;" />
			</a> 
			<a href="#">
				<img src="${commonImage}360logo.gif" width="90" style="margin-top:15px;" />
			</a>
			<br />
			<a href="javascript:;" onclick="toSimpleChinese(this);" class="simpleChinese selected">简体中文</a>
			/
			<a href="javascript:;" class="Chinese" onclick="toChinese(this);">正体中文</a>
			<script src="${commonAsset}js/libs/chinese.js"></script>
			<br />
			${empty _config.site_icp ? '粤ICP备15007080号-2' :  _config.site_icp}
			Powered by <a target="_blank" href="http://framework.ajaxjs.com">AJAXJS</a>
			<br />
			<%
			
			if(request.getAttribute("requestTimeRecorder") != null ){
				Long requestTimeRecorder = (Long)request.getAttribute("requestTimeRecorder");
				requestTimeRecorder = System.currentTimeMillis() - requestTimeRecorder;
				float _requestTimeRecorder = (float)requestTimeRecorder;
				_requestTimeRecorder = _requestTimeRecorder / 1000;
				// float seconds = (endTime - startTime) / 1000F;
				request.setAttribute("requestTimeRecorder", _requestTimeRecorder); 
			}
			%>
	 	©Copyright <%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%> 版权所有， ${_config.clientFullName} ${empty requestTimeRecorder ? '' : '请求完成耗时：'.concat(requestTimeRecorder).concat('秒') }
	
	</div>
</div>
</footer>
<%}%>


<c:if test="${type == 'search'}">
    <!-- 自定义 Baidu 搜索 -->
    <form id="globalSearch" class="globalSearch" method="GET" action="http://www.baidu.com/baidu" onsubmit="//return g(this);">
        <input type="text" name="word" placeholder="请输入搜索之关键字" />
        <input name="tn" value="bds" type="hidden" />
        <input name="cl" value="3" type="hidden" />
        <input name="ct" value="2097152" type="hidden" />
        <input name="si" value="gz88.cc" type="hidden" />
    <div class="searchBtn" onclick="document.getElementById('globalSearch').submit();"></div>
    </form>
    <!-- // 自定义 Baidu 搜索 -->
</c:if>