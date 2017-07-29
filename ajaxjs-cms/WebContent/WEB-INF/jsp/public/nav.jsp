<%@page pageEncoding="utf-8" import="java.util.Map"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
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
					<a href="http://show.metinfo.cn/muban/ps01701/346/"
						class="navbar-brand navbar-logo vertical-align" title="res017">
						<div class="vertical-align-middle">
							<img src="asset/web/1500623274.jpg" alt="res017" title="res017">
						</div>
					</a>
				</div>
				<div class="collapse navbar-collapse navbar-collapse-toolbar"
					id="example-navbar-default-collapse">

						<style>
						
							.navbar-collapse ul{
							position: relative;
							
							    margin-right: 0;
							    float: right !important;
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