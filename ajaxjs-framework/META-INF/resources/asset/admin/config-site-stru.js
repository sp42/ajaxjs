var tree = aj('.tree');
var tpl = aj('.tpl').value;
var stack = [], indexs = [], ids =[];

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
		indexs.push(i);
		var el = json[i];
		ids.push(el.id);
		
		var li = document.createElement('li');
			li.dataset.path = ids.join('/');
			li.dataset.jsonPath = indexs.join('-');

		if (el.children) {
			var div = document.createElement('div'); // parentNode
			div.className = 'parentNode';
			div.innerHTML = tpl;

			li.appendChild(div);
			li.$('.indicator').innerHTML = '+';
			//debugger;
		
			it(el.children, fn, li);
		} else {
			li.innerHTML = tpl;
			li.$('.indicator').innerHTML = '&nbsp;&nbsp;';
			
			fn(i, el);
		}
		
		li.onclick = toggle;
		li.querySelector('button.saveId').onclick = onSaveIdBtnClk;
		li.querySelector('button.saveName').onclick = onSaveBtnClk;
		li.querySelector('button.saveParam').onclick = onSaveParamBtnClk;
		li.querySelector('input[name=id]').value = el.id;
		li.querySelector('input[name=name]').value = el.name;
		li.querySelector('.name').innerHTML = el.id;
		li.$('button.initJSP').onclick = e => {
			aj.showConfirm('提示：<br />初始化页面会<b>覆盖</b>原有的页面，确定进行初始化？？', ()=> {
				var el = e.target, li = el.up('li');
				var path = li.dataset.path;
				
				aj.xhr.post('initJSP', json => {
					if(json.isOk)
						aj.msg.show(json.msg);				
				}, {path: path});
			});
		};
		
		
		var isHidden = li.querySelector('input[type=checkbox]');
		isHidden.onchange = saveIsHidden;
		if(el.isHidden)
			isHidden.checked = true;
		
		ul.appendChild(li);
		indexs.pop();
		ids.pop();
	}
	
	stack.pop();
	parentEl.appendChild(ul);
}

function toggle(e) {
	var el = e.target, li = e.currentTarget;
	e.stopPropagation();
	//debugger;
	if(el.tagName == 'SPAN') {				
		if(el.className.indexOf('up') != -1 && li.previousElementSibling) {
			new JSON_CRUD_ARR(jsonData, li.dataset.jsonPath).swap(li.previousElementSibling);
			swapElements(li.previousElementSibling, li);
			saveJsonToServer();
			return;
		}
		
		if(el.className.indexOf('down') != -1 && li.nextElementSibling) {
			new JSON_CRUD_ARR(jsonData, li.dataset.jsonPath).swap(li.nextElementSibling);
			swapElements(li.nextElementSibling, li);
			saveJsonToServer();
			return;
		}

		if(el.classList.contains('fa-minus')) {
			aj.showConfirm('确定删除？', ()=> {
				new JSON_CRUD_ARR(jsonData, li.dataset.jsonPath).delete();
				saveJsonToServer(() => {
					location.reload();
				});
			});
			return;
		}

		if(el.classList.contains('fa-plus')) {
			aj.showConfirm(aj('.addTpl').value, (el)=> {
				var id = el.$('input[name=id]').value;
				var name = el.$('input[name=name]').value;
				
				new JSON_CRUD_ARR(jsonData, li.dataset.jsonPath).createChild(id, name);
				saveJsonToServer(() => {
					location.reload();
				});
			}, true);
			return;
		}
		
		
		if(el.classList.contains('indicator') || el.classList.contains('name')) {
			// 展开
			var ul = li.$('ul');
			if (ul) {
				if (ul.style.height == '0px') {
					li.$('.indicator').innerHTML = '-';
					ul.style.height = ul.scrollHeight + 'px';
					
					setTimeout(() => {
						ul.style.height = 'auto'; // a ticky
					}, 500);
				} else {
					li.$('.indicator').innerHTML = '+';
					ul.style.height = '0px';
				}
			}
		}
	} 	
	
}

function saveIsHidden(e) {
	var el = e.target, li = el.up('li');
	new JSON_CRUD_ARR(jsonData, li.dataset.jsonPath).updateIsHidden(el.checked);
	saveJsonToServer();
}

function onSaveIdBtnClk(e) {
	var el = e.target, li = el.up('li');
	new JSON_CRUD_ARR(jsonData, li.dataset.jsonPath).updateId(el.previousElementSibling.value);
	saveJsonToServer(() => {
		location.reload();
	});
}

function onSaveBtnClk(e) {
	var el = e.target, li = el.up('li');
	new JSON_CRUD_ARR(jsonData, li.dataset.jsonPath).updateName(el.previousElementSibling.value);
	saveJsonToServer();
}

function onSaveParamBtnClk(e) {
	var el = e.target, li = el.up('li');
	new JSON_CRUD_ARR(jsonData, li.dataset.jsonPath).updateParam(el.previousElementSibling.value);
	
	saveJsonToServer();
}

it(jsonData, (item, v) => {
	//console.log(item + ':' + stack.length);
}, tree);


function JSON_CRUD_ARR(jsonData, indexs) {
	var arr = indexs.split('-'), _arr = jsonData, lastIndex;
	
	// find that array
	for(var i = 0, j = arr.length; i < j; i++) {
		if(i === (j - 1)) {
			lastIndex = arr[i];
			break;
		}
		
		_arr = (_arr instanceof Array) ? _arr : _arr.children;  
		_arr = _arr[arr[i]];
	}
	
	this.arr = _arr.children || _arr;
	this.lastIndex = lastIndex;
	// debugger;
}

JSON_CRUD_ARR.prototype = {
	// 所在的数组
	arr: null,
	
	// delete a element in Array
	delete(){
		 this.arr.splice(this.lastIndex, 1);
	},
	updateId(newId) {
		this.arr[this.lastIndex].id = newId;
	},
	updateName(newName) {
		this.arr[this.lastIndex].name = newName;
	},
	
	createChild(id, name) {
		var node = this.arr[this.lastIndex];
		if(!node.children) 
			node.children = [];
		
		node.children.push({
			id : id,
			name: name
		});
	},
	addTop(id, name) {
		
	},
	updateParam(newParam) {
		this.arr[this.lastIndex].param = newParam;
	},
	updateIsHidden(isHidden) {
		this.arr[this.lastIndex].isHidden = isHidden;
	},
	swap(sEl) {
		var arr = sEl.dataset.jsonPath.split('-');
		var sEl_index = arr.pop();
		//alert(sEl_index)
		var temp = this.arr[this.lastIndex];
		
		this.arr[this.lastIndex] = this.arr[sEl_index];
		this.arr[sEl_index] = temp;
	}
};

// 提交 JSON 数据到服务器
function saveJsonToServer(fn) {
	var str = JSON.stringify(jsonData, null, 4);
	aj.xhr.post('?', json => {
		if(json.isOk)
			aj.msg.show(json.msg);
		
		fn && setTimeout(fn,2000);
	}, {json: str});
}

function swapElements(a, b) {
	if (a == b) 
		return;
	// 记录父元素
	var bp = b.parentNode, ap = a.parentNode;
	// 记录下一个同级元素
	var an = a.nextElementSibling, bn = b.nextElementSibling;
	// 如果参照物是邻近元素则直接调整位置
	if (an == b)
		return bp.insertBefore(b, a);
	if (bn == a)
		return ap.insertBefore(a, b);
	if (a.contains(b)) //如果a包含了b
		return ap.insertBefore(b, a), bp.insertBefore(a, bn);
	else
		return bp.insertBefore(a, b), ap.insertBefore(b, an);
}

aj('.addTop').onclick = e => {
	aj.showConfirm(aj('.addTpl').value, el => {
		var id = el.$('input[name=id]').value;
		var name = el.$('input[name=name]').value;
		
		jsonData.push({
			id : id,
			name: name
		})
		
		saveJsonToServer(() => {
			location.reload();
		});
	}, true);
};