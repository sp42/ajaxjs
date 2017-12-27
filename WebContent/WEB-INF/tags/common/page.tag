<%@tag import="com.ajaxjs.web.Constant"%>
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
		<commonTags:widget type="search" />
	</div>
	<nav>
		<commonTags:page type="navMenu" />
	</nav>
	<h1>
		<a href="${pageContext.request.contextPath}/">
			<img src="${pageContext.request.contextPath}/asset/images/logo.png" style="height: 80px;" />
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
				${PageNode.siteMap}
			</div>
		</div>

		<div class="copyright">
		<div>
			<jsp:doBody />
			<a href="#">
				<img src="${pageContext.request.contextPath}/<%=Constant.commonImage%>gs.png" height="40" />
			</a> 
			<a href="#">
				<img src="${pageContext.request.contextPath}/<%=Constant.commonImage%>kexin.png" hspace="20" width="90" style="margin-top:15px;" />
			</a> 
			<a href="#">
				<img src="${pageContext.request.contextPath}/<%=Constant.commonImage%>360logo.gif" width="90" style="margin-top:15px;" />
			</a>
			<br />
			<a href="javascript:;" onclick="toSimpleChinese(this);" class="simpleChinese selected">简体中文</a>
			/
			<a href="javascript:;" class="Chinese" onclick="toChinese(this);">正体中文</a>
			<script src="${pageContext.request.contextPath}/<%=Constant.commonFolder %>js/libs/chinese.js"></script>
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

<%-- 读取数据库的菜单 --%>
<c:if test="${type == 'catalogMenu' && not empty catalogMenu}">
<%@attribute name="listPath" type="String" required="false" description="返回上一层，列表目录。既要适合在列表用，又要适合在详情页用"%>
	<ul>
		<li>
			全部分类
		</li>
	<c:foreach items="${catalogMenu}" var="catalog">
		<li ${param.filterValue == catalog.id ? 'class="selected"' : ''}>
			<a href="${empty listPath ? '' : '../'.concat(listPath)}?filterField=catalog&filterValue=${catalog.id}">
				${catalog.name}
			</a>
		</li>
	</c:foreach>
	</ul>
</c:if>



<%-- 分类 约定：catalogs 为 List<Map<String, Object>> 结构；url 参数 catalogId 有匹配则选中 item 或者 catalogId 变量 --%>
<%@attribute name="isNotJump" type="Boolean" required="false" description="选择后是否调转？"%>
<c:if test="${type == 'catalog_dropdownlist'}">
	<span class="catalog_dropdownlist"> 
		分类： 
		<script>
			function onCatalogSelected(el) {
				var catalogId = el.selectedOptions[0].value;
				if (catalogId == '全部分类')
					location.assign(location.origin + location.pathname); // todo
				else
					location.assign('?catalogId=' + catalogId);
			}
		</script> 
		<select onchange="${isNotJump ? '' : 'onCatalogSelected(this);'}" class="select_1" name="catalog">
				<option>全部分类</option>
				<c:foreach items="${catalogs}" var="current">
					<c:choose>
						<c:when test="${param.catalogId == current.id || info.catalog == current.id || catalogId == current.id }">
							<option value="${current.id}" selected>${current.name}</option>
						</c:when>
						<c:otherwise>
							<option value="${current.id}">${current.name}</option>
						</c:otherwise>
					</c:choose>
				</c:foreach>
		</select>
	</span>
</c:if>

<%-- 分页 --%>
<%@attribute name="pageInfo" type="com.ajaxjs.jdbc.PageResult" required="false" description="分页对象"%>
<jsp:useBean id="PageUtil" class="com.ajaxjs.mvc.view.PageTag" />
<c:if test="${type == 'page' && not empty pageInfo}">
	<section class="pageInfo">
	<c:choose>
	<c:when test="${pageInfo.totalCount > 0}">
		<c:if test="${pageInfo.start > 0}">
			<a href="?start=${pageInfo.start - pageInfo.pageSize}${PageUtil.getParams_without('start', pageContext.request.queryString)}">上一页</a>
		</c:if>
		<c:if test="${(pageInfo.start > 0 )&& (pageInfo.start + pageInfo.pageSize < pageInfo.totalCount)}">
			<a href="#" style="text-decoration: none;">&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</a>
		</c:if>
		<c:if test="${pageInfo.start + pageInfo.pageSize < pageInfo.totalCount}"> 
			<a href="?start=${pageInfo.start + pageInfo.pageSize}${PageUtil.getParams_without('start', pageContext.request.queryString)}">下一页</a>
		</c:if>
		
		<div class="info" style="vertical-align: bottom;">
		 	页数：${pageInfo.currentPage}/${pageInfo.totalPage} 记录数：${pageInfo.start}/${pageInfo.totalCount} 
		 	<form style="display:inline-block;vertical-align: bottom;" method="GET">
			 	每页记录数：
			 	<input size="4" title="输入一个数字确定每页记录数" type="text" name="limit" value="${empty param.limit ? pageInfo.pageSize : param.limit}" 
			 	style="text-align:center;width:40px;height:22px;float: none;" class="ajaxjs-inputField" />
				<!-- 其他参数 -->
				<c:foreach items="${PageUtil.getParams_without_asMap('limit', pageContext.request.queryString)}" var="current">
					<input type="hidden" name="${current.key}" value="${current.value}" />
				</c:foreach>
		 	</form>
		 	<%--分页数过多影响 HTML 加载，这里判断下 --%>
		 	<c:if test="${pageInfo.totalPage < 1000}">
		 	跳转：
		 	<select onchange="jumpPage(this);" style="text-align:center;width:40px;height:22px;" class="ajaxjs-select">
			 	<c:foreach items="${PageUtil.jumpPage(pageInfo.totalPage)}" var="i">
			 		<option value="${currentIndex * pageInfo.pageSize}" 
			 		${(currentIndex + 1)==pageInfo.currentPage ? ' selected' : ''}>${currentIndex + 1}</option>
			 	</c:foreach>
		 	</select>
		 	</c:if>
		</div>
		
		<script>
			/**
			 * 分页，跳到第几页，下拉控件传入指定的页码。
			 */
			function jumpPage(selectEl) {
				var start = selectEl.options[selectEl.selectedIndex].value;	
				var go2 = location.search;
				
				if(go2.indexOf('start=') != -1) {
					go2 = go2.replace(/start=\d+/, 'start=' + start);
				} else {
					go2 += go2.indexOf('?') != -1 ? ('&start=' + start) : ('?start=' + start);
				}
				
				location.assign(go2);
			}
		</script>
		
	</c:when>
	<c:otherwise>
			没有数据！
	</c:otherwise>
	</c:choose>
	</section>
</c:if>

<%-- 相邻的两笔记录 --%>
<c:if test="${type == 'neighborRecord'}">
	<c:if test="${not empty neighbor.perRecord.id}">
		<div>
			<a href="?id=${neighbor.perRecord.id}">上则记录：${neighbor.perRecord.name}</a>
		</div>
	</c:if>
	<c:if test="${not empty neighbor.nextRecord.id}">
		<div>
			<a href="?id=${neighbor.nextRecord.id}">下则记录：${neighbor.nextRecord.name}</a>
		</div>
	</c:if>
</c:if>