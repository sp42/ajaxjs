<%@tag pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<%@attribute name="left" required="false" fragment="true" description="插入左侧菜单内容"%>
<%@attribute name="type" required="false" type="String" description="标签类型"%>
<%@attribute name="isOnlyCreateDate" required="false" type="Boolean" description="是否只显示创建日期"%>

<c:if test="${type == 'list'}">
	<c:if test="${not empty PageResult}">
		<div class="aj-pc-list-1">
			<ul>
				<c:foreach items="${PageResult}" var="item">
					<li>
						<h3>
							<a href="${item.id}/">${item.name}</a>
						</h3>
						<p>${item.intro}……</p>
						<div>
							<c:dateFormatter value="${item.createDate}" />
							类别：<a href="?catelogId=${item.catelogId}">${catalogs[item.catelogId].name}</a>
							| <a href="${item.id}/">阅读更多»</a>
						</div>
					</li>
				</c:foreach>
			</ul>
			<%@include file="/WEB-INF/jsp/pager_tag.jsp"%>
		</div>
	</c:if> 
</c:if>

<c:if test="${type == 'info' && not empty info}">
	<div class="aj-article-info">
		<article>
			<h1>${info.name}</h1>
			<h4 class="createDate">
			<c:choose>
				<c:when test="${isOnlyCreateDate}">
					创建于：<c:dateFormatter value="${info.createDate}" />
				</c:when>
				<c:otherwise>
					文/${info.author } <br />
					出处：${empty info.sourceUrl ? info.source : '<a href="'.concat(info.sourceUrl).concat('">').concat(info.source).concat('</a>') }<br />
					创建于：<c:dateFormatter value="${info.createDate}" /><br />最后编辑 ：<c:dateFormatter value="${info.updateDate}" />
				</c:otherwise>
			</c:choose>
			</h4>
			${info.content.replaceAll('src="', 'src="../../images/')}
		</article>
		<table class="vue" style="float:right;margin:10px 0 20px 0;">
			<tr>
				<td>
					<aj-page-share></aj-page-share>
				</td>
				<td>
					<aj-adjust-font-size></aj-adjust-font-size>
				</td>
			</tr>
		</table>
	</div>
	<script>
		new Vue({ el : '.vue' });
	</script>
</c:if>
