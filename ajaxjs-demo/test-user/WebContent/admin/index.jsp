<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="height: 100%;">
	<head>
		<meta charset="utf-8" />
	    <meta name="keywords"    content="广州活映, 活映, CMS,J2EE CMS,内容管理,内容管理系统,网站内容管理系统,品牌,网站建设,网站设计, APP 开发" />
	    <meta name="description" content="品牌 App、网站建设与设计案例-来自活映不畏惧的创新力" />
	    <meta name="author"      content="Frank Chueng, frank@ajaxjs.com" />
		<meta name="renderer"	 content="webkit" /> <meta name="robots" 	 content="index,follow" />
		<meta http-equiv="X-UA-Compatible" content="edge,chrome=1" />
		<title>藏经阁&#10;&#10;管理</title>
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
	
		<link rel="stylesheet" type="text/css" href="admin.css" />
	
	    <script src="vue.min.js"></script>
	    <script src="all.js"></script>
		<script>
	   		aj.Vue = {};
	   		aj.Vue.install = function(Vue) {
	   			Vue.prototype.ajResources = {
		   			ctx : '/myarticle',
		   			commonAsset : '/myarticle/asset/common',
		   			libraryUse  : '/myarticle/asset/common/resources' // 庫使用的資源
	   			};
	   			
	   			Vue.prototype.BUS = new Vue();
	   		}
	   		
	   		Vue.use(aj.Vue);
	   		
	   		window.addEventListener('load', function() { // 页面渐显效果
				document.body.classList.add('active');
			});
	   	</script>
		<noscript><div align="center">如要享受本网站之服务请勿禁用浏览器 JavaScript 支持</div></noscript>
	</head>
	
	<body class="admin-shell">
		<header>
			<h1>藏经阁 后台管理系统</h1>
			<menu>
				admin 已登录 | <a href="/myarticle/" target="_blank">首页</a> | <a href="/myarticle/user/center/home/" target="iframepage">后台首页</a> 
				| <a href="javascript:logout();">退出</a> 
			</menu>
		</header>
		<section class="side">
			<div class="rightTop_title">Welcome</div>
			<div class="rightTop"></div>
			<div class="closeBtn" onclick="hideSider(this);"></div>
			<span class="menu">
				<aj-accordion-menu>
					<li>
						<h3>网站管理</h3>
						<ul>
							<li><a target="iframepage" href="/myarticle/admin/config/siteStru/">网站结构</a></li>
							<li><a target="iframepage" href="/myarticle/admin/page_editor/">页面管理</a></li>
						</ul>
					</li>
						<li>
						<h3>全局数据</h3>
						<ul>
							<li><a target="iframepage" href="/myarticle/admin/config/site/">项目信息</a></li>
							<li><a target="iframepage" href="/myarticle/admin/config/">所有配置</a></li>
						</ul>
					</li>
					<li> 
						<h3>用户管理</h3>
						<ul>
							<li><a target="iframepage" href="/myarticle/admin/user/list/">用户管理</a></li>
							<li><a target="iframepage" href="/myarticle/admin/userLoginLog/">登录日志</a></li>
							<li><a target="iframepage" href="/myarticle/admin/userGlobalLog/">操作日志</a></li>
						<li><a target="iframepage" href="/myarticle/admin/user/privilege/">用户-权限管理</a></li>
							<li><a target="iframepage" href="/myarticle/admin/user/user_group/">用户组管理</a></li>
							<li><a target="iframepage" href="/myarticle/user/register/">用户注册</a></li>
						</ul>
					</li> 
					<li>
						<h3>开发工具</h3>
						<ul>
							<li><a target="iframepage" href="/myarticle/admin/DataBase/">数据库管理</a></li>
							<li><a target="iframepage" href="/myarticle/admin/CodeGenerators/">代码生成器</a></li>
							<li><a target="iframepage" href="/myarticle/asset/ajaxjs-ui/doc">前端文档</a></li>
							<li><a target="iframepage" href="/myarticle/admin/DataBaseShowStru">表字段浏览</a></li>
							<li><a target="iframepage" href="/myarticle/admin/GlobalLog/">操作日志浏览</a></li>
							<li><a target="iframepage" href="/myarticle/asset/admin/tomcat-log.jsp">后台日志浏览</a></li>
							<li><a target="iframepage" href="/myarticle/jsp/swagger-ui/">API 文档</a></li>
						</ul>
					</li>
					<li>
						<h3>个人信息</h3>
						<ul>
							<li><a  target="iframepage" href="/myarticle/user/center/info/">个人信息</a></li>
							<li><a  target="iframepage" href="/myarticle/user/center/loginInfo/">账户安全</a></li>
							<li><a href="javascript:logout();">退出登录</a></li>
						</ul>
					</li>
				</aj-accordion-menu>
			</span>
		</section>
		<section class="iframe">
			<iframe src="/myarticle/user/center/home" name="iframepage"></iframe>
		</section>
	    <script>
	    
	    	function logout() {
	    		confirm('确定退出吗？') && aj.xhr.get('/myarticle/user/login/logout', json => {
	  				if(json.isOk) {
	  					aj.msg.show(json.msg);
	  					setTimeout(()=>location.assign('/myarticle/admin/login/'), 1000);
	  				}
	    		});
	    	}
	    	
		 	// 判断当前页面是否在iframe中 
		    if (self != top) {    
		    	parent.window.location.reload();
		    }  
		 	
		    // 收缩菜单
		    function hideSider(el) {
		    	if(hideSider.isHidden) {
		    		aj('.side').style.width   = '20%';
		    		aj('.iframe').style.width = '80%';
		    		hideSider.isHidden = false;
		    		el.style.right = '0'; 
		    	}else {
		    		aj('.side').style.width   = '0';
		    		aj('.iframe').style.width = '100%';
		    		hideSider.isHidden = true;
		    		el.style.right = '-'+ el.clientWidth +'px';
		    	}				
		    }
	
		    // 初始化菜单
		    new Vue({el: '.menu'});
	
		    // 需要把链接配置属性  target="iframepage"
		    ;(function () {
		    	// 获取 # target=abc 参数
		    	function getTarget() {
		    		var target = window.location.hash.match(/target=([^$]+)/);
		    		return target && target.pop();
		    	}
		    	
		    	// 展开菜单
		    	function highlightMenu(target) {
		    		var a = document.querySelector('a[href="' + target + '"]');
		    		if (a) {        	
		    			var li = a.up('li'), ul = li.up('ul');
		    			li.classList.add('selected');
		    			ul.style.height = 'auto';
		    			ul.up('li').classList.add('pressed');
		    		}
		    	}
		    	
		    	var prefix = location.origin + "", iframepageName = 'iframepage';
		    	
		        var target = getTarget();
		        var iframeEl = aj('iframe[name=' + iframepageName + ']');
		        
		        if(target) {
		        	iframeEl.src = target; // 跳转 iframe
		        	highlightMenu(target);
		        }
	
		        // 点击菜单时保存按钮
		    	aj('a[target=' + iframepageName + ']', function(a) {
		            a.onclick = add_Hash;
		        });
		        
		        function add_Hash(e) {
		        	var target = new String(iframeEl.contentWindow.location);
		        	var target = e.target.getAttribute('href');
	
		        	window.location.assign('#target=' + target); // 为主窗体添加描点记录，以便 F5 刷新可以回到这里
		            //alert(99)
		        	return false; // onhashchange() 里面已经跳转了，这里避免 a 再次跳转
		        }
		        
		        window.onhashchange = function(e) {
		        	var target = getTarget();
		        	iframeEl.src = target;
	//	        	highlightMenu(target);
		        }
		    })();
	    		    
	    </script>
	</body>
</html>