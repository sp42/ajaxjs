<%@tag import="com.ajaxjs.web.Constant"%>
<%@tag pageEncoding="UTF-8" description="Page HTML"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<%@attribute name="type" type="String" required="true"
	description="指定哪种 HTML 片断"%>
<%@attribute name="classList" type="String" required="false"
	description="样式类"%>
<%@attribute name="urlPerfix" type="String" required="false"
	description="URL地址前缀"%>
<%@attribute name="list" type="java.util.List"  required="false"
	description="列表内容"%>
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
	
	<commonTag:page type="page" pageInfo="${PageResult}"/>
</c:if>