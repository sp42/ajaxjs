<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/public"%>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/public/UI"%>
<header>
	<div>
		<div class="right">
			<ui:widget type="search" />
		</div>
		<h1>
			<a href="${pageContext.request.contextPath}/">
				<img src="${pageContext.request.contextPath}/asset/images/logo.png" style="height: 40px;" />
				${global_config.site_description}
			</a>
		</h1>
	</div>
	<nav>
		<commonTag:page type="navMenu" />
	</nav>
</header>
 

<style type="text/css">
	nav.top {
	    border-bottom: 1px solid white;
	}
	.imgBanner{
	    text-align:center;
	    width:100%;
	    background-color:#f4faf4;
	}
</style>