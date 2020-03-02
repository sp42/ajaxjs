<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags/"%>
<%@taglib prefix="config" uri="/ajaxjs_config"	%>
<tags:content bodyClass="" bannerText="">
	<jsp:attribute name="left">
		<div class="baiduSearch">
			<aj-baidu-search></aj-baidu-search>
		</div>
		<script>
			new Vue({
				el : '.baiduSearch'
			});
		</script>
		<div style="height: 400px;display: flex; align-items:flex-start;">
			<ul style="width: 100%;">
				<config:siteStru type="secondLevelMenu" />
			</ul>
		</div>		
	</jsp:attribute>
	<jsp:attribute name="body">
		<article>
			<h2>${PAGE_Node.name}</h2>
			
			<!-- Editable AREA|START -->
			<p>
				<img src="${commonAsset}/images/under_construction.jpg" />
			</p>
			<!-- Editable AREA|END -->
			
			<table class="vue" style="float:right;margin:10px 0 20px 0;">
				…… 
			</table>
		</article>
	</jsp:attribute>
</tags:content>
