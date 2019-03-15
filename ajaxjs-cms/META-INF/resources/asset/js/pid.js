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
	var table = document.querySelector('tbody');
	table.innerHTML = '';
	m = {};
	//select.onchange = onSelectChange;
	
	ajaxjs.xhr.get('list/?limit=99&randrom=' + Math.random(), function(json) {
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
	
	var name = window.prompt('你即将在 {0} 下创建新的字典，请问名称是？'.replace('{0}', name));
	if(!name)
		return;
	
	var content = window.prompt('你即将在 {0} 下创建新的字典，请问描述是？'.replace('{0}', name));
	if(!content)
		return;
	
	if (name &&content ) {
		ajaxjs.xhr.post('.', function(json) {
			aj.alert.show(json.msg);
			if (json.isOk) {
				render();
			}
		}, { pid : id, name : name,  content : content});
	}
}

function showUpdate(a) {
	var tr = a.up('tr');
	var id = tr.dataset.id, name = tr.dataset.name;
	var text = window.prompt('请输入数据字典 {0} 新的名称？'.replace('{0}', name));
	
	if (text) {
		// 修改该数据字典名称
		ajaxjs.xhr.put(id + '/', function(json) {
			aj.alert.show(json.msg);
			if (json.isOk) {
				render();
			}
		}, { name : text });
	}
}

function showDelete(a) {
	var tr = a.up('tr');
	var id = tr.dataset.id, name = tr.dataset.name;
	
	if (window.confirm('确定删除该数据字典{0}？'.replace('{0}', name))) {
		ajaxjs.xhr.dele(id + '/',
		function callback(json) {
			aj.alert.show(json.msg);
			if (json.isOk) {
				render();
			}
		});
	}
}

render();