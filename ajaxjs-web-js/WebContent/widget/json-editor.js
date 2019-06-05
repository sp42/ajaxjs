
var stack = [];
var stack_indexOrKey = [];
var stack_uls = [aj('.tree')];

function everyItem(item, indexOrKey) {
	var len = stack.length;
	var path = getPath(indexOrKey);
	var li = document.createElement('li');
	li.innerHTML = '<div class="line"></div><h3 data-path="' + path + '"></h3>';
	var h3 = li.$('h3');
	h3.innerHTML = indexOrKey;
	h3.onclick = onNodeClk;
	
	var isArray, isObject;
	if (Array.isArray(item)) 
		isArray = true;
	else if (isPlainObject(item))
		isObject = true;
	
	if (isArray || isObject) {
		li.className = 'parent';

		var ul = document.createElement('ul');
		li.appendChild(ul);
		stack_uls[stack_uls.length - 1].appendChild(li);
		
		stack.push(item);
		stack_indexOrKey.push(indexOrKey);
		stack_uls.push(ul)
		
		if (isArray) 
			everyJsonArr(item);
		else if (isObject)
			everyObjectField(item);
		
		stack.pop();
		stack_indexOrKey.pop();
		stack_uls.pop();
	} else {
		h3.innerHTML = indexOrKey + ':' + item;
		
		stack_uls[stack_uls.length - 1].appendChild(li);
	}
}

// 点击　node 事件
function onNodeClk(e) {
	var h3 = e.target, path = e.target.dataset.path;
	var value = eval('VUE.jsonData' + path);
	VUE.path = path;
	
	var isArray, isObject;
	if (Array.isArray(value)) {
		isArray = true;
		VUE.valueType = Array;
		VUE.value = null;
	} else if (isPlainObject(value)) {
		isObject = true;
		VUE.valueType = Object;
		VUE.value = null;
	} else {
		if(typeof value == 'string')
			VUE.valueType = String;
		if(typeof value == 'number')
			VUE.valueType = Number;
		if(typeof value == 'boolean')
			VUE.valueType = Boolean;
		VUE.value = value;
	}
	
	VUE.key = getKey(path);
	VUE.isLeaf = !isArray && !isObject && /^\d+$/.test(VUE.key);
	VUE.canEditValue = !(isArray || isObject);
}

function getKey(path) {
	var arr = path.split('[');
	return arr.pop().replace(/'|"|]/g, '');
}

function everyJsonArr(jsonArr) {
	for (var i = 0, j = jsonArr.length; i < j; i++) 
		everyItem(jsonArr[i], i)
}

function everyObjectField(obj) {
	for ( var i in obj) 
		everyItem(obj[i], i);
}

var getPath = (lastKey) => {
	var arr = [];
	for(var i = 0, j = stack_indexOrKey.length; i < j; i++) {
		var item = stack_indexOrKey[i];
		if(typeof item === 'string') {
			arr.push("['" + item + "']");
		} else {
			arr.push('[' + item + ']');
		}
	}
	
	if(typeof lastKey === 'string') {
		arr.push("['" + lastKey + "']");
	} else {
		arr.push('[' + lastKey + ']');
	}
	
	return arr.join('');
}

// 判断指定参数是否是一个纯粹的对象（所谓”纯粹的对象”，就是该对象是通过”{}”或”new Object”创建的。）
function isPlainObject(obj) {
	return Object.prototype.toString.call(obj) === '[object Object]';
}

everyJsonArr(VUE.jsonData);