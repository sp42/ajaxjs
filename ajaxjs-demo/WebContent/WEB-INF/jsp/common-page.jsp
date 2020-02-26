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
			<table class="vue" style="float:right;margin:10px 0 20px 0;">
				<tr>
					<td>
						<!-- 百度分享按钮 http://share.baidu.com/ -->
						<div class="bdsharebuttonbox">
							<a href="#" class="bds_more" data-cmd="more">分享到：</a>
							<a href="#" class="bds_qzone" data-cmd="qzone" title="分享到QQ空间">QQ空间</a>
							<a href="#" class="bds_tsina" data-cmd="tsina" title="分享到新浪微博">新浪微博</a>
							<a href="#" class="bds_tqq" data-cmd="tqq" title="分享到腾讯微博">腾讯微博</a>
							<a href="#" class="bds_renren" data-cmd="renren" title="分享到人人网">人人网</a>
							<a href="#" class="bds_weixin" data-cmd="weixin" title="分享到微信">微信</a>
						</div> 
						<!-- // -->
					</td>
					<td>
						<aj-adjust-font-size></aj-adjust-font-size>
					</td>
				</tr>
			</table>
		<!-- Editable AREA|END -->
		</article>
	</jsp:attribute>
</tags:content>
