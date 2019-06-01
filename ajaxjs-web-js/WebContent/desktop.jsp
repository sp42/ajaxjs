<%@page pageEncoding="UTF-8"%>
<%@include file="tools.jsp" %>
<!DOCTYPE html>
<html>
    <head>
		<meta charset="utf-8" />
	    <meta name="keywords"    content="广州活映, 活映, CMS,J2EE CMS,内容管理,内容管理系统,网站内容管理系统,品牌,网站建设,网站设计, APP 开发" />
	    <meta name="description" content="品牌 App、网站建设与设计案例-来自活映不畏惧的创新力" />
	    <meta name="author"      content="Frank Chueng, frank@ajaxjs.com" />
		<meta name="renderer"	 content="webkit" /> <meta name="robots" 	 content="index,follow" />
		<meta http-equiv="X-UA-Compatible" content="edge,chrome=1" />
		<title>博客&#10;&#10;</title>
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
		
		<link rel="stylesheet/less" data-global-vars='{"assetFilePath": "\"/myblog/asset\"" }' type="text/css" href="/ajaxjs-web-js/less/desktop.less" />
	
		<script src="/myblog/ajaxjs-ui-output/lib/less.min.js"></script>
	    <script src="http://192.168.1.88:8080/ajaxjs-web-js/js/libs/vue.js"></script>
	    <script src="http://192.168.1.88:8080/ajaxjs-web-js/js/ajaxjs-base.js"></script>
	    <script src="http://192.168.1.88:8080/ajaxjs-web-js/js/widgets/admin.js"></script>
	    <script src="http://192.168.1.88:8080/ajaxjs-web-js/js/widgets/form.js"></script>
	    <script src="http://192.168.1.88:8080/ajaxjs-web-js/js/widgets/list.js"></script>
	    <script src="http://192.168.1.88:8080/ajaxjs-web-js/js/widgets/menu.js"></script>
	    <script src="http://192.168.1.88:8080/ajaxjs-web-js/js/widgets/msg.js"></script>
	    <script src="http://192.168.1.88:8080/ajaxjs-web-js/js/widgets/page.js"></script>
	    <script src="http://192.168.1.88:8080/ajaxjs-web-js/js/widgets/tree.js"></script>
	    <script src="http://192.168.1.88:8080/ajaxjs-web-js/js/widgets/upload.js"></script>
	    <script src="http://192.168.1.88:8080/ajaxjs-web-js/js/widgets/carousel.js"></script>
	    <script src="http://192.168.1.88:8080/ajaxjs-web-js/js/widgets/marquee.js"></script>
	    <script src="http://192.168.1.88:8080/ajaxjs-web-js/js/widgets/img.js"></script>
	    
	    <script>
	   		aj.Vue = {};
	   		aj.Vue.install = function(Vue) {
	   			Vue.prototype.ajResources = {
		   			ctx : '/myblog',
		   			commonAsset : '/myblog/asset/common',
		   			libraryUse  : '/myblog/asset/common/resources' // 庫使用的資源
	   			};
	   			
	   			Vue.prototype.BUS = new Vue();
	   		}
	   		
	   		Vue.use(aj.Vue);
	   		
	   		window.addEventListener('load', function() { // 页面渐显效果
				document.body.classList.add('active');
			});
	   	</script>
		<link rel="icon"		  type="image/x-icon" href="/myblog/asset/images/favicon.ico" />
		<link rel="shortcut icon" type="image/x-icon" href="/myblog/asset/images/favicon.ico" />
		<noscript><div align="center">如要享受本网站之服务请勿禁用浏览器 JavaScript 支持</div></noscript>
	</head>

    <body>
		<div class="stage">
			<aj-window></aj-window>
		</div>
		<menu>
			<div class="button"><div class="glare"></div>开始</div>
			<ul>
				<li></li>
			</ul>
		</menu>
		
		<div class="mask"></div>
		
		<script src="desktop.js"></script>
    </body>
</html>