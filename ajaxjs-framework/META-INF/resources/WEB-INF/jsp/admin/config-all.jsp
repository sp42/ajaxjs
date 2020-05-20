<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/WEB-INF/jsp/head.jsp">
			<jsp:param name="lessFile" value="/asset/less/admin.less" />
			<jsp:param name="title" value="全局配置" />
		</jsp:include>
		<style>
			form.aj-form{
			
			}
		</style>
	</head>
<body>
	<div>	
		<ajaxjs-admin-header>
			<template slot="title">全局配置：<span>请点击有+号菜单项以展开下一级的内容</span></template>
		</ajaxjs-admin-header>
	</div>
	
	<p class="aj-note">不推荐在该界面中修改配置。小白用户请谨慎修改配置参数！</p>
	<form>
		<button onclick="save();return false;" class="aj-btn" style="margin-left: 30px;">保存</button>
		
		<div class="all-config-classic">
			<div class="tipsNote hide">
				<div class="aj-arrow toLeft"></div>
				<span></span>
			</div>
		</div>
	
	</form>
	
	<script>
		new Vue({el: 'body>div'});
		var configJson = ${configJson};
		var jsonScheme = ${jsonSchemePath};
	</script>
	
	<script>
// https://github.com/jojoin/tppl/blob/gh-pages/tppl.js
tppl = function(tpl, data) {
    var fn =  function(d) {
        var i, k = [], v = [];
        for (i in d) {
            k.push(i);
            v.push(d[i]);
        };
        return (new Function(k, fn.$)).apply(d, v);
    };
    if(!fn.$){
        var tpls = tpl.split('[:');
        fn.$ = "var $=''";
        for(var t = 0;t < tpls.length;t++){
            var p = tpls[t].split(':]');
            if(t!=0){
                fn.$ += '='==p[0].charAt(0)
                  ? "+("+p[0].substr(1)+")"
                  : ";"+p[0].replace(/\r\n/g, '')+"$=$"
            }
            // 支持 <pre> 和 [::] 包裹的 js 代码
            fn.$ += "+'"+p[p.length-1].replace(/\'/g,"\\'").replace(/\r\n/g, '\\n').replace(/\n/g, '\\n').replace(/\r/g, '\\n')+"'";
        }
        fn.$ += ";return $;";
        // log(fn.$);
    }
    return data ? fn(data) : fn;
}


var tree = aj('.all-config-classic');

function isMap(v) {
	return typeof v == 'object' && v != null;
}

var stack = [];
function it(json, fn, parentEl) {
	stack.push(json);
	var ul = document.createElement('ul');
	ul.style.paddingLeft = ((stack.length - 1) * 10) + "px";

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
			// debugger;
			it(el, fn, li);
		} else {
			var namespaces = getParentStack(stack); // 另外设一个 stack？
			
			if (namespaces)
				namespaces += '.' + i;
			else
				namespaces = i;

			try {
				var scheme = eval('jsonScheme.' + namespaces);
				var tip = makeTip(scheme);
			} catch (e) {
				// 有时 jsonScheme 没有对应的
				var scheme = {
					ui : 'input_text'
				};
				var tip = '';
			}

			var html = '<div class="valueHolder">';

			if (!scheme) { // 没有说明，忽略
				continue;
			}

			switch (scheme.ui) {
			case 'textarea':
				html += '<textarea name="' + namespaces + '">' + el + '</textarea>';
				break;
			case 'htmlEditor':
				// html += document.querySelector('.tpl').value;
				break;
			case 'checkbox':
				html += renderCheckbox(scheme, namespaces, tip, el);
				break;
			case 'radio':
				html += renderRadio(scheme, namespaces, tip, el);
				break;
			case 'select':
				html += renderSelect(scheme, namespaces, tip, el);
				break;
			case 'input_text':
			default:
				html += '<input name="' + namespaces + '" type="text" value="' + el + '" data-note="' + tip + '" />';
			}

			html += ('</div>' + (scheme.name || '') + ' ' + i);
			li.innerHTML = html;

			// if (scheme.ui == 'htmlEditor')
			// initHtmlEditor(li, el, namespaces);

			fn(i, el);
		}

		ul.appendChild(li);
	}
	stack.pop();

	parentEl.appendChild(ul);
}

function getRealValue(v) {
	switch (v) {
	case 'null':
	case 'true':
	case 'false':
		return eval(v);
	}

	if ((Number(v) + "") == v)
		return Number(v);

	return v;
}

function isChecked(value, itemValue) {
	return (value & itemValue) == itemValue;
}

// 获取完整的父节点路径
function getParentStack(stack) {
	var names = [];
	var last;

	for (var i = stack.length; i > 0; i--) {
		last = stack[i];
		if (last) {
			var map = stack[i - 1];
			for ( var j in map) {
				if (map[j] == last) 
					names.push(j);
			}
		}
	}

	return names.reverse().join('.');
}

// 根据元数据生成说明
function makeTip(scheme) {
	if (scheme && scheme.name && scheme.tip)
		return scheme.name.bold() + '<br />' + scheme.tip;
	else
		return "";
}

function toggle(e) {
	var div = e.target;
	var ul = div.parentNode.$('ul');

	if (ul.style.height == '0px') {
		div.innerHTML = div.innerHTML.replace('+', '-');
		ul.style.height = ul.scrollHeight + 'px';
		setTimeout(() => {
			ul.style.height = 'auto'; // a ticky
		}, 500);
	} else {
		div.innerHTML = div.innerHTML.replace('-', '+');
		ul.style.height = '0px';
	}
}


// 遍历每个函数
function getList(formEl, fn) {
	var list = [];
	function add(el) {
		list.push(el);
	}
	var forEach = [].forEach;
	forEach.call(formEl.querySelectorAll('input'), add);
	forEach.call(formEl.querySelectorAll('select'), add);
	// forEach.call(formEl.querySelectorAll('input[type=password]'), add);
	forEach.call(formEl.querySelectorAll('textarea'), add);

	list.forEach(fn);
}

var isIn = false;
function everyInput(input) {
	input.onfocus = function(e) {
		// debugger;
		var el = e.currentTarget;
		isIn = true;
		var tipsNote = el.dataset.note;// el.getAttribute('data-note');

		if (tipsNote) {
			var tipsNoteEl = document.querySelector('.tipsNote');
			tipsNoteEl.querySelector('span').innerHTML = tipsNote;
			// tipsNoteEl.style.left = el.offsetLeft + el.offsetWidth + 30 +
			// 'px';
			tipsNoteEl.style.top = (el.offsetTop - 10) + 'px';
			tipsNoteEl.classList.remove('hide');
		}
	}
	input.onblur = function(e) {
		setTimeout(() => {
			if (!isIn)
				document.querySelector('.tipsNote').classList.add('hide');
		}, 5000);
		isIn = false;
	}
}

if (window.configJson) 
	it(configJson, function(item, v) {}, tree);

if (window.tree)
	getList(tree, everyInput);

// 保存原 form 表单数据
var oldData;
setTimeout(() => {
	oldData = {};
	new FormData(aj('form')).forEach((value, key) => {
		oldData[key] = value;
	});
}, 200);

function getDiffent() {
	var needsTo8421 = [ 'user.login.loginType', 'user.login.passWordLoginType' ]; // 写死
	for (var i = 0, j = needsTo8421.length; i < j; i++)
		code8421(needsTo8421[i]);

	var htmlEditorTextareas = document.querySelectorAll('.htmlEditorTextarea');
	// debugger;
	for (var i = 0, j = htmlEditorTextareas.length; i < j; i++) {
		var htmlEditorTextarea = htmlEditorTextareas[i];
		htmlEditorTextarea.value = htmlEditorTextareas[0].previousElementSibling.contentWindow.document.body.innerHTML;
	}

	var different = {};
	var formData = new FormData(aj('form'));

	formData.forEach((value, key) => {
		if (oldData[key] != value) 
			different[key] = value;
	});

	return different;
}

function save() {
	var data = getDiffent();

	if (Object.keys(data).length)
		ajaxjs.xhr.post('?', json => json && json.isOk && aj.alert.show('修改配置成功！'), data);
	else 
		aj.alert.show('没任何修改');
}

function code8421(key) {
	var arr = document.querySelectorAll('input[data-name="' + key + '"]');
	var total = 0;
	for (var i = 0, j = arr.length; i < j; i++) {
		if (arr[i].checked) 
			total += Number(arr[i].value);
	}

	aj('input[name="' + key + '"]').value = total;
}


function renderRadio(scheme, namespaces, tip, value) {
	var t = '[: for (var i=0;i<list.length;i++) { :]\
		<label><input type="radio" value="[:=list[i].value:]" name="[:=list[i].name:]" data-note="[:=list[i].dataNote:]" [:=list[i].checked ? "checked" : "":] />\
		[:=list[i].text:] </label>\
		[:}:]';
	
	var arr = [];
	if(!scheme.option)
		scheme.option = ['true=是', 'false=否'];
	
	for(var i = 0, j = scheme.option.length; i < j; i++) {
		var item = scheme.option[i];
		var _arr = item.split('=');
		var checked = value == getRealValue(_arr[0]); // 是否选中？

		arr.push({
			name : namespaces,
			value: _arr[0],
			text : _arr[1],
			dataNote: tip,
			checked : checked
		});
	}
	
	return tppl(t, {
		list : arr
	});
}

function renderCheckbox(scheme, namespaces, tip, value) {
	// 有一隐藏域是 保存 value 的
	var checkboxTpl = '<input type="hidden" name="[:=namespaces:]" value="[:=totalValue:]" />\
		[: for (var i=0;i<list.length;i++) { :]\
		<label><input type="checkbox" value="[:=list[i].value:]" data-name="[:=list[i].name:]" data-note="[:=list[i].dataNote:]" [:=list[i].checked ? "checked" : "":] />\
			[:=list[i].text:]</label> \
		[:}:]';
	var arr = [];
	for(var i = 0, j = scheme.option.length; i < j; i++) {
		var item = scheme.option[i];
		var _arr = item.split('=');
		var checked = isChecked(value, getRealValue(_arr[0])); // 是否选中？

		arr.push({
			name : namespaces,
			value: _arr[0],
			text : _arr[1],
			dataNote: tip,
			checked : checked
		});
	}
	
	return tppl(checkboxTpl, {
		namespaces:namespaces,
		totalValue : value,
		list : arr
	});
}

// 初始化 HTML 编辑器
function initHtmlEditor(li, value, namespaces) {
 	setTimeout(() => {
		var htmlEditor = new ajaxjs_HtmlEditor(li.$('.htmlEditor'));
	
		htmlEditor.setValue(value);
		htmlEditor.sourceEditor.name = namespaces;
		htmlEditor.sourceEditor.value = value;
	}, 0);
}

function renderSelect(scheme, namespaces, tip, value) {
	var tpl = '<select name="[:=namespaces:]" data-note="[:=dataNote:]" />\
		[: for (var i=0;i<list.length;i++) { :]\
		<option value="[:=list[i].value:]" [:=list[i].checked ? "selected" : "":]>\
			[:=list[i].text:]</option> \
		[:}:]</select>';

	
	var arr = [];
	for(var i = 0, j = scheme.option.length; i < j; i++) {
		var item = scheme.option[i];
		var _arr = item.split('=');
		var checked = value == getRealValue(_arr[0]); // 是否选中？

		arr.push({
			value: _arr[0],
			text : _arr[1],
			checked : checked
		});
	}
	
	return tppl(tpl, {
		namespaces:namespaces,
		dataNote: tip,
		list : arr
	});
}
	</script>
</body>
</html>