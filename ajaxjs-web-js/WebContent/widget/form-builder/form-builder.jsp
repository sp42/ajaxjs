<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
		<meta charset="utf-8" />
	    <meta name="keywords"    content="广州活映, 活映, CMS,J2EE CMS,内容管理,内容管理系统,网站内容管理系统,品牌,网站建设,网站设计, APP 开发" />
	    <meta name="description" content="品牌 App、网站建设与设计案例-来自活映不畏惧的创新力" />
	    <meta name="author"      content="Frank Chueng, frank@ajaxjs.com" />
		<meta name="renderer"	 content="webkit" /> <meta name="robots" 	 content="index,follow" />
		<meta http-equiv="X-UA-Compatible" content="edge,chrome=1" />
		<title>Form Builder</title>
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
		
		<link rel="stylesheet/less" data-global-vars='{"assetFilePath": "\"/myblog/asset\"" }' type="text/css" href="/ajaxjs-web-js/widget/form-builder/form-builder.less" />
	
	    <script src="http://192.168.1.88:8080/ajaxjs-web-js/js/libs/vue.js"></script>
	    <script src="http://192.168.1.88:8080/ajaxjs-web-js/js/libs/less.min.js"></script>
	    <script src="http://192.168.1.88:8080/ajaxjs-web-js/js/ajaxjs-base.js"></script>
	    <script src="${pageContext.request.contextPath}/js/libs/code-prettify.min.js"></script>  
	    
	    <script>
	   		aj.Vue = {};
	   		aj.Vue.install = function(Vue) {
	   			Vue.prototype.ajResources = {
		   			ctx : '/shop',
		   			commonAsset : '/shop/asset/common',
		   			libraryUse  : '/shop/asset/common/resources' // 庫使用的資源
	   			};
	   			
	   			Vue.prototype.BUS = new Vue();
	   		}
	   		
	   		Vue.use(aj.Vue);
	   		
	   		window.addEventListener('load', function() { // 页面渐显效果
				document.body.classList.add('active');
			});
	   	</script>
		<link rel="icon"		  type="image/x-icon" href="../../asset/images/favicon.ico" />
		<link rel="shortcut icon" type="image/x-icon" href="../../asset/images/favicon.ico" />
		<noscript><div align="center">如要享受本网站之服务请勿禁用浏览器 JavaScript 支持</div></noscript>
	</head>

    <body>
    	<div class="components">
    		<h3 class="title">Components</h3>
    		<ul>
    			<li><img src="asset/icon/label.png" />Label</li>
    			<li><img src="asset/icon/inputText.png" />Text Field</li>
    			<li><img src="asset/icon/button.png" />Button</li>
    			<li><img src="asset/icon/radio.png" />Radio</li>
    			<li><img src="asset/icon/checkbox.png" />CheckBox</li>
    			<li><img src="asset/icon/textarea.png" />Textarea</li>
    			<li><img src="asset/icon/label.png" />Label</li>
    		</ul>
    	</div>
    	<div class="center">
	    	<header>
	    		<h3 class="title" style="padding: .7%;font-weight:bold">Form Builder</h3>
	    		<div>
	    			<span>
	    				<a href="#" @click="toggleCodeMode" v-bind:class="{active:!codeMode}">Design</a> | <a href="#" @click="toggleCodeMode" v-bind:class="{active:codeMode}">Code</a>
	    			</span>
	    			模版路径 <input type="text" class="aj-input" /> <button>打开</button> <button><img :src="ajResources.commonAsset + '/icon/save.gif'" /> 保存</button>
	    		</div>
	    	</header>
	    	
    		<div class="stage admin-entry-form" >
		    	
		    	<form v-show="!codeMode" @click="onStageClk">
					<div>
						<label>
							<div contenteditable="true" class="label">名 称：</div> 
							<input placeholder="请填写${uiName}名称" size="60" required="required" name="name" value="${info.name}" type="text" />
						</label> 
					</div>
					<div>
						<label>
							<div contenteditable="true" class="label">名 称：</div> 
							<input placeholder="请填写766名称" size="60" required="required" name="name22" value="${info.name}" type="text" />
						</label> 
					</div>
				</form>
				
				
				<div class="codeEdit" v-show="codeMode">
					<pre class="prettyprint">var menuPanel = el.parentNode;
menuPanel.style.display = 'none';
setTimeout(function(){
  menuPanel.style.display = '';
}, 500);</pre>
				</div>
    		</div>
    	</div>
    	
    	<div class="right">
    		<!-- 属性修改面板 -->
    		<div class="dataSource">
    			<h3 class="title">Data Source</h3>
    			<label>表名：
	    			<select class="aj-select" @change="tableSelect($event)">
						<option>不指定</option>
						<option v-for="v in tables">{{v}}</option>
					</select>
    			</label>
    			<ul>
    				<li v-for="v in fields"><div>{{v.name}}</div><div class="comment">{{v.comment}}</div></li>
    			</ul>
    		</div>

    		<!-- 属性修改面板 -->
    		<div class="bottom">
	   			<div class="property">
					<h3 class="title">Property Editor</h3>
					<table>
						<tr><td class="key">id</td>			<td><input type="text" /></td></tr>
						<tr><td class="key">名称 name</td>	<td><input type="text" v-model="name" /></td></tr>
						<tr><td class="key">提示</td>			<td><input type="text" v-model="placeHolder" /></td></tr>
						<tr><td class="key">值 value</td>		<td><input type="text" /></td></tr>
						<tr><td class="key">值类型</td>		<td>
							<select class="aj-select">
								<option>不指定</option>
								<option>纯数字</option>
								<option></option>
							</select>
						</td></tr>
						<tr><td class="key">必填 required</td><td><input type="checkbox" /></td></tr>
						<tr><td class="key">正则验证</td>		<td><input type="text" /></td></tr>
					</table>
					<div class="bottom"></div>
				</div>
    		</div>
    	</div>

		<template class="propertyTpl">
			<div class="property">
				<h3 class="title">Property Editor</h3>
				<table>
					<tr><td class="key">id</td>			<td><input type="text" /></td></tr>
					<tr><td class="key">名称 name</td>	<td><input type="text" v-model="name" /></td></tr>
					<tr><td class="key">提示</td>			<td><input type="text"  /></td></tr>
					<tr><td class="key">值 value</td>		<td><input type="text" /></td></tr>
					<tr><td class="key">值类型</td>		<td>
						<select class="aj-select">
							<option>不指定</option>
							<option>纯数字</option>
							<option></option>
						</select>
					</td></tr>
					<tr><td class="key">必填 required</td><td><input type="checkbox" /></td></tr>
					<tr><td class="key">正则验证</td>		<td><input type="text" /></td></tr>
				</table>
				<div class="bottom"></div>
			</div>
		</template>
		
		
		<!-- 状态栏 -->
		<div class="statusBar" v-bind:class="{hideMe:!show}">{{text}}</div>
		
		

		<script>
			FB={};
			FB.statusBar = new Vue({
				el:'.statusBar',
				data: {
					show : false,
					text:'',
					timer:null
				},
				mounted() {
					this.showMsg('欢迎来到 From Builder');
				},
				methods:{
					showMsg(text) {
						this.text = text;
						this.show = true;
						if(this.timer) {
							clearTimeout(this.timer);
						}
						this.timer = setTimeout(()=>this.show = false, 4000);
					}
				}
			});
			
		</script>
		<script src="form-builder.js"></script>
		<script src="center.js"></script>
		<script src="property-editor.js"></script>
    </body>
</html>