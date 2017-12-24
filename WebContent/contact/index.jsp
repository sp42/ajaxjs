<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags/"%>
<tags:content banner="../images/contact.jpg" bodyClass="">
	<jsp:attribute name="left">
		<ul>
			<li>公司电话（020）37392396</li>
			<li>邮箱地址 bp@egdtv.com</li>
			<li>公司地址 广州市天河区高普路国家软件基地</li>
		</ul>
	</jsp:attribute>
	<jsp:attribute name="body">
		<img src="../images/contact2.jpg" width="100%" /> 
		<div style="padding: 20px 0;" align="right">
			<!-- 分享功能 -->
			<commonTag:widget type="share" />
			<!-- 调整页面字号 -->
			<commonTag:widget type="adjustFontSize" />
			<!-- 页面功能 -->
			<commonTag:widget type="function" />
		</div>
	</jsp:attribute>
</tags:content>

