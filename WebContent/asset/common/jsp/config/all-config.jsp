<%@page pageEncoding="UTF-8" import="com.ajaxjs.web.Constant"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<!DOCTYPE html>
<html>
<commonTag:head lessFile="/asset/less/admin.less" title="网站配置" />
<body class="configForm admin-entry-form">
	<h3>全部配置</h3>
	<div class="tree">
		<div class="tooltip tipsNote hide">
			<div class="aj-arrow toLeft"></div>
			<span>用户名等于账号名；不能与现有的账号名相同；注册后不能修改；</span> fdsfdfd
		</div>
	</div>
	<script>
		var a = {
			"site" : {
				"titlePrefix" : "大华官网",
				"keywords" : "大华•川式料理",
				"description" : "大华•川式料理饮食有限公司于2015年成立，本公司目标致力打造中国新派川菜系列。炜爵爷川菜料理系列的精髓在于清、鲜、醇、浓、香、烫、酥、嫩，擅用麻辣。在服务出品环节上，团队以ISO9000为蓝本建立标准化餐饮体系，务求以崭新的姿态面向社会各界人仕，提供更优质的服务以及出品。炜爵爷宗旨：麻辣鲜香椒，美味有诀窍，靓油用一次，精品煮御赐。 ",
				"footCopyright" : "版权所有"
			},
			"dfd" : {
				"dfd" : 'fdsf',
				"id" : 888,
				"dfdff" : {
					"dd" : 'fd'
				}
			},
			"clientFullName" : "大华•川式料理",
			"clientShortName" : "大华",
			"isDebug" : true,
			"data" : {
				"newsCatalog_Id" : 6,
				"jobCatalog_Id" : 7
			},
			"uploadFile" : {
				"MaxTotalFileSize" : {
					name : '',
					type : 'number',
					ui : 'input_text'
				},
				"MaxSingleFileSize" : 1024000,
				"isFileOverwrite" : true,
				"SaveFolder" : "C://temp//",
			}
		};

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

			for ( var i in json) {
				var el = json[i];

				var li = document.createElement('li');
				if (isMap(el)) {
					var div = document.createElement('div'); // parentNode
					div.className = 'parentNode';
					div.innerHTML = '+' + i;
					div.onclick = toggle;
					li.appendChild(div);
					//debugger;
					it(el, fn, li);
				} else {
					li.innerHTML = '<div class="valueHolder"><input type="text" value="' + el + '" data-note="ddddddddd" /></div>'
							+ i;
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
		
		// 遍历每个函数
		function getList(formEl, fn) {
			var list = [];
			function add(el) {
				list.push(el);
			}
			var forEach = [].forEach;
			forEach.call(formEl.querySelectorAll('input[type=text]'), add);
			forEach.call(formEl.querySelectorAll('input[type=password]'), add);
			forEach.call(formEl.querySelectorAll('textarea'), add);

			list.forEach(fn);
		}
		
		var isIn = false;
		function everyInput(input) {
			input.onfocus = function (e) {
				var el = e.currentTarget;
				isIn = true;
				var tipsNote = el.getAttribute('data-note');
				if (tipsNote) {
					var tipsNoteEl = document.querySelector('.tipsNote');
					tipsNoteEl.querySelector('span').innerHTML = tipsNote;
					//tipsNoteEl.style.left = el.offsetLeft + el.offsetWidth + 30 + 'px';
					tipsNoteEl.style.top = (el.offsetTop - 10) + 'px';
					tipsNoteEl.classList.remove('hide');
						
				}
			}
			input.onblur  = function (e) {
				setTimeout(function(){
					if(!isIn)
						document.querySelector('.tipsNote').classList.add('hide');
				}, 5000);
				isIn = false;
			}
		}
		
		getList(tree, everyInput);
	</script>
</body>
</html>