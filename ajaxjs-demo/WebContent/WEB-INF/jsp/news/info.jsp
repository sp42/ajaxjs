<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags/"%>
<tags:content banner="../../images/news.jpg" bodyClass="newsContent">
	<jsp:attribute name="left">
		<commonTag:list type="catalogMenu" listPath="." />
	</jsp:attribute>
	<jsp:attribute name="body">
		<commonTag:article />
	</jsp:attribute>
</tags:content>

