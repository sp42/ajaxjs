<%@tag pageEncoding="UTF-8" description="输出头部文件" body-content="scriptless" import="com.ajaxjs.web.UserAgent, com.ajaxjs.web.HtmlHead"%>
<%@tag trimDirectiveWhitespaces="true"%>
<%@attribute name="title" 		required="false" description="其他标题"%> 
<%@attribute name="lessFile" 	required="false" description="指定 LESS 样式文件"%> 
<%-- <jsp:useBean id="ua" class="com.ajaxjs.web.UserAgent" /> --%>
<head>
<%-- <html lang="zh-cmn-Hans"> --%>
		<meta charset="utf-8" />
	    <meta name="keywords"    content="${_config.site_keywords}" />
	    <meta name="description" content="${_config.site_description}" />
	    <meta name="author"      content="Frank Chueng, frank@ajaxjs.com" />
    	<title>${_config.site_titlePrefix} ${title}</title>
		<noscript>如要享受本网站服务，请您不要禁用  JavaScript 支持。</noscript>
		<%-- pageContext.request.contextPath 作用是取出部署的应用程序名，这样不管如何部署，所用路径都是正确的。 --%>
		<link rel="icon"		  type="image/x-icon" href="${pageContext.request.contextPath}/asset/images/favicon.ico" />
		<link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/asset/images/favicon.ico" />
	    
	    <style type="text/css">
			body,dl,dt,dd,ul,li,pre,form,fieldset,input,p,blockquote,th,td,h1,h2,h3,h4,h5{margin:0;padding:0;}
			img{border:0;vertical-align:top}ul li{list-style-type:none}.hide{display:none}
			body{
				-webkit-font-smoothing:antialiased;
				font-family:"Microsoft YaHei","ff-tisa-web-pro-1","ff-tisa-web-pro-2","Lucida Grande","Hiragino Sans GB","Hiragino Sans GB W3",Arial;
			}
			*{	
				<%-- 很多Android 浏览器的 a 链接有边框，这里取消它  --%>
				-webkit-tap-highlight-color: rgba(0, 0, 0, 0); 
				<%-- 在IOS浏览器里面，假如用户长按a标签，都会出现默认的弹出菜单事件  --%>  
				-webkit-touch-callout: none;    			
			}
		</style>
		
<%
	UserAgent ua = new UserAgent(request);

	// 是否为移动客户端，响应式输出
	// 宽度 320px
	if(ua.isPhone()) {
		// iOS 7.1的Safari为meta标签新增minimal-ui属性，在网页加载时隐藏地址栏与导航栏。<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimal-ui" />
%>   
		<meta name="viewport" content="width=320,user-scalable=0,initial-scale=1.0,maximum-scale=1.0, minimum-scale=1.0" />
		<meta name="format-detection" content="telephone=no" />
		<%-- 通过百度手机打开网页时，百度可能会对你的网页进行转码，脱下你的衣服，往你的身上贴狗皮膏药的广告 --%>
		<meta http-equiv="Cache-Control" content="no-siteapp" />
<%	}
	// 兼容旧版 ie
	if(ua.isIE() && ua.is_old_IE()) {
		response.setHeader("X-UA-Compatible","IE=EmulateIE8");
%>
		<meta http-equiv="X-UA-Compatible" content="edge" />
<%
	}
%>
	<%-- 通过百度手机打开网页时，百度可能会对你的网页进行转码，脱下你的衣服，往你的身上贴狗皮膏药的广告 --%>
	<meta http-equiv="Cache-Control" content="no-siteapp" />
	<%-- 优先使用 IE 最新版本和 Chrome --%>
	<meta http-equiv="X-UA-Compatible" content="edge,chrome=1" />
	<%-- 360 使用Google Chrome Frame --%>
	<meta name="renderer" content="webkit" />
	
	<%-- 定义网页搜索引擎索引方式，robotterms 是一组使用英文逗号「,」分割的值，通常有如下几种取值：none，noindex，nofollow，all，index和follow --%>
	<meta name="robots" content="index,follow" />
	<%-- request.setAttribute("isDebug", Init.isDebug); --%>
	<%-- <link rel="stylesheet" type="text/css" href="${_config.LessUrlProcessor.getCssUrl(pageContext.request, lessFile, isDebug)}" /> --%>
	
	<%
		// 是否处于调试模式
		boolean isDebug = true;
	
		if(request.getServletContext().getAttribute("isDebug") != null) {
			isDebug = (Boolean)request.getServletContext().getAttribute("isDebug");
		} 
	%>
	<link rel="stylesheet" type="text/css" href="<%=HtmlHead.getCssUrl(request, lessFile, isDebug)%>" />
    <script src="${pageContext.request.contextPath}/asset/js/dom.js"></script>

    <jsp:doBody />
</head>