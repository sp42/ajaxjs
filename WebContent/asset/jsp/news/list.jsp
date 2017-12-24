<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags/"%>
<tags:content banner="../images/news.jpg" bodyClass="newsList">
	<jsp:attribute name="left">
		<commonTag:page type="catalogMenu" />
	</jsp:attribute>
	<jsp:attribute name="body">
		<commonTag:list type="list-thumb" classList="list" />
	</jsp:attribute>
</tags:content>
