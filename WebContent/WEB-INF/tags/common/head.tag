<%@tag pageEncoding="UTF-8" description="输出 HTML 头部 head 元素的标签" body-content="scriptless" trimDirectiveWhitespaces="true"%>
<%@attribute name="title" 		required="false" description="其他标题"%> 
<%@attribute name="lessFile" 	required="false" description="指定 LESS 样式文件"%> 
<jsp:useBean id="PAGE" class="com.ajaxjs.web.HtmlHead" scope="request" /> 
<%
	PAGE.init(request);
%>
<head>
	<meta charset="utf-8" />
    <meta name="keywords"    content="${all_config.site.keywords}" />
    <meta name="description" content="${all_config.site.description}" />
    <meta name="author"      content="Frank Chueng, frank@ajaxjs.com" />
	<meta name="renderer"	 content="webkit" /> <%-- 360 优先使用 Google Chrome Frame 和最新版本 IE --%>
	<meta name="robots" 	 content="index,follow" />
	<meta http-equiv="X-UA-Compatible" content="edge,chrome=1" />
<%
	// 是否为移动客户端，响应式输出宽度 320px
	if(PAGE.getUa().isPhone()) {
%>   
		<meta name="viewport" content="width=320, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0" />
		<meta name="format-detection" content="telephone=no" />
		<meta http-equiv="Cache-Control" content="no-siteapp" /><%-- 通过百度手机打开网页时，百度可能会对你的网页进行转码，脱下你的衣服，往你的身上贴狗皮膏药的广告 --%>
<%	}
	
	// 兼容旧版 ie
	if(PAGE.isOldIE()) {
		response.setHeader("X-UA-Compatible","IE=EmulateIE8");
%>
		<meta http-equiv="X-UA-Compatible" content="edge" />
<%
	}
%>
	<title>${all_config.site.titlePrefix}&#10;${PAGE.node.name}&#10;${title}</title>
	<style type="text/css">
		/* AJAXJS Base CSS */
		body,dl,dt,dd,ul,li,pre,form,fieldset,input,p,blockquote,th,td,h1,h2,h3,h4,h5{margin:0;padding:0;}
		h1,h2,h3,h4,h5{font-weight: normal;}img{border:0;}ul li{list-style-type:none}.hide{display:none}
		body {-webkit-font-smoothing:antialiased;-moz-osx-font-smoothing: grayscale;
			font-family: "Lantinghei SC", "Open Sans", Arial, "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", "STHeiti", "WenQuanYi Micro Hei", SimSun, sans-serif;}
		a{text-decoration:none;color:#666;transition:color .4s ease-in-out}
		a:hover{color:#000;text-decoration:underline}
		button{border:none;outline:0;cursor:pointer;letter-spacing:2px;text-align:center;-webkit-user-select:none;-moz-user-select:none;user-select:none}
		input[type=password],input[type=text],select,textarea{outline:0;-moz-appearance:none}
		
		/* 手机端浏览器所显示的网页 CSS */
		@media screen and (max-width:480px) {
			* {
				-webkit-tap-highlight-color: transparent; /* 很多 Android 浏览器的 a 链接有边框，这里取消它  */
				-webkit-touch-callout: none; /* 在 iOS 浏览器里面，假如用户长按 a 标签，都会出现默认的弹出菜单事件 */
				/* -webkit-user-select:none; */ /* !!! */
			}
		}	
	</style>
<%-- 	<link rel="stylesheet" type="text/css" href="${PAGE.getCssUrl(lessFile)}" /> --%>
<% if(PAGE.isDebug()) { %>
	<link rel="stylesheet/less" type="text/css" href="${pageContext.request.contextPath}${empty lessFile ? '/asset/less/main.less' : lessFile}" />
		
	<script>
	  less = {
	    env: "development",
	    globalVars: {
	      assetFilePath: '"<%=request.getContextPath()%>/asset"'   
	    }
	  };
	</script>
	<script src="${pageContext.request.contextPath}/asset/common/js/libs/less.min.js"></script>
<%}else { %>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/asset/css/${empty lessFile ? 'main' : lessFile.replaceAll("(?:.*/)(\\w+).less", "$1")}.css" />
<%} %>
    <script src="${commonAsset}js/ajaxjs-base.js"></script>
    <script src="${commonAsset}js/ajaxjs-list.js"></script>
    <script src="${commonAsset}js/ajaxjs-ui.js"></script>
	<link rel="icon"		  type="image/x-icon" href="${pageContext.request.contextPath}/asset/images/favicon.ico" />
	<link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/asset/images/favicon.ico" />
    <jsp:doBody />
</head>
<noscript><div align="center">如要享受本网站之服务请勿禁用浏览器 JavaScript 支持</div></noscript>