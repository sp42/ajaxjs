<%@tag pageEncoding="UTF-8" description="输出头部文件" body-content="scriptless" import="java.util.regex.Pattern"%>
<%
	/**
	 * 浏览器 UA 检测
	 * @author frank
	 *
	 */
	class UA {
		/**
		 * 送入一个浏览器 UA 标识字符串
		 * 
		 * @param ua
		 *            UA 标识字符串
		 */
		public UA(String ua) {
			this.ua = ua;
		}
	
		/**
		 * 送入一个请求
		 * 
		 * @param request
		 *            请求对象
		 */
		public UA(HttpServletRequest request) {
			this.ua = getUA_String(request);
		}
		
		private String ua;
	
		/**
		 * 获取请求的 UA
		 * 
		 * @return 浏览器 UA 标识
		 */
		private String getUA_String(HttpServletRequest request) {
			String ua = request.getHeader("User-Agent");
			return ua != null ? ua.toLowerCase() : null; // 强制转换为小写字母
		}
	
		/**
		 * 是否 IE 浏览器
		 * 
		 * @return true 表示为 IE 浏览器
		 */
		public boolean isIE() {
			return ua.contains("msie");
		}
	
		/**
		 * IE 8 或其以下的皆为旧版 IE
		 * 
		 * @return true 表示为旧 IE 浏览器
		 */
		public boolean is_old_IE() {
			return isIE() && (ua.contains("msie 5.5") || ua.contains("msie 6.0")
						   || ua.contains("msie 7.0") || ua.contains("msie 8.0"));
		}
		
		/**
		 * 是否为 Firefox 浏览器
		 * 
		 * @return true 表示为 Firefox 浏览器
		 */
		public boolean isFireFox() {
			return ua.contains("firefox");
		}
	
		/**
		 * 是否为 Chrome 浏览器
		 * 
		 * @return true 表示为 Chrome 浏览器
		 */
		public boolean isChrome() {
			return ua.contains("chrome");
		}
		
		/**
		 * 是否为安卓
		 * 
		 * @return true 表示为 Android 浏览器
		 */
		public boolean isAndroid() {
			return ua.contains("android");
		}
	
		/**
		 * 是否为安卓 5.x
		 * 
		 * @return true 表示为 Android 5.x 浏览器
		 */
		public boolean isAndroid_5() {
			boolean is5 = Pattern.compile("Android\\s5", Pattern.CASE_INSENSITIVE).matcher(ua).find();
			return isAndroid() && is5;
		}
		
		/**
		 * 是否为安卓 4.x
		 * 
		 * @return true 表示为 Android 4.x 浏览器
		 */
		public boolean isAndroid_4() {
			boolean is4 = Pattern.compile("Android\\s4", Pattern.CASE_INSENSITIVE).matcher(ua).find();
			return isAndroid() && is4;
		}
		
		/**
		 * 是否为安卓 2.x
		 * 
		 * @return true 表示为 Android 2.x 浏览器
		 */
		public boolean isAndroid_2() {
			boolean is2 = Pattern.compile("Android\\s2", Pattern.CASE_INSENSITIVE).matcher(ua).find();
			return isAndroid() && is2;
		}
		
		/**
		 * 是否为安卓 2.2.x
		 * 
		 * @return true 表示为 Android 2.2.x 浏览器
		 */
		public boolean isAndroid_2_2() {
			boolean is2_2 = Pattern.compile("Android\\s2\\.2", Pattern.CASE_INSENSITIVE).matcher(ua).find();
			return isAndroid() && is2_2;
		}
		
		/**
		 * 是否为 iPhone
		 * 
		 * @return true 表示为 iPhone Safari 浏览器
		 */
		public boolean isIPhone() {
			return ua.contains("iphone");
		}
		
		/**
		 * 是否为 iPad
		 * 
		 * @return true 表示为 iPad Safari 浏览器
		 */
		public boolean isIPad() {
			return ua.contains("ipad");
		}
		
		/**
		 * 是否为 iOS 系统
		 * 
		 * @return true 表示为 iOS 系统
		 */
		public boolean isIOS() {
			return isIPad() || isIPhone();
		}
		
		/**
		 * 是否为 WinPhone 手机
		 * 
		 * @return true 表示为 WinPhone 手机
		 */
		public boolean isWinPhone() {
			return ua.contains("windows phone");
		}
		
		/**
		 * 是否为手机
		 * 
		 * @return true 表示为手机
		 */
		public boolean isPhone() {
			return isAndroid() || isIOS() || isWinPhone();
		}
	}
%>
<%@tag trimDirectiveWhitespaces="true"%>
<%@attribute name="title" 		required="false" description="其他标题"%> 
<%@attribute name="lessFile" 	required="false" description="指定 LESS 样式文件"%> 
<head>
<%-- <!doctype html> --%>
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
	<% request.setAttribute("isDebug", com.ajaxjs.app.Init.isDebug); %>
	<link rel="stylesheet" type="text/css" href="${_config.LessUrlProcessor.getCssUrl(pageContext.request, lessFile, isDebug)}" />
    <script src="${pageContext.request.contextPath}/asset/bigfoot/js/dom.js"></script>

    <jsp:doBody />
</head>