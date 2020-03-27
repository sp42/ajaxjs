var stack = [];

var map = {
	a : 1,
	b : 2,
	c : {
		children : [ {
			d : 3
		} ]
	}
};

function findParent(map, id) {
	for ( var i in map) {
		if (i == id)
			return map[i];
		var c = map[i].children;
		if (c) {
			for (var q = 0, p = c.length; q < p; q++) {
				var result = findParent(c[q], id);
				if (result != null)
					return result;
			}

		}
	}

	return null;
}

var stack = [];
function output(map, cb) {
	stack.push(map);
	for ( var i in map) {
		map[i].level = stack.length;
		cb(map[i], i);
		
		var c = map[i].children;
		if (c) {
			for (var q = 0, p = c.length; q < p; q++) 
				output(c[q], cb);
		}
	}
	stack.pop();
}

//findParent(map, 'd')
var m = {};

function render() {
	var table = aj('tbody');
	table.innerHTML = '';
	m = {};
	//select.onchange = onSelectChange;
	
	aj.xhr.get('list/?limit=99&randrom=' + Math.random(), function(json) {
		if(json.errorMsg) {
			ajaxjs.modal(null, {text:json.errorMsg});
			return;
		}
		
		var temp = document.createDocumentFragment();
		
		for (var i = 0, j = json.result.length; i < j; i++) {
			var n = json.result[i];
			//console.log(n);
			var parentNode = findParent(m, n.pid);
			if (parentNode == null) {
				m[n.id] = n;				
			} else {
				var obj = {};
				obj[n.id] = n;
				if (!parentNode.children)
					parentNode.children = [];

				parentNode.children.push(obj);
			}
		}
		
		output(m, function(node, nodeId) {
			var tr = document.createElement('tr'); // 节点
			tr.dataset['id'] = nodeId;
			tr.dataset['pid'] = node.pid;
			tr.dataset['name'] = node.name;
			tr.dataset['content'] = node.content;
			node.id = nodeId;
			node.nameTd = new Array(node.level * 5).join('&nbsp;') + (node.level == 1 ? '' : '└─') + node.name;
			tr.innerHTML = tppl(tdHtml, node);
				
			temp.appendChild(tr);
		});
		table.appendChild(temp)
	});
}


function showCreate(a) {
	var tr = a.up('tr');
	var id = tr.dataset.id, name = tr.dataset.name;
	showForm(true, id, '', '');
}

// 修改该数据字典名称
function showUpdate(a) {
	var tr = a.up('tr');
	var id = tr.dataset.id, name = tr.dataset.name, content = tr.dataset.content;
	showForm(false, id, name, content);
}

function showForm(isCreate, id, name, content) {
	var html = '请输入数据字典 {0} 名称以及简介<br /> <br />\
		<input type="text" class="ajaxjs-inputField" style="width:100%;" value="{1}" placeholder="请输入名称" /><br /><br />\
		<textarea placeholder="请输入简介"　class="ajaxjs-inputField" style="width:400px;height:150px;">{2}</textarea>'.replace('{0}', name).replace("{1}", name).replace("{2}", content);
	
	var alertObj = aj.alert.show(html, {
		showYes : true,
		showNo :true,
		showOk :false,
		onNoClk (e) { 
			alertObj.$el.classList.add('hide');
		},
		onYesClk (e) { 
			var cb = json => {
				aj.showOk(json.msg);
				json.isOk && render();
			}, data = {
				name : alertObj.$el.$('input').value, 
				content : alertObj.$el.$('textarea').value				
			};
			if(isCreate) {
				aj.xhr.post('.', cb, aj.apply(data, { pid : id}));
			} else {				
				aj.xhr.put(id + '/', cb, data);
			}
		}
	});
	
}

function showDelete(a) {
	var tr = a.up('tr');
	var id = tr.dataset.id, name = tr.dataset.name;
	aj.showConfirm('确定删除该数据字典{0}？'.replace('{0}', name), () => {
		ajaxjs.xhr.dele(id + '/',  json => {
			aj.showOk(json.msg);
			json.isOk && render();
		});
	});
}

render();