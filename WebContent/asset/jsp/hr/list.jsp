<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags/"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<tags:content banner="../images/news.jpg" bodyClass="hrList">
	<jsp:attribute name="left">
		<commonTag:page type="catalogMenu" />
	</jsp:attribute>
	<jsp:attribute name="body">
		<ul class="${classList}">
		<c:foreach var="current" items="${PageResult.rows}">
			<li>
				<div class="box">
					<header>
						<h5>分类：${current.catalogName}</h5>
						<h4>${current.name}</h4>
					</header>
					<div>
						${current.content}
					</div>
				</div>
			</li>
		</c:foreach>
	</ul>
	
	<commonTag:page type="page" pageInfo="${PageResult}"/>
	</jsp:attribute>
</tags:content>
