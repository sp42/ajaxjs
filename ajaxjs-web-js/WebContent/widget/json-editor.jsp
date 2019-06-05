<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
		<meta charset="utf-8" />
	    <meta name="keywords"    content="JSON 编辑器" />
	    <meta name="description" content="JSON 编辑器" />
	    <meta name="author"      content="Frank Chueng, frank@ajaxjs.com" />
		<meta name="renderer"	 content="webkit" /> <meta name="robots" 	 content="index,follow" />
		<meta http-equiv="X-UA-Compatible" content="edge,chrome=1" />
		<title>JSON 编辑器</title>
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
		
		<link rel="stylesheet/less" data-global-vars='{"assetFilePath": "\"/myblog/asset\"" }' type="text/css" href="/ajaxjs-web-js/widget/json-editor.less" />
	
	
   		<script src="http://192.168.1.88:8080/ajaxjs-web-js/js/libs/vue.js"></script>
	    <script src="http://192.168.1.88:8080/ajaxjs-web-js/js/libs/less.min.js"></script>
	    <script src="http://192.168.1.88:8080/ajaxjs-web-js/js/ajaxjs-base.js"></script>

	    
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
    	<h1>JSON Editor</h1>
    	<div class="container">
    		<h3>ROOT</h3>
	    	<ul class="tree parent"></ul>
    	</div>
    	
    	<div class="form">
	    	<fieldset class="aj-fieldset">
	    		<legend>点击节点编辑</legend>
	    		<table>
	    			<tr>
	    				<td width="100">路径</td><td>{{path}}</td>
	    			</tr>
	    			<tr>
	    				<td>键/索引</td><td><input type="text" v-model="key" class="aj-input" /></td>
	    			</tr>
	    			<tr>
	    				<td>值</td><td><input type="text" v-model="value" class="aj-input" v-bind:class="{'ajaxjs-disable': !canEditValue}" /></td>
	    			</tr>
	    			<tr>
	    				<td>值类型</td><td>{{valueTypeText}}</td>
	    			</tr>
	    			<tr>
	    				<td>是否树叶</td><td>{{isLeaf?'是':'否'}}</td>
	    			</tr>
	    			
	    			<tr><Td></Td><td>
	    				<button></button>
	    			</td></tr>
	    		</table>
	    	</fieldset>
	    	
	    	<br />
	    	<br />
	    	<fieldset class="aj-fieldset">
	    		<legend>粘贴你的 JSON</legend>
	    		<textarea class="aj-input" v-model="inputJson" style="width:90%;height:100px;margin: 5%;"></textarea>
	    	</fieldset>
	    	<br />
	    	<br />
	    	<h3>用法说明：</h3>
	    	<br />
	    	<ul class="note">
	    		<li>
	    		“树叶 Leaf” 表示没用下级的节点，或是表示单个值，没用下一级的可能，以区别于 Object 和 Array，例如数组里面的 ["a", 123, true, {...}, [...]]，除了最后两者不是叶子之外，其余都是树叶。</li>
	    		<li>代码出自 <a href="https://framework.ajaxjs.com">AJAXJS Web 前端框架</a>，可脱离框架单独使用</li>
	    		<li>编码风格采用了 ES5，但可以稍作修改降级到 ECMAScript 3.0</li>
	    		<li>免费使用，亦可有偿制定化，请联系 sp42@qq.com</li>
	    		<li>TODO：日期类型，通过鼠标拖放移动数组元素</li>
	    	</ul>
    	</div> 

		<script>
			VUE = new Vue({
				el: '.form',
				data : {
					jsonData : null,
					path : '',
					key:'',
					value : null,
					valueType: null,
					valueTypeText: '',
					isLeaf:null,
					canEditValue: true,
					inputJson:''
				},
				watch :{
					valueType(v){
						switch(v) {
							case Object:
								this.valueTypeText = '普通对象';
							break;
							case Array:
								this.valueTypeText = '数组';
							break;
							case String:
								this.valueTypeText = '字符串';
							break;
							case Number:
								this.valueTypeText = '数字';
							break;
							case Boolean:
								this.valueTypeText = '布尔';
							break;
							default:
								this.valueTypeText = '未知类型';
						}
					},
					inputJson(v) {
						if(v && v.trim()) {
							this.jsonData = JSON.parse(v);
							aj('.tree').innerHTML = '';
							everyJsonArr(this.jsonData);
							console.log('done')
						}
					}
				}
			});
		</script>
		
		<script src="simpleData.js"></script>
		<script src="json-editor.js"></script>
    </body>
</html>