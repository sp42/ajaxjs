<%-- <%@page pageEncoding="utf-8" import="java.util.Map"%> --%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<!--[if lte IE 8]>
	<div class="text-center padding-top-50 padding-bottom-50 bg-blue-grey-100">
	<p class="browserupgrade font-size-18">你正在使用一个<strong>过时</strong>的浏览器。请<a href="http://browsehappy.com/" target="_blank">升级您的浏览器</a>，以提高您的体验。</p>
	</div>
<![endif]-->
	<nav class="navbar navbar-default met-nav navbar-fixed-top" role="navigation">
		<div class="container">
			<div class="row">
				<div class="navbar-header">
					<button type="button"
						class="navbar-toggle hamburger hamburger-close collapsed"
						data-target="#example-navbar-default-collapse"
						data-toggle="collapse">
						<span class="sr-only">Toggle navigation</span> <span
							class="hamburger-bar"></span>
					</button>
					<a href="http://show.metinfo.cn/muban/ps01701/346/" class="navbar-brand navbar-logo vertical-align" title="res017">
						<div class="vertical-align-middle">
							<img src="${pageContext.request.contextPath}/asset/web/1500623274.jpg" alt="" title="">
						</div>
					</a>
				</div>
				<div class="collapse navbar-collapse navbar-collapse-toolbar" id="example-navbar-default-collapse">

						<style>
							.banner{
								margin:0 auto;
							}
							.article p{
								line-height:300%;
								font-size:16px;
							}
							.bodyWidth{
								max-width:1300px;
								margin:0 auto;
							}
							.navbar-collapse ul{
								position: relative;
							    margin-right: 0;
							    float: right !important;
							}
							.menu{
								border-bottom:1px solid lightgray;
								overflow: hidden;
							}
							
							.menu ul {
								display: flex;
								-webkit-justify-content: center;
								justify-content: center;
							
							}
							.menu  li {
								float:left;
								height:50px;
								line-height: 50px;
								text-align:center;
								width: 150px;
							}
							.navbar-collapse li {
								position: relative;
								display: block;
								float: left;
							}
							
							.navbar-collapse li a {
								padding: 0 25px;
								color: #696969 !important;
								height: 60px;
								line-height: 60px;
							}
						</style>
						<commonTag:page type="navMenu" />
				</div>
			</div>
		</div>
	</nav>