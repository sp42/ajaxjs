<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<%-- <%@attribute name="PageResult" type="com.ajaxjs.jdbc.PageResult" required="false" description="分页对象"%> --%>
<jsp:useBean id="PageUtil" class="com.ajaxjs.web.view.PageTag" />

<section class="pageInfo aj-pager">
	<c:choose>
		<c:when test="${PageResult.getTotalCount() > 0}">
			<c:if test="${PageResult.getStart() > 0}">
				<a href="?start=${PageResult.getStart() - PageResult.getPageSize()}${PageUtil.getParams_without('start', pageContext.request.queryString)}">上一页</a>
			</c:if>
<%-- 			<c:if test="${(PageResult.getStart() > 0 ) && (PageResult.getStart() + PageResult.getPageSize() < PageResult.getTotalCount())}"> --%>
<!-- 				<a href="#" style="text-decoration: none;">&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</a> -->
<%-- 			</c:if> --%>
			<c:if test="${PageResult.getStart() + PageResult.getPageSize() < PageResult.getTotalCount()}">
				<a href="?start=${PageResult.getStart() + PageResult.getPageSize()}${PageUtil.getParams_without('start', pageContext.request.queryString)}">下一页</a>
			</c:if>

			<div class="info" style="vertical-align: bottom;">
				页数：${PageResult.getCurrentPage()}/${PageResult.getTotalPage()}
				记录数：${PageResult.getStart()}/${PageResult.getTotalCount()}
				<form method="GET">
					每页记录数： <input size="4" title="输入一个数字确定每页记录数" type="text"
						name="limit" value="${empty param.limit ? PageResult.getPageSize() : param.limit}" />
					<!-- 其他参数 -->
					<c:foreach items="${PageUtil.getParams_without_asMap('limit', pageContext.request.queryString)}" var="current">
						<input type="hidden" name="${current.key}" value="${current.value}" />
					</c:foreach>
				</form>
				<%--分页数过多影响 HTML 加载，这里判断下 --%>
				<c:if test="${PageResult.getTotalPage() < 1000}">
				 	跳转：<select onchange="jumpPage(this);">
						<c:foreach items="${PageUtil.jumpPage(PageResult.getTotalPage())}" var="i">
							<option value="${currentIndex * PageResult.getPageSize()}" ${(currentIndex + 1)==PageResult.getCurrentPage() ? ' selected' : ''}>${currentIndex + 1}</option>
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

					if (go2.indexOf('start=') != -1) 
						go2 = go2.replace(/start=\d+/, 'start=' + start);
					 else 
						go2 += go2.indexOf('?') != -1 ? ('&start=' + start) : ('?start=' + start);
					
					location.assign(go2);
				}
			</script>
		</c:when>
		<c:otherwise>没有数据！</c:otherwise>
	</c:choose>
</section>