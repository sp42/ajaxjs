// Tree-like option control
;(function() {
	/*
	 * 参考结构
	var map = {
		a : 1,
		b : 2,
		c : {
			children : [ {
				d : 3
			} ]
		}
	};
	*/
	ajaxjs.tree = function() {
		this.initData();
	};
	
	ajaxjs.tree.prototype = {
		// 重置数据
		initData : function() {
			this.stack = [];
			this.tree = {};
		},
		
		// 生成树
		makeTree : function (jsonArray) {
			if(ajaxjs.ua.isWebkit) {
				for (var i = 0; i < jsonArray.length; i++) {
					jsonArray[i].oldIndex = i;
				}
			}
			
			jsonArray.sort(sortByPid);// 父id 必须在子id之前，不然下面 findParent() 找不到后面的父节点，故先排序
			
			for (var i = 0, j = jsonArray.length; i < j; i++) {
				var n = jsonArray[i];

				var parentNode = findParent(this.tree, n.pid);
				if (parentNode == null) { // 没有父节点，那就表示这是根节点，保存之
					this.tree[n.id] = { // id 是key，value 新建一对象
						name : n.name,
						pid : n.pid
					};				
				} else { // 有父亲节点，作为孩子节点保存
					var obj = {};
					obj[n.id] = {
						name : n.name,
						pid : n.pid
					};
					if (!parentNode.children)
						parentNode.children = [];

					parentNode.children.push(obj);
				}
			}
		},
		
		// 遍历各个元素，输出
		output : function (map, cb) {
			this.stack.push(map);
			
			for (var i in map) {
				map[i].level = this.stack.length;// 层数，也表示缩进多少个字符
				cb(map[i], i);
				
				var c = map[i].children;
				if (c) {
					for (var q = 0, p = c.length; q < p; q++) 
						this.output(c[q], cb);
				}
			}
			
			this.stack.pop();
		}
	}
	
	// 递归查找父亲节点，根据传入 id
	var findParent = function (map, id) {
		for ( var i in map) {
			if (i == id)
				return map[i];
			
			var c = map[i].children;
			if (c) {
				for (var q = 0, p = c.length; q < p; q++) {
					var result = arguments.callee(c[q], id);
					if (result != null)
						return result;
				}
			}
		}

		return null;
	}
	
	var sortByPid = ajaxjs.ua.isWebkit ? function (a, b) {
		// Chrome谷歌浏览器中js代码Array.sort排序的bug乱序解决办法 
		// https://www.cnblogs.com/yzeng/p/3949182.html?utm_source=tuicool&utm_medium=referral
		return [a.a, a.b] > [b.a, b.b] ? 1:-1;
//		return b.v - a.v || b.oldIndex - a.oldIndex; // Chrome sucks
	} : function (a, b) {
		return a.pid > b.pid;
	}
	
	ajaxjs.tree.selectUI = function() {
		ajaxjs.tree.call(this);
		
		/**
		 * 渲染 DOM
		 */
		this.renderer = function (json, select, selectedId, cfg) {
			
			selectUI.makeTree(json);
			
			if(cfg) {
				if(cfg.makeAllOption) {
					var option = document.createElement('option');
					option.value = option.innerHTML = "全部分类";
					select.appendChild(option);
				}
			}
			
			// 生成 option
			var temp = document.createDocumentFragment();

			this.output(this.tree, function(node, nodeId) {
				var option = document.createElement('option'); // 节点
				option.value = nodeId;
				
				if(selectedId && selectedId == nodeId) { // 选中的
					option.selected = true;
				}
				
				option.dataset['pid'] = node.pid;
				//option.style= "padding-left:" + (node.level - 1) +"rem;";
				option.innerHTML = new Array(node.level * 5).join('&nbsp;') + (node.level == 1 ? '' : '└─') + node.name;
				temp.appendChild(option);
				//console.log(node);
			});
			
			select.appendChild(temp);
		}
	}
	
	ajaxjs.tree.selectUI.prototype = ajaxjs.tree.prototype;
})();