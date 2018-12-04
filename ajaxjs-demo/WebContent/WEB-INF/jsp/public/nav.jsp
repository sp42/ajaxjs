<%@taglib uri="/ajaxjs" prefix="c"%>
<%-- <%@page pageEncoding="UTF-8"%> --%>
<header>
	<div class="logo">
		<img src="${ctx}/asset/images/logo.png" /> 源自法国品牌专业污水提升器
	</div>
	<div class="right">
		<aj-baidu-search></aj-baidu-search>
		<div class="tel">
			<img src="${ctx}/asset/images/tel.jpg"  />
			138-0297-5784 苏先生 &nbsp;
		</div>
	</div>
</header>
<nav>
	<ul class="">
		<c:siteStru type="nav-bar" />
	</ul>
</nav>
<script>
	new Vue({
		el : 'body > header .right'
	});
</script>