<%@tag pageEncoding="UTF-8" description="输出头部文件" body-content="scriptless" trimDirectiveWhitespaces="true"%>
<%@attribute name="title" 		required="false" description="其他标题"%> 
<%@attribute name="lessFile" 	required="false" description="指定 LESS 样式文件"%> 
<jsp:useBean id="PAGE" class="com.ajaxjs.web.HtmlHead" scope="request" /> 
<%
	request.setAttribute("commonAsset", request.getContextPath() + "/asset/common/"); // 静态资源目录
	request.setAttribute("commonAssetIcon", request.getAttribute("commonAsset").toString() + "images/icon/");
	PAGE.init(request);
%>
<head>
	<meta charset="utf-8" />
    <meta name="keywords"    content="${all_config.site.keywords}" />
    <meta name="description" content="${all_config.site.description}" />
    <meta name="author"      content="Frank Chueng, frank@ajaxjs.com" />
	<%-- 优先使用 IE 最新版本和 Chrome --%>
	<%-- 360 使用Google Chrome Frame --%>
	<meta name="renderer"	 content="webkit" />
	<%-- 定义网页搜索引擎索引方式，robotterms 是一组使用英文逗号「,」分割的值，通常有如下几种取值：none，noindex，nofollow，all，index和follow --%>
	<meta name="robots" 	 content="index,follow" />
	<meta http-equiv="X-UA-Compatible" content="edge,chrome=1" />
	<title>${all_config.site.titlePrefix}&#10;${PAGE.node.name}&#10;${title}</title>

<%
	// 是否为移动客户端，响应式输出宽度 320px
	if(PAGE.getUa().isPhone()) {
		// iOS 7.1的Safari为meta标签新增minimal-ui属性，在网页加载时隐藏地址栏与导航栏。<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimal-ui" />
%>   
		<meta name="viewport" content="width=320, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0" />
		<meta name="format-detection" content="telephone=no" />
		<%-- 通过百度手机打开网页时，百度可能会对你的网页进行转码，脱下你的衣服，往你的身上贴狗皮膏药的广告 --%>
		<meta http-equiv="Cache-Control" content="no-siteapp" />
<%	}
	
	// 兼容旧版 ie
	if(PAGE.getUa().isIE() && PAGE.getUa().is_old_IE()) {
		response.setHeader("X-UA-Compatible","IE=EmulateIE8");
%>
		<meta http-equiv="X-UA-Compatible" content="edge" />
<%
	}
%>
	<style type="text/css">
		/* AJAXJS Base CSS */
		body,dl,dt,dd,ul,li,pre,form,fieldset,input,p,blockquote,th,td,h1,h2,h3,h4,h5{margin:0;padding:0;}
		h1,h2,h3,h4,h5{font-weight: normal;}img{border:0;}ul li{list-style-type:none}.hide{display:none}
		body{
			-webkit-font-smoothing:antialiased;
			-moz-osx-font-smoothing: grayscale;
			font-family: "Lantinghei SC", "Open Sans", Arial, "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", "STHeiti", "WenQuanYi Micro Hei", SimSun, sans-serif;
		}
		
		a{
			text-decoration:none;
			color:#666;
			transition : color 400ms ease-in-out;
		}
		
		a:hover{
			color:black;
			text-decoration:underline;
		}
		
		button {
			border:none;
			outline: none;
			cursor: pointer;
			letter-spacing: 2px;
			text-align: center;
			-webkit-user-select: none; /* 不可选择文本 */
			-moz-user-select: none;
			user-select: none;
		}
		select, input[type=text], input[type=password], textarea {
			outline:none;
			-moz-appearance:none;/* for mac */
		}
		
		/* 手机端浏览器所显示的网页 CSS */
		@media screen and (max-width:480px) {
			* {
				-webkit-tap-highlight-color: transparent; /* 很多 Android 浏览器的 a 链接有边框，这里取消它  */
				-webkit-touch-callout: none; /* 在 iOS 浏览器里面，假如用户长按 a 标签，都会出现默认的弹出菜单事件 */
				/* -webkit-user-select:none; */ /* !!! */
			}
		}	
	</style>
	<link rel="stylesheet" type="text/css" href="${PAGE.getCssUrl(lessFile)}" />

    <script src="${pageContext.request.contextPath}/asset/common/js/dom.js"></script>
    <script src="${pageContext.request.contextPath}/asset/common/js/widget.js"></script>
	<%-- pageContext.request.contextPath 作用是取出部署的应用程序名，这样不管如何部署，所用路径都是正确的。 --%>
	<link rel="icon"		  type="image/x-icon" href="${pageContext.request.contextPath}/asset/images/favicon.ico" />
	<link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/asset/images/favicon.ico" />
    <jsp:doBody />
</head>
<noscript><div align="center">如要享受本网站之服务请勿禁用浏览器 JavaScript 支持</div></noscript>