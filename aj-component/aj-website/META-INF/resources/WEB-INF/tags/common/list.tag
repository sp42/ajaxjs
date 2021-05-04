<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@taglib uri="/ajaxjs" 		prefix="c" %>
<%@attribute name="listClass" required="fasle" type="String" description="列表样式类"%>
<%@attribute name="catalogs"  required="fasle" type="java.util.Map" description="类别"%>

<c:if test="${not empty PageResult}">
	<div class="${empty listClass ? 'aj-article-list' : listClass}">
		<ul>
			<c:foreach items="${PageResult}" var="item">
				<li>
					<h3>
						<a href="${item.id}/">${item.name}</a>
					</h3> 
					<c:if test="${not empty item.cover}">
						<a href="${item.id}/">
							<div class="thumb">
								<img src="${item.cover.startsWith('http') ? item.cover : aj_allConfig.uploadFile.imgPerfix.concat(item.cover)}" />
							</div>
						</a>
					</c:if>
					<p>${item.intro}……</p>
					<div>
						<c:dateFormatter value="${item.createDate}" format="YYYY-MM-dd" />
						类别：<a href="?catalogId=${item.catalogId}">${catalogs[item.catalogId].name}</a>
						| <a href="${item.id}/">阅读更多»</a>
					</div>
				</li>
			</c:foreach>
		</ul>
	</div>
</c:if>
