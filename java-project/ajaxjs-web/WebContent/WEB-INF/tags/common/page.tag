<%@tag pageEncoding="UTF-8" description="Page HTML"%>
<%@attribute name="type" type="String" required="true" description="该属性选择类型"%>
<%@ taglib uri="/ajaxjs" prefix="c"%>

<%-- 导航 --%>
<c:if test="${type == 'navMenu'}">
	<ul>
		<li><a href="${pageContext.request.contextPath}/">首页</a></li>
	<c:foreach items="${PageNode.navBar}" var="menu">
		<li ${PageNode.isCurrentNode(menu) ? ' class="selected"' : ''}>
			<a href="${pageContext.request.contextPath}/${menu.fullPath}/">${menu.name}</a>
		</li>
	</c:foreach>
	</ul>
</c:if>

<%-- 二级菜单 --%>
<c:if test="${type == 'secondLevelMenu'}">
	<ul>
	<c:foreach items="${PageNode.pageNode.menu}" var="menu">
		<li ${PageNode.isCurrentNode(menu) ? ' class="selected"' : ''}>
			<a href="${pageContext.request.contextPath}/${menu.fullPath}/">${menu.name}</a>
		</li>
	</c:foreach>
	</ul>
</c:if>

<c:if test="${type == 'anchor'}">
<nav class="anchor">
	您在位置 ：
	<a href="${pageContext.request.contextPath == '' ? '' : pageContext.request.contextPath}/">首页 </a>
	<%-- MVC模式下，url 路径还是按照 JSP 的而不是 Servlet 的，我们希望统一的路径是按照 Servlet 的，故所以这里 Servlet 优先 --%>
	<c:choose>
		<c:when test="${not empty PageNodeByServlet}">
		  » ${PageNodeByServlet.pageNode.anchorLink}
		</c:when>
		<c:otherwise>
			<c:if test="${not empty PageNode.pageNode.anchorLink}">
			 » ${PageNode.pageNode.anchorLink}
			</c:if>
		</c:otherwise>
	</c:choose>
	
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

<%-- 读取数据库的菜单 --%>
<c:if test="${type == 'catalogMenu' && not empty catalogMenu}">
<%@attribute name="listPath" type="String" required="false" description="返回上一层，列表目录。既要适合在列表用，又要适合在详情页用"%>
	<ul>
		<li>
			<a href="?">全部分类</a>
		</li>
	<c:foreach items="${catalogMenu}" var="catalog">
		<li ${param.catalogId == catalog.id ? 'class="selected"' : ''}>
			<a href="${empty listPath ? '' : '../'.concat(listPath)}?catalogId=${catalog.id}">
				${catalog.name}
			</a>
		</li>
	</c:foreach>
	</ul>
</c:if>

<%-- 分页 --%>
<%@attribute name="pageInfo" type="com.ajaxjs.view.PageResultView" required="false" description="分页对象"%>
<c:if test="${type == 'page'}">
	<section class="pageInfo">
	<c:choose>
	<c:when test="${pageInfo.totalCount > 0}">
		<c:if test="${pageInfo.start > 0}">
			<a href="?start=${pageInfo.start - pageInfo.pageSize}${viewUtils.getParams_without('start', pageContext.request.queryString)}">上一页</a>
		</c:if>
		<c:if test="${(pageInfo.start > 0 )&& (pageInfo.start + pageInfo.pageSize < pageInfo.totalCount)}">
			<a href="#" style="text-decoration: none;">&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</a>
		</c:if>
		<c:if test="${pageInfo.start + pageInfo.pageSize < pageInfo.totalCount}"> 
			<a href="?start=${pageInfo.start + pageInfo.pageSize}${viewUtils.getParams_without('start', pageContext.request.queryString)}">下一页</a>
		</c:if>
		<div class="info" style="vertical-align: bottom;">
		 	页数：${pageInfo.currentPage}/${pageInfo.totalPage} 记录数：${pageInfo.start}/${pageInfo.totalCount} 
		 	<form style="display:inline-block;vertical-align: bottom;" method="GET">
			 	每页记录数：
			 	<input size="4" title="输入一个数字确定每页记录数" type="text" name="limit" value="${pageInfo.pageSize}" 
			 	style="text-align:center;width:40px;height:22px;float: none;" />
				<!-- 其他参数 -->
				<c:foreach items="${viewUtils.getParams_without_asMap('limit', pageContext.request.queryString)}" var="current">
					<input type="hidden" name="${current.key}" value="${current.value}" />
				</c:foreach>
		 	</form>
		 	<%--分页数过多影响 HTML 加载，这里判断下 --%>
		 	<c:if test="${pageInfo.totalPage < 1000}">
		 	跳转：
		 	<select onchange="jumpPage(this);" style="text-align:center;width:40px;height:22px;" class="select_1">
			 	<c:foreach items="${viewUtils.jumpPage(pageInfo.totalPage)}" var="i">
			 		<option value="${currentIndex * pageInfo.pageSize}" 
			 		${(currentIndex + 1)==pageInfo.currentPage ? ' selected' : ''}>${currentIndex + 1}</option>
			 	</c:foreach>
		 	</select>
		 	</c:if>
		</div>
		
		<script type="text/javascript">
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