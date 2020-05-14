// https://github.com/jojoin/tppl/blob/gh-pages/tppl.js
tppl = function(tpl, data){
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