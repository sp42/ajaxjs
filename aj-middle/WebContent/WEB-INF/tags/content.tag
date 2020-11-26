<%@tag pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" 		prefix="c"%>
<%@taglib uri="/ajaxjs_config"	prefix="config"%>
<%@taglib tagdir="/WEB-INF/tags/" prefix="tagfile"%>

<%@attribute name="left" 		required="false" fragment="true" description="插入左侧菜单内容"%>
<%@attribute name="body" 		required="false" fragment="true" description="插入正文内容"%>
<%@attribute name="bannerText" 	required="false" type="String" description="Banner 文字"%>
<%@attribute name="bannerImg" 	required="false" type="String" description="Banner 图片"%>
<%@attribute name="bodyClass" 	required="false" type="String" description="body 标签样式"%>
<%@attribute name="showPageHelper" 	required="false" type="Boolean" description="页面助手"%>

<!DOCTYPE html>
<html>
<head>
	<!-- 通用头部 -->
	<%@include file="/WEB-INF/jsp/head.jsp"%>
</head>

<body class="aj ${bodyClass}">
<%--
<%if(ua.isPhone()) { %>
	<script>
		alert('手机版适配中，请浏览我们的PC电脑版或者微信公众号');
	</script>
<%} %>
 --%>
	<header>
		<div>
			<div style="float:right">
				<config:logined>
		            <a href="${ctx}/user/">${userName} 已登录</a>
				</config:logined>
				<config:notLogined> 
					会员：<a href="${ctx}/user/register/">注册</a> | <a href="${ctx}/user/login/">登录</a>
				</config:notLogined>
				&nbsp;&nbsp;&nbsp;
			
				 语言：<a href="javascript:aj.alert('English Version is coming.<br />英语版本敬请期待');">English</a> / 
				 <a href="javascript:;" onclick="toSimpleChinese(this);" class="simpleChinese selected">简中</a> / 
				 <a href="javascript:;" class="Chinese" onclick="toChinese(this);">正体中文</a>
			 </div>
			 技术支持/咨询QQ：<a href="javascript:location.assign('tencent://message/?uin=799887651&Site=活映信息&Menu=yes');">799887651</a> 
			 联系电话： 137-1122-8150 Email: 
			 <a onclick="this.href=this.href.replace(/\[--at--\]/, String.fromCharCode(64));" href="mailto:frank[--at--]gz88.cc">
				fran@gz88.cc
			</a>
		 </div>
	</header>
	<div class="main">
		<nav>
			<ul>
				<config:siteStru type="navBar" />
			</ul>
			<script>
				function showMenu(){
					var el = document.querySelector('.moblieTop1Menu ul');
					var d = getComputedStyle(el).display;
					el.style.display = d == 'none' ? 'block' : 'none';
				}
			</script>
			<div class="moblieTop1Menu">
				<button class="btn" onclick="showMenu();">Menu</button>
				<ul>
					<config:siteStru type="navBar" />
				</ul>
			</div>
			<a href="${ctx == '' ? '/' : ctx}"><img class="logo" src="${ctx}/asset/images/logo.png" /></a>
			<span>专注于小微企业工作流信息化应用</span>
		</nav>

		<c:if test="${not empty bannerText}">
			<div class="inner" style="">
			  	<div class="banner">
			  		<div class="l-1"></div>
			  		<div class="l-2"></div>
			  		<div class="l-3">
			  			<h2>${bannerText}</h2>
			  		</div>
			  	</div>
			  	<div class="content">
			  		<menu>
						<ul style="width: 100%;">
							<config:siteStru type="secondLevelMenu" />
						</ul>
						
						<jsp:invoke fragment="left"/>
						
				        <form action="${ctx}/global_search/" style="text-align:center;margin-top:10%;">
							<input type="text" name="q" placheholder="请输入标题或正文关键字" size="16" class="aj-input" />
							<br>
							<button style="margin-top:8%;" class="aj-btn">全站搜索</button>
						</form>
			  		</menu>
			  		
			  		<div>
			  			<config:siteStru type="breadCrumb" />
			  			
			  			<jsp:doBody />
			  			
			  			<br />
			  			<br />
						<c:if test="${showPageHelper}">		
							<table class="aj-pageHelper" style="float:right;margin:10px 0 20px 0;">
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
								new Vue({ el : '.aj-pageHelper' });
								window._bd_share_config={"common":{"bdSnsKey":{},"bdText":"","bdMini":"2","bdMiniList":false,"bdPic":"","bdStyle":"1","bdSize":"16"},"share":{"bdSize":16}};with(document)0[(getElementsByTagName('head')[0]||body).appendChild(createElement('script')).src='https://ajaxjs.nos-eastchina1.126.net/bs/static/api/share.js?v=89860593.js?cdnversion='+~(-new Date()/36e5)];
							</script>
						</c:if>
			  			<br />
			  			<br />
			  		</div>
			  	</div>
			</div>
		</c:if>
		
		<c:if test="${empty bannerText}">
			<jsp:doBody />
		</c:if>
		
	</div>
	
 	<div class="moblieMenu">
		<div class="left">
			<h2>${SITE_STRU.getSecondLevelNode(pageContext.request).name}</h2>
			<ul style="width: 100%;">
				<config:siteStru type="secondLevelMenu" />
			</ul>
		</div>
		<div class="right" onclick="arguments[0].target.parentNode.classList.toggle('hover');">二级菜单</div>
	</div>
	
	<!-- 页脚 -->
	<tagfile:common type="footer" />
	<tagfile:wechat type="share" imgUrl="https://www.ajaxjs.com/asset/images/smallLogo.png" />
</body>
</html>