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
			// 父id 必须在子id之前，不然下面 findParent() 找不到后面的父节点，故先排序
			
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

	ajaxjs.tree.selectUI = function() {
		ajaxjs.tree.call(this);
		
		/**
		 * 渲染 DOM
		 */
		this.renderer = function (json, select, selectedId, cfg) {
			
			this.makeTree(json);
			
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

// 下拉分类选择器，异步请求远端获取分类数据
Vue.component('aj-tree-catelog-select', {
	props : {
		catelogId : { 			// 请求远端的分类 id，必填
			type: Number,
			required: true
		},
		selectedCatelogId : {	// 已选中的分类 id
			type: Number,
			required: false
		},
		fieldName : { // 表单 name，字段名
			type: String,
			required: false
		},
		isAutoJump : Boolean // 是否自动跳转 catalogId
	},
	template : '<select :name="fieldName" @change="onSelected($event);" class="aj-tree-catelog-select ajaxjs-select" style="width: 200px;"></select>',
		
	mounted : function() {
		aj.xhr.get(this.ajResources.ctx + "/admin/catelog/getListAndSubByParentId", this.load.bind(this), {parentId : this.catelogId});
	},
	
	methods : {
		load : function(json) {
			var catalogArr = json.result;
			var selectUI = new ajaxjs.tree.selectUI();
			selectUI.renderer(catalogArr, this.$el, this.selectedCatelogId, {makeAllOption : false});
		},
		
		onSelected : function(e) {
			if(this.isAutoJump) {
				var el = e.target, catalogId = el.selectedOptions[0].value;
				location.assign('?catalogId=' + catalogId);
			} else {
				this.BUS.$emit('aj-tree-catelog-select-change', e, this);
			}
		}
	}
});