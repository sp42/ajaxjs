<%@tag pageEncoding="UTF-8" description="文章功能模块"%>
<%@attribute name="body"  required="false" fragment="true" description="插入正文内容"%>
<%@attribute name="title"  required="false" type="String" description="标题"%>		
<%@attribute name="isNoMenu"  required="false" type="Boolean" description="是否需要  menu 内容"%>		
<%@attribute name="isSecondLevel"  required="false" type="Boolean" description="是否二级栏目，否则为三级"%>		
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<!DOCTYPE html>
<html
	class="js flexbox canvas canvastext webgl no-touch geolocation postmessage no-websqldatabase indexeddb hashchange history draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow textshadow opacity cssanimations csscolumns cssgradients no-cssreflections csstransforms csstransforms3d csstransitions fontface generatedcontent video audio localstorage sessionstorage webworkers applicationcache svg inlinesvg smil svgclippaths tablesaw-enhanced">
<commonTag:head>
	<link rel="stylesheet" href="../asset/web/metinfo.css" />
</commonTag:head>

<body class="met-navfixed">
	<%@include file="/WEB-INF/jsp/public/nav.jsp"%>
	
	<div class="banner">
		<img src="../asset/web/1500545439.jpg" alt="" />
	</div>
	<div class="menu">
		<!-- 当前位置 -->
		<commonTag:page type="secondLevelMenu" />
	</div>
	<div class="bodyWidth article">
		<div style="float:right;margin:10px;" class="menu">
			<!-- 当前位置 -->
			<commonTag:page type="anchor" />
		</div>
		<br />
		<p style="text-indent: 2em;">
			<h2 style="text-align:center;">${PAGE.node.name}</h2>
		</p>
		<jsp:invoke fragment="body" />
		<p>
			<br>
		</p>
		
<div style="padding:20px 0;" align="right">
	<!-- 分享功能 -->
	<commonTag:widget type="share" />
	<!-- 调整页面字号 -->
	<commonTag:widget type="adjustFontSize" />
</div>
	</div>
	<%@include file="/WEB-INF/jsp/public/footer.jsp"%>
</body>
</html>