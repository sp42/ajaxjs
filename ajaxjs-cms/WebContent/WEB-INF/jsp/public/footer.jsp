<%-- <%@page pageEncoding="utf-8"%> --%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<div class="met-footnav text-center met-index-body">
		<div class="container">
			<div class="row mob-masonry">
				<!-- 所有栏目一览，通常展现在页脚 -->
				<style>
					.siteMap{
						width:80%;
					}
					.siteMap a {
						display:block;
						margin-left:3px;
					}
					.siteMap td {
						vertical-align: top;
						padding: 0 1%;
						text-align:left;
					}
					
					.indentBlock_0{
						font-size:18px;
						margin-left:0 !important;
					}
				</style>
				${SITE_STRU.siteMap}
			</div>
		</div>
	</div>

	<div class="met-links text-center">
		<div class="container">
			<ol class="breadcrumb">
				<li>友情链接 :</li>

				<li><a href="http://" title="" target="_blank" class="link_img"><img
						src="${pageContext.request.contextPath}/asset/web/1499246368.png"></a></li>

				<li><a href="http://" title="" target="_blank" class="link_img"><img
						src="${pageContext.request.contextPath}/asset/web/1499246324.png"></a></li>

				<li><a href="http://" title="" target="_blank" class="link_img"><img
						src="${pageContext.request.contextPath}/asset/web/1499246735.png"></a></li>

				<li><a href="http://" title="" target="_blank" class="link_img"><img
						src="${pageContext.request.contextPath}/asset/web/1499246807.png"></a></li>

			</ol>
		</div>
	</div>

	<footer>
		<div class="container text-center">
			<p>我的网站 版权所有 2008-2016 湘ICP备8888888</p>
			<p>本页面内容为网站演示数据，前台页面内容都可以在后台修改。</p>

			<div class="powered_by_metinfo">Powered&nbsp;by&nbsp; AJAXJS</div>

		</div>
	</footer>
	<button type="button"
		class="btn btn-icon btn-primary btn-squared met-scroll-top hide">
		<i class="icon wb-chevron-up" aria-hidden="true"></i>
	</button>
	
	<!-- CNZZ 统计 -->
	<script src="https://s22.cnzz.com/z_stat.php?id=1261991802&web_id=1261991802"></script>
	<!-- // CNZZ 统计 -->
