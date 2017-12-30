<%@tag import="com.ajaxjs.web.Constant"%>
<%@tag pageEncoding="UTF-8" description="Page HTML"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<%@attribute name="type" type="String" required="true" description="指定哪种 HTML 片断"%>
<%@attribute name="classList" type="String" required="false" description="样式类"%>
<%@attribute name="urlPerfix" type="String" required="false" description="URL地址前缀"%>
<%@attribute name="list" type="java.util.List"  required="false" description="列表内容"%>
<%-- 单行 --%>
<c:if test="${type == 'list-simple'}">
	<ul class="${classList}">
		<c:foreach var="current" items="${list}">
			<li><a href="${urlPerfix}${current.id}/info.do">
					<h4>${current.name}</h4>
			</a></li>
		</c:foreach>
	</ul>
</c:if>

<%-- 有缩略图的 --%>
<c:if test="${type == 'list-thumb'}">
	<%-- 如果有异常则显示之 --%>
	${(not empty errMsg ) ? (errMsg.message) : ''}
	${(not empty errMsg.cause ) ? (errMsg.cause.message) : ''}
	
	<ul class="${classList}">
		<c:foreach var="current" items="${PageResult.rows}">
			<li>
				<div class="thumb">

					<img
						src="http://localhost:8080/ajaxjs-web/asset/common/images/360logo.gif" />

				</div>
				<div class="text">
					<a href="${current.id}/info.do">
						<h4>${current.name}</h4>
					</a>
					<p>${current.intro}
						<a href="${current.id}/info.do">阅读更多……</a>
					</p>
					<div class="small">作者：Admin|日期：${current.createDate}|阅读次数：10
					</div>
				</div>

			</li>
		</c:foreach>
	</ul>
	
	<commonTag:pager pageInfo="${PageResult}"/>
</c:if>

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