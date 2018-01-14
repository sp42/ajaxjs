<%@page pageEncoding="UTF-8" import="com.ajaxjs.web.Constant"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<!DOCTYPE html>
<html>
<commonTag:head lessFile="/asset/less/admin.less" title="网站结构" />
<body class="configForm admin-entry-form">
	<h3>网站结构</h3>
	<div class="tree">
		<div class="tooltip tipsNote hide">
			<div class="aj-arrow toLeft"></div>
			<span>用户名等于账号名；不能与现有的账号名相同；注册后不能修改；</span> fdsfdfd
		</div>
	</div>
	<script>
		var a = [ {
			'name' : "关于我们",
			'id' : 'about',
			'children' : [ 
				{
					name : "公司历程",
					id : 'history'
				},
				{
					name : "企业文化",
					id : 'cluture'
				}
			]
		}, {
			'name' : "产品中心",
			'id' : 'product',
			'children' : [ 
				{
					name : "最新美食",
					id : 'new',
					'children' : [
						{
							'id' : 'yuecai',
							'name' : '粤菜'
						},
						{
							'id' : 'yuecai',
							'name' : '湘菜'
						}
					]
				},
				{
					name : "热门菜谱",
					id : 'hot'
				}
			]
		}, 
		{
			'name' : "最新资讯",
			'id' : 'news'
		}, 
		{
			'name' : "招聘信息",
			'id' : 'hr'
		}, {
			'name' : "联系我们",
			'id' : 'contact'
		}];

		var tree = document.querySelector('.tree');

		function isMap(v) {
			return typeof v == 'object' && v != null;
		}

		var stack = [];
		function it(json, fn, parentEl) {
			stack.push(json);
			var ul = document.createElement('ul');
			ul.style.paddingLeft = (stack.length * 10) + "px";

			// 折叠
			if (stack.length != 1) {
				ul.className = 'subTree';
				ul.style.height = '0';
			}

			for ( var i = 0, j = json.length; i < j; i++) {
				var el = json[i];

				var li = document.createElement('li');
				if (el.children) {
					var div = document.createElement('div'); // parentNode
					div.className = 'parentNode';
					div.innerHTML = '+' + el.name;
					div.onclick = toggle;
					li.appendChild(div);
					//debugger;
					
					//for(var p = 0, q = el.children.length; p < q; p++) {
						it(el.children, fn, li);
					//}
				} else {
					li.innerHTML = '<div class="valueHolder"><input type="text" value="' + el.name + '" data-note="ddddddddd" /></div>' + el.id;
					fn(i, el);
				}
				ul.appendChild(li);
			}
			stack.pop();

			parentEl.appendChild(ul);
		}

		function toggle(e) {
			var div = e.target;
			var ul = div.parentNode.querySelector('ul');

			if (ul.style.height == '0px') {
				div.innerHTML = div.innerHTML.replace('+', '-');
				ul.style.height = ul.scrollHeight + 'px';
				setTimeout(function() {
					ul.style.height = 'auto'; // a ticky
				}, 500);
			} else {
				div.innerHTML = div.innerHTML.replace('-', '+');
				ul.style.height = '0px';
			}
		}
		
		it(a, function(item, v) {
			console.log(item + ':' + stack.length);
		}, tree);
		
	</script>
</body>
</html>