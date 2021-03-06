<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags/"%>
<tags:content banner="../../images/news.jpg" bodyClass="newsContent">
	<jsp:attribute name="left">
		<commonTag:page type="catalogMenu" />
	</jsp:attribute>
	<jsp:attribute name="body">
		<article> 
			<h3>${info.name}</h3>
			<h4>${info.createDate}</h4>
			${info.content}
		</article> 
	
		<div style="padding: 20px 0;" align="right">
			<!-- 分享功能 -->
			<commonTag:article type="share" />
			<!-- 调整页面字号 -->
			<commonTag:article type="adjustFontSize" />
			<!-- 页面功能 -->
			<commonTag:article type="function" />
		</div>
	</jsp:attribute>
</tags:content>

