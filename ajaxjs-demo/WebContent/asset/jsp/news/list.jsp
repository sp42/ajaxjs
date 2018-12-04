<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags/"%>
<tags:content banner="../images/news.jpg" bodyClass="newsList">
	<jsp:attribute name="left">
		<commonTag:list type="catalogMenu" />
	</jsp:attribute>
	<jsp:attribute name="body">
		<!-- 引用列表 -->
		<commonTag:list type="list-thumb" classList="list" />
	</jsp:attribute>
</tags:content>
