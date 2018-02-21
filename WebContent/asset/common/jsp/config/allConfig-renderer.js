
function renderRadio(scheme, namespaces, tip, value) {
	var t = '[: for (var i=0;i<list.length;i++) { :]\
		<label><input type="radio" value="[:=list[i].value:]" name="[:=list[i].name:]" data-note="[:=list[i].dataNote:]" [:=list[i].checked ? "checked" : "":] />\
		[:=list[i].text:] </label>\
		[:}:]';
	
	var arr = [];
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
	var colorPickerHTML = ajaxjs_HtmlEditor.createColorPickerHTML();
	var colorPicker = li.querySelector('.fontColor.colorPicker');
	colorPicker.innerHTML = colorPickerHTML;
	var colorPicker = li.querySelector('.bgColor.colorPicker');
	colorPicker.innerHTML = colorPickerHTML;

	setTimeout(function() {
		var htmlEditor = new ajaxjs_HtmlEditor(li.querySelector('.htmlEditor'));
	
		htmlEditor.setValue(value);
		htmlEditor.sourceEditor.name = namespaces;
		htmlEditor.sourceEditor.value = value;
	});
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