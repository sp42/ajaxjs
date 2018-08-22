<%@tag pageEncoding="UTF-8" description="展现数据实体列表用"  import="com.ajaxjs.simpleApp.Constant"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<%@attribute name="type" type="String" required="true" description="指定哪种 HTML 片断"%>



<c:if test="${type == 'pager'}">
<%@attribute name="pageInfo" type="com.ajaxjs.jdbc.PageResult" required="false" description="分页对象"%>
<jsp:useBean id="PageUtil" class="com.ajaxjs.mvc.view.PageTag" />

<section class="pageInfo">
	<c:choose>
		<c:when test="${pageInfo.totalCount > 0}">
			<c:if test="${pageInfo.start > 0}">
				<a href="?start=${pageInfo.start - pageInfo.pageSize}${PageUtil.getParams_without('start', pageContext.request.queryString)}">上一页</a>
			</c:if>
			<c:if
				test="${(pageInfo.start > 0 ) && (pageInfo.start + pageInfo.pageSize < pageInfo.totalCount)}">
				<a href="#" style="text-decoration: none;">&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</a>
			</c:if>
			<c:if
				test="${pageInfo.start + pageInfo.pageSize < pageInfo.totalCount}">
				<a href="?start=${pageInfo.start + pageInfo.pageSize}${PageUtil.getParams_without('start', pageContext.request.queryString)}">下一页</a>
			</c:if>

			<div class="info" style="vertical-align: bottom;">
				页数：${pageInfo.currentPage}/${pageInfo.totalPage}
				记录数：${pageInfo.start}/${pageInfo.totalCount}
				<form style="display: inline-block; vertical-align: bottom;"
					method="GET">
					每页记录数： <input size="4" title="输入一个数字确定每页记录数" type="text"
						name="limit"
						value="${empty param.limit ? pageInfo.pageSize : param.limit}"
						style="text-align: center; width: 40px; height: 22px; float: none;"
						class="ajaxjs-inputField" />
					<!-- 其他参数 -->
					<c:foreach items="${PageUtil.getParams_without_asMap('limit', pageContext.request.queryString)}" var="current">
						<input type="hidden" name="${current.key}" value="${current.value}" />
					</c:foreach>
				</form>
				<%--分页数过多影响 HTML 加载，这里判断下 --%>
				<c:if test="${pageInfo.totalPage < 1000}">
				 	跳转：
				 	<select onchange="jumpPage(this);" style="text-align: center; width: 40px; height: 22px;" class="ajaxjs-select">
						<c:foreach items="${PageUtil.jumpPage(pageInfo.totalPage)}" var="i">
							<option value="${currentIndex * pageInfo.pageSize}" ${(currentIndex + 1)==pageInfo.currentPage ? ' selected' : ''}>${currentIndex + 1}</option>
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

					if (go2.indexOf('start=') != -1) {
						go2 = go2.replace(/start=\d+/, 'start=' + start);
					} else {
						go2 += go2.indexOf('?') != -1 ? ('&start=' + start)
								: ('?start=' + start);
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