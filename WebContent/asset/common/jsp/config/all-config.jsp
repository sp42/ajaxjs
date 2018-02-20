<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<!DOCTYPE html>
<html>
	<commonTag:head lessFile="/asset/less/admin.less" title="全部配置" />
<body class="configForm admin-entry-form">
	<h3>全部配置</h3>
	<p>请点击有+号菜单项以展开下一级的内容。</p>
	<div class="tree">
		<div class="tipsNote hide">
			<div class="aj-arrow toLeft"></div>
			<span></span>
		</div>
	</div>
	<script>
		var configJson = ${configJson};

		var jsonScheme = ${jsonSchemePath};
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
					var namespaces = getParentStack(stack); // 另外设一个 stack？
					if (namespaces)
						namespaces += '.' + i;
					else 
						namespaces = i;
					
					var scheme = eval('jsonScheme.' + namespaces);
					var tip = makeTip(scheme);
					
					var html;
					switch(scheme.ui) {
						case 'textarea':
							html = '<div class="valueHolder"><textarea></textarea></div>';
							break;
						case 'input_text':
						default:
							html = '<div class="valueHolder"><input name="' + namespaces +'" type="text" value="' + el + '" data-note="' + tip +'" /></div>';	
					}
					li.innerHTML = html + i;
					fn(i, el);
				}
				ul.appendChild(li);
			}
			stack.pop();

			parentEl.appendChild(ul);
		}
		
		// 获取完整的父节点路径
		function getParentStack(stack) {
			var names = [];
			var last;
			for (var i = stack.length; i > 0; i--) {
				last = stack[i];
				if(last){
					var map = stack[i - 1];
					for(var j in map) {
						if(map[j] == last) {
							names.push(j);
						}
					}					
				}
				
			}
			
			return names.reverse().join('.');
		}
		
		// 根据元数据生成说明
		function makeTip(scheme) {
			if(!scheme || !scheme.name) 
				return '';
			
			var html = scheme.name.bold(); 
			html += '<br />' + scheme.tip;
			
			return html;
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
		
		it(configJson, function(item, v) { }, tree);
		
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