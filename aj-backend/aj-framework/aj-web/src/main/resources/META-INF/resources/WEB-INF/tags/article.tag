<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<article class="aj-article">
	<h1 class="aj-title-2">${info.name}</h1>
	<h4 class="createDate aj-kaiti">
		<c:choose>
			<c:when test="${isOnlyCreateDate}">
				创建于：<c:dateFormatter value="${info.createDate}" />
			</c:when>
			<c:otherwise>
				文/${empty info.author ? '佚名' : info.author}
				${not empty catalogs ? '<br />分类/': ''} ${not empty catalogs ? catalogs[info.catalogId].name : ''}
				<c:if test="${not empty info.source}"> 出处/${info.source}</c:if>
						<c:if test="${not empty info.sourceUrl}">
							<a href="${info.sourceUrl}" target="_blank"> 源网址 &#128279;</a>
						</c:if>
						<br />
				创建于/<c:dateFormatter value="${info.createDate}" format="YYYY-MM-dd" /> 最后编辑/<c:dateFormatter value="${info.updateDate}" format="YYYY-MM-dd" />
			</c:otherwise>
		</c:choose>
	</h4>

	${info.content.replaceAll('src="[^http]', 'src="../i')}
</article>
