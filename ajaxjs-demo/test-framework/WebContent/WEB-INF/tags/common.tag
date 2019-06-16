<%@tag pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" 		prefix="c" %>
<%@taglib uri="/ajaxjs_config"	prefix="config"%>
<%@taglib tagdir="/WEB-INF/tags/" prefix="tagfile"%>

<%@attribute name="type" required="true" type="String" description="标签类型"%>

<c:if test="${type == 'nav'}">
	<!-- 通用导航条 -->
	<header>
		<div class="logo">
			<img src="${ctx}/asset/images/logo.png" /> 源自法国品牌专业污水提升器
		</div>
		<div class="right">
			<aj-baidu-search></aj-baidu-search>
		</div>
	</header>
	<nav>
		<ul class="">
			<config:siteStru type="nav-bar" />
		</ul>
	</nav>
	<script>
		new Vue({
			el : 'body > header .right'
		});
	</script>
</c:if>



<%@attribute name="bodyClass" required="false" type="String" description="body 标签样式"%>
<%@attribute name="banner" required="false" type="String" description="Banner 图片"%>
<%@attribute name="left" required="false" fragment="true" description="插入左侧菜单内容。如果为空默认输出二级菜单"%>
<%@attribute name="body" required="false" fragment="true" description="插入正文内容"%>
<%@attribute name="showPageHelper" required="false" type="Boolean" fragment="false" description="是否显示分享、调整字体大小"%>

<%-- 通用内容页面 --%>
<c:if test="${type == 'content'}">
	<!DOCTYPE html>
	<html>
	<head>
		<jsp:include page="/WEB-INF/jsp/head.jsp" flush="true" />
	</head>
	<body class="${bodyClass}">
		<tagfile:common type="nav" />
	
		<div class="centerWidth">
			<img src="${banner}" width="100%" style="margin-top: 5px;" />

			<div class="body">
				<div class="left">
					<c:choose>
					    <c:when test="${empty left}">
							<ul>
								<config:siteStru type="secondLevelMenu" />
							</ul>
					    </c:when>
					    <c:otherwise>					      
							<jsp:invoke fragment="left" />
					    </c:otherwise>
					</c:choose>
				</div>
				
				<div class="right">
					<config:siteStru type="breadcrumb" />
					<h2>${PAGE_Node.name}</h2>
					
					<jsp:invoke fragment="body" />
					
					<c:if test="${showPageHelper}">
					<table class="pageHelper" style="float:right;margin:10px 0 20px 0;">
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
					<script>
						new Vue({ el : '.pageHelper' });
						// 百度分享按钮
						window._bd_share_config={"common":{"bdSnsKey":{},"bdText":"","bdMini":"2","bdMiniList":false,"bdPic":"","bdStyle":"1","bdSize":"16"},"share":{"bdSize":16}};with(document)0[(getElementsByTagName('head')[0]||body).appendChild(createElement('script')).src='http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion='+~(-new Date()/36e5)];
					</script>
					</c:if>
				</div>
				
			</div>
		</div>
		
		<tagfile:common type="footer" />
	</body>
	</html>
</c:if>

<%

	// 要写 java 所以不用 jstl tag
	if("footer".equals(jspContext.getAttribute("type"))) {
		
		if (request.getAttribute("requestTimeRecorder") != null) {
			long requestTimeRecorder = (long) request.getAttribute("requestTimeRecorder");
			long end = System.currentTimeMillis() - requestTimeRecorder;
			float _end = (float) end;
			request.setAttribute("requestTimeRecorder", _end / 1000);
		}
		
		request.setAttribute("FOOTER_YEAR", java.util.Calendar.getInstance().get(java.util.Calendar.YEAR));
%>

	<!-- 通用页面底部 -->
	<footer>
		<div class="top">
			<div>
				<div class="btn" onclick="aj('footer .top').classList.toggle('close');"></div>
			</div>
			${SITE_STRU.getSiteMap(pageContext.request)}
		</div>
	
		<div class="copyright">
		<div>
			<div class="right">
<%-- 				<a href="#"> <img src="${commonAsset}/images/gs.png" height="40" /></a> 
				<a href="#"> <img src="${commonAsset}/images/kexin.png" hspace="20" width="90" style="margin-top: 15px;" /></a>  --%>
				<a href="#"> <img src="${commonAsset}/images/360logo.gif" width="90" style="margin-top: 15px;" /></a> 
			</div>
			<a href="javascript:;" onclick="toSimpleChinese(this);" class="simpleChinese selected">简体中文</a> / <a href="javascript:;" class="Chinese" onclick="toChinese(this);">正体中文</a>
			<script src="${ajaxjs_ui_output}/lib/Chinese.js"></script>
			<br /> 
			
			${empty aj_allConfig.site.site_icp ? '粤ICP备15007080号-2' : aj_allConfig.site.site_icp}
			Powered by <a target="_blank" href="http://framework.ajaxjs.com">AJAXJS</a>
			<br />
		
			©Copyright ${FOOTER_YEAR} 版权所有， ${aj_allConfig.clientFullName} &nbsp; ${empty requestTimeRecorder ? '' : '请求完成耗时：'.concat(requestTimeRecorder).concat('秒') }
		</div>
		</div>
	</footer>
<%}%>

