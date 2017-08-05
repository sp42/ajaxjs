<%@page pageEncoding="UTF-8"%>  
<%@include file="java.jsp" %>
<head>
<%-- <html lang="zh-cmn-Hans"> --%>
		<meta charset="utf-8" />
	    <meta name="keywords"    content="ajaxjs java js javascript web ui framework html5 ioc aop orm restful" />
	    <meta name="description" content="AJAXJS Web 是一款基于 Java 平台的开源 Web 框架，继承了 Java 平台的高效、安全、稳定、跨平台等诸多优势， 但却摒弃了传统企业级架构所带来的庞大和臃肿，强调轻量级，非常适合互联网小型网站的快速应用。" />
	    <meta name="author"      content="Frank Chueng, frank@ajaxjs.com" />
    	<title>${_config.site_titlePrefix} ${title}</title>
		<noscript>如要享受本网站服务，请您不要禁用  JavaScript 支持。</noscript>
		<%-- pageContext.request.contextPath 作用是取出部署的应用程序名，这样不管如何部署，所用路径都是正确的。 --%>
		<link rel="icon"		  type="image/x-icon" href="${pageContext.request.contextPath}/asset/images/favicon.ico" />
		<link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/asset/images/favicon.ico" />
		
<%
	UA ua = new UA(request);
	// 是否为移动客户端，响应式输出
	// 宽度 320px
	if(ua.isPhone()){
		// iOS 7.1的Safari为meta标签新增minimal-ui属性，在网页加载时隐藏地址栏与导航栏。<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimal-ui" />
%>   
		<meta name="viewport" content="width=320,user-scalable=0,initial-scale=1.0,maximum-scale=1.0, minimum-scale=1.0" />
		<meta name="format-detection" content="telephone=no" />
		<%-- 通过百度手机打开网页时，百度可能会对你的网页进行转码，脱下你的衣服，往你的身上贴狗皮膏药的广告 --%>
		<meta http-equiv="Cache-Control" content="no-siteapp" />
<%	}
	// 兼容旧版 ie
	if(ua.isIE() && ua.is_old_IE()){
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
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/asset/common/css/base.css" />
	<link rel="stylesheet" type="text/css" href="<%=getCssUrl(request, "/ui_demo/public/main.less", true)%>" />
<%-- 	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/ui_demo/public/css.css" /> --%>
    <script src="${pageContext.request.contextPath}/asset/common/js/dom.js"></script>
    <script src="${pageContext.request.contextPath}/asset/common/js/widget.js"></script>
    <script src="${pageContext.request.contextPath}/asset/common/js/list.js"></script>
    <script src="${pageContext.request.contextPath}/ui_demo/public/run_prettify.js"></script>  

	
    <title>AJAXJS UI-${title}</title>