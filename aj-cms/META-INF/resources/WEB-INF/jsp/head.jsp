<%
	request.setAttribute("PAGE_Node", com.ajaxjs.framework.config.SiteStruService.getPageNode(request));
	com.ajaxjs.web.UserAgent ua = new com.ajaxjs.web.UserAgent(request);
%>
	<meta charset="utf-8" />
    <meta name="keywords"    content="${aj_allConfig.site.keywords}" />
    <meta name="description" content="${aj_allConfig.site.description}" />
    <meta name="author"      content="Frank Chueng, frank@ajaxjs.com" />
	<%-- 360 优先使用 Google Chrome Frame 和最新版本 IE --%>
	<meta name="renderer"	 content="webkit" /> 
	<meta name="robots" 	 content="index,follow" />
	
<%
	if(ua.isPhone()) { // 是否为移动客户端，响应式输出宽度 320px
%>   
		<meta name="viewport" content="width=320, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0" />
		<meta name="format-detection" content="telephone=no" />
		<meta http-equiv="Cache-Control" content="no-siteapp" />
		<%-- 通过百度手机打开网页时，百度可能会对你的网页进行转码，脱下你的衣服，往你的身上贴狗皮膏药的广告 --%>
<%	}
	
	if(ua.isOldIE()) {// 兼容旧版 ie
		response.setHeader("X-UA-Compatible","IE=EmulateIE8");
%>
		<meta http-equiv="X-UA-Compatible" content="edge,chrome=1" />
<%
	}
%>
	<title>${aj_allConfig.site.titlePrefix}&#10;${PAGE_Node.name}&#10;${param.title}</title>
	<style type="text/css">
		/* AJAXJS Base CSS */
		body,dl,dt,dd,ul,li,pre,form,fieldset,input,p,blockquote,th,td,h1,h2,h3,h4,h5{margin:0;padding:0;}
		h1,h2,h3,h4,h5{font-weight: normal;}img{border:0;}ul li{list-style-type:none}.hide{display:none}
		body {-webkit-font-smoothing:antialiased;-moz-osx-font-smoothing: grayscale;
			font-family: "Lantinghei SC", "Open Sans", Arial, "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", "STHeiti", "WenQuanYi Micro Hei", SimSun, sans-serif;}
		a{text-decoration:none;color:#666;transition:color .4s ease-in-out;}
		a:hover{color:#000;}
		button{border:none;outline:0;cursor:pointer;letter-spacing:2px;text-align:center;-webkit-user-select:none;-moz-user-select:none;user-select:none}
		input[type=password],input[type=text],select,textarea{outline:0;-moz-appearance:none;}
		
		/* 手机端浏览器所显示的网页 CSS */
		@media screen and (max-width:480px) {
			* {
				-webkit-tap-highlight-color: transparent; /* 很多 Android 浏览器的 a 链接有边框，这里取消它  */
				-webkit-touch-callout: none; /* 在 iOS 浏览器里面，假如用户长按 a 标签，都会出现默认的弹出菜单事件 */
				/* -webkit-user-select:none; */
			}
		}	
	</style>
	
    <link href="https://lib.baomitu.com/font-awesome/4.7.0/css/font-awesome.css" rel="stylesheet" />
	
<% if(com.ajaxjs.Version.isDebug) { %>
	<link rel="stylesheet" href="${aj_static_resource}/dist/css/all.css" />
	<link rel="stylesheet" href="${aj_static_resource}/dist/css/website/${aj_allConfig.site.appId}.css" /> 
	<script crossorigin="anonymous" integrity="sha512-YXLGLsQBiwHPHLCAA9npZWhADUsHECjkZ71D1uzT2Hpop82/eLnmFb6b0jo8pK4T0Au0g2FETrRJNblF/46ZzQ==" src="//lib.baomitu.com/vue/2.6.12/vue.js"></script>
    <script src="http://localhost:8888/dist/base.js"></script>
    <script src="http://localhost:8888/dist/widget.js"></script>
    <script src="http://localhost:8888/dist/form.js"></script>
    <script src="http://localhost:8888/dist/list.js"></script>
    <script src="http://localhost:8888/dist/misc.js"></script> 
<%}else { %>
	<link rel="stylesheet" href="${aj_static_resource}/dist/css/all.min.css" />
	<link rel="stylesheet" href="${aj_static_resource}/dist/css/website/${aj_allConfig.site.appId}.min.css" /> 
	<script crossorigin="anonymous" integrity="sha512-BKbSR+cfyxLdMAsE0naLReFSLg8/pjbgfxHh/k/kUC82Hy7r6HtR5hLhobaln2gcTvzkyyehrdREdjpsQwy2Jw==" src="//lib.baomitu.com/vue/2.6.12/vue.min.js"></script>    
	<script src="${aj_static_resource}/dist/all.min.js"></script>
<%} %>

   	<script>
   		aj.ctx = '${ctx}';
 
   		Vue.use(function(Vue) {
   			Vue.prototype.ajResources = {
	   			ctx: '${ctx}',
	   			imgPerfix: '${aj_allConfig.uploadFile.imgPerfix}', // 图片云存储前缀
	   			commonAsset: '${commonAsset}',  
	   			commonAssetIcon: '${commonAssetIcon}'
   			};
   			
 	   		Vue.prototype.BUS = new Vue();
   	   	});
   		
   		window.addEventListener('load', function() { // 页面渐显效果
			document.body.classList.add('active');
		});
   	</script>
   	
	<link rel="icon"		  type="image/x-icon" href="${ctx}/asset/images/favicon.ico" />
	<link rel="shortcut icon" type="image/x-icon" href="${ctx}/asset/images/favicon.ico" />
	<noscript><div>如要享受本网站之服务请勿禁用浏览器 JavaScript 支持</div></noscript>