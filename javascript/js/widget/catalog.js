// 双亲节点分类结构

/**
 * 下拉列表控件
 * @param {Object} json 输入的数据
 * @param {Number} TOP_ID 顶级 id，如果你想限制某一分类，而不是全部分类的出现，可以指定一个 父id，默认是 -1
 * @param {Number} selected_id 可选，如果指定了 id，则会默认显示那个 id
 */

function CatalogSelectList(json, TOP_ID, selected_id) {
	var ids = this.init(json);
	
	var tree = [];
	for (var i in ids) {
		if(ids[i].parentId === TOP_ID) // 一级父节点
			tree.push(ids[i]);
	}

	var data = []; this.data = data;
	// 扁平化，形成一维数组
	this.flat(tree, 0);
	
	this.selected_id = selected_id;
	this.render(data);
}

CatalogSelectList.prototype.init = function (json) {
	var ids = {};
	
	// id 作为索引
	for(var i = 0, j = json.length; i < j; i++){
	    ids[json[i].id] = json[i];
	}
	
	// 链接到 父节点
	for(var i = 0, j = json.length; i < j; i++){
		var item = json[i];
		var parentNode = ids[item.parentId]; // 先找到父节点在哪里，找到的话，将自己保存到父节点的 children 数组里面
		if(!parentNode){
			// 找不到 父id 表示为顶级
			continue; 
		}
	
		if(!parentNode.children) {// 如果没有先创建
			parentNode.children = [];
		}
		parentNode.children.push(item);// 保存自己
	}
	
	return ids;
}

CatalogSelectList.prototype.flat = function(arr, deep) {
	 for(var i = 0, j = arr.length; i < j; i++) {
		 var item = arr[i];
		 item.deep = deep; // 记录深度
		 this.data.push(item);
		 if(item.children && item.children.length > 0) // 递归
			 arguments.callee.call(this, item.children, deep + 1);
	 }
}

CatalogSelectList.prototype.render = function(data) {
	// 渲染 DOM
	var option = '<option value="{0}" {1}>{2}</option>';
	
	document.writeln('<select name="catalog" required="required">');
	for(var i = 0, j = data.length; i < j; i++) {
		var html = '';

		if(data[i].deep == 0) {
		}else if(data[i].deep > 0) {
			html += (new Array(data[i].deep * 4)).join('&nbsp;'); // 缩进
			html += '└';
		}
		html += data[i].name;// 名称字段

		document.writeln(option.format(data[i].id, this.selected_id == data[i].id ? 'selected' : '', html));
	}
	document.writeln('</select>');
}