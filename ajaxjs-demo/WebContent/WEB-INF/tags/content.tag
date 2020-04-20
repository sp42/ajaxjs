<%@tag pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" 		prefix="c"%>
<%@taglib uri="/ajaxjs_config"	prefix="config"%>
<%@taglib tagdir="/WEB-INF/tags/" prefix="tagfile"%>

<%@attribute name="left" 		required="false" fragment="true" description="插入左侧菜单内容"%>
<%@attribute name="body" 		required="false" fragment="true" description="插入正文内容"%>
<%@attribute name="bannerText" 	required="false" type="String" description="Banner 文字"%>
<%@attribute name="bannerImg" 	required="false" type="String" description="Banner 图片"%>
<%@attribute name="bodyClass" 	required="false" type="String" description="body 标签样式"%>

<!DOCTYPE html>
<html>
<head>
	<!-- 通用头部 -->
	<%@include file="/WEB-INF/jsp/head.jsp"%>
</head>
<body class="${bodyClass}">
	<!-- 导航条 -->
	<header class="top">
		<div class="logo">
			<img src="${ctx}/asset/images/logo.png" /> 
		</div>
		<div class="right">
			<ul class="">
				<config:siteStru type="navBar" />
			</ul>
		</div>
	</header>
	
	<!-- 头图 -->
	<section class="banner">
		<div>
			<div class="text">
				<c:if test="${not empty bannerText}">
					${bannerText}
				</c:if>
				<c:if test="${empty bannerText}">
					${PAGE_Node.name}
				</c:if>
			</div>
			<div class="img">
			<c:if test="${not empty bannerImg}">
				<img src="${bannerImg}" />
			</c:if>
			</div>
		</div>

	</section>
	<section class="banner2">
		<div>
			<config:siteStru type="breadCrumb" />
		</div>
	</section>
	
	<!-- 正文 -->
	<div class="centerWidth body">
		<c:choose>
			<c:when test="${left != null && body != null}">
				<!-- 左、右结构 -->
				<div class="left">
					<jsp:invoke fragment="left" />
				</div>
				<div class="right">
					<jsp:invoke fragment="body" />
				</div>
			</c:when>
			<c:otherwise>
				<!-- 不分结构 -->
				<jsp:doBody />
			</c:otherwise>
		</c:choose>
	</div>
	
	<!-- 页脚 -->
	<tagfile:common type="footer" />
</body>
</html>