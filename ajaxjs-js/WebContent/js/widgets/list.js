aj._list = {
	props: {
		apiUrl: {		// JSON 接口地址
			type: String, required : true
		},
		
		hrefStr: {
			type: String, required : false
		},
		isPage: {// 是否分页，false=读取所有数据
			type: Boolean, default : true
		}
	},
	data() {
		return {
			result: [],		// 展示的数据
			baseParam: {},		// 每次请求都附带的参数
			realApiUrl: this.apiUrl
		};
	}
};

// 简单列表
Vue.component('aj-simple-list', {
	mixins: [aj._list],
	beforeCreate() {	
		aj.getTemplate('list', 'aj-simple-list', this);
	},
	mounted() {
		aj.xhr.get(this.realApiUrl, j => aj.apply(this, j), this.baseParam);
	}
});


aj._pager = {
	data() {
		return {
			api: this.apiUrl, 	// 接口地址
			result: [],			// 展示的数据
			baseParam: {},		// 每次请求都附带的参数
			pageSize: this.initPageSize,
			total: 0,
			totalPage: 0,
			pageStart: 0,
			currentPage: 0
		};
	},
	props: {
		initPageSize: {
			type: Number, required: false, default: 9
		},
		apiUrl: String,
		autoLoad: { // 是否自动加载
			type: Boolean, default: true
		},
		isPage: {// 是否分页，false=读取所有数据
			type : Boolean, default : true
		}
	},
	methods: {
		// 分页，跳到第几页，下拉控件传入指定的页码
		jumpPageBySelect($event) {
			var selectEl = e.target;
			var currentPage = selectEl.options[selectEl.selectedIndex].value;
			this.pageStart = (Number(currentPage) - 1) * this.pageSize;
			this.ajaxGet();
		},
		onPageSizeChange($event) {
			this.pageSize = Number(e.target.value);
			this.count();
			this.ajaxGet();
		},
		count() {
			var totalPage = this.total / this.pageSize, yushu = this.total % this.pageSize;
			this.totalPage = parseInt(yushu == 0 ? totalPage : totalPage + 1);
			this.currentPage = (this.pageStart / this.pageSize) + 1;
		},
		previousPage() {
			this.pageStart -= this.pageSize;
			this.currentPage = (this.pageStart / this.pageSize) + 1;
			
			this.ajaxGet();
		},
		nextPage() {
			this.pageStart += this.pageSize;
			this.currentPage = (this.pageStart / this.pageSize) + 1;

			this.ajaxGet();
		},
	}
};

Vue.component('aj-pager', {	
	beforeCreate() {	
		aj.getTemplate('list', 'aj-pager', this);
	},
	mixins: [aj._pager],
	methods: {
		get() {
			aj.apply(this.baseParam, { start : this.pageStart, limit : this.pageSize });
			
			aj.xhr.get(this.api, json => {
				if(json.result) {
					this.result = json.result;
					if(this.isPage) {
						this.total = json.total;
						this.count();
					}
				}
				
				this.$emit('onDataLoad', this.result);	
				this.$emit('pager-result', this.result);
			}, this.baseParam);
		},
		ajaxGet() {
			this.get();
		},
		// 复位
		reset() {
			this.total = this.totalPage = this.pageStart = this.currentPage = 0;
			this.pageSize = this.initPageSize;
		}
	}
});

/**
 * 列表控件
 */

// 分页
Vue.component('aj-page-list', {
	mixins: [aj._pager, aj._list],
	props: {
		isShowFooter: {
			type : Boolean, default : true
		},
		autoLoadWhenReachedBottom : {	// 到底部是否自动加载下一页，通常在 移动端使用，这个应该是元素的 CSS Selector
			type : String, default: ''
		},
		isDataAppend: {// 数据分页是否追加模式，默认不追加 = false。 App 一般采用追加模式
			type : Boolean, default : false
		}
	},
	beforeCreate() {	
		aj.getTemplate('list', 'aj-page-list', this);
	},
	mounted() {
		aj.xhr.get(this.realApiUrl, this.doAjaxGet, { limit: this.pageSize});
		
		if(!!this.autoLoadWhenReachedBottom) {
			var scrollSpy = new aj.scrollSpy({scrollInElement: aj(this.autoLoadWhenReachedBottom), spyOn: thish.$el.$('.buttom')});
			scrollSpy.onScrollSpyBackInSight = e => this.nextPage();
		}
	},
	
	created() {
		this.BUS.$on('base-param-change', this.onBaseParamChange.bind(this));
	},
	
	methods : {
		doAjaxGet(json) {
			if(this.isPage) {
				this.total = json.total;
				this.result = this.isDataAppend ? this.result.concat(json.result) : json.result;
				this.count();				
			} else
				this.result = json.result;
		}, 
		ajaxGet() {
			var params = {};
			aj.apply(params, { start : this.pageStart, limit : this.pageSize });
			this.baseParam && aj.apply(params, this.baseParam);
			
//			ajaxjs.xhr.get(this.$props.apiUrl, this.doAjaxGet, params);
			ajaxjs.xhr.get(this.realApiUrl, this.doAjaxGet, params);
		},
		onBaseParamChange(params) {
			aj.apply(this.baseParam, params);
			
			this.pageStart = 0; // 每次 baseParam 被改变，都是从第一笔开始
			this.ajaxGet();
		}
	},
	watch: {
		baseParam(index, oldIndex) {
			this.ajaxGet();
		}
	}
});

//register the grid component
Vue.component('aj-grid', {
	beforeCreate() {	
		aj.getTemplate('list', 'aj-grid', this);
	},
    props: {
        data: Array,
        columns: Array,
        filterKey: String
    },
    data() {
        var sortOrders = {};
        this.columns.forEach(key => sortOrders[key] = 1);
        
        return {sortKey: '', sortOrders: sortOrders};
    },
    computed: {
        filteredData() {
            var sortKey = this.sortKey;
            var filterKey = this.filterKey && this.filterKey.toLowerCase();
            var order = this.sortOrders[sortKey] || 1;
            var data = this.data;

            if (filterKey) {
                data = data.filter(row => {
                    return Object.keys(row).some(key => String(row[key]).toLowerCase().indexOf(filterKey) > -1);
                });
            }

            if (sortKey) {
                data = data.slice().sort((a, b) => {
                    a = a[sortKey];
                    b = b[sortKey];
                    return (a === b ? 0 : a > b ? 1 : -1) * order;
                });
            }

            return data;
        }
    },
    filters: {
        capitalize(str) {
            return str.charAt(0).toUpperCase() + str.slice(1);
        }
    },
    methods: {
        sortBy(key) {
            this.sortKey = key;
            this.sortOrders[key] = this.sortOrders[key] * -1;
        }
    }
});

/**
 * Thx to ScrollSpy
 * 
 * @param {Elment} 如果是在区域内滚动的话，则要传入滚动面板的元素，移动端会适用
 */
aj.scrollSpy = function(cfg) {
    var isScrollInElement = !!(cfg && cfg.scrollInElement);
    
    var handleScroll = function() {
        var currentViewPosition;
        
        if(isScrollInElement) 
        	currentViewPosition = cfg.scrollInElement.scrollTop + window.innerHeight;
        else 
        	currentViewPosition = document.documentElement.scrollTop ? document.documentElement.scrollTop: document.body.scrollTop;
        
        for (var i in elements) {
            var element = elements[i], 
            	el = element.domElement,
            	elementPosition = getPositionOfElement(el);
            
            var usableViewPosition = currentViewPosition;

            if (element.isInViewPort == false) 
                usableViewPosition -= el.clientHeight;

            if (usableViewPosition < elementPosition) {
            	this.onScrollSpyOutOfSight && this.onScrollSpyOutOfSight(el);
                element.isInViewPort = false;
            } else if (element.isInViewPort == false) {
            	this.onScrollSpyBackInSight && this.onScrollSpyBackInSight(el);
                element.isInViewPort = true;
            }
        }
    }.bind(this);
  
    if (document.addEventListener) {
    	(cfg && cfg.scrollInElement || document).addEventListener("touchmove", handleScroll, false);
    	(cfg && cfg.scrollInElement || document).addEventListener("scroll", handleScroll, false);
    } else if (window.attachEvent) 
        window.attachEvent("onscroll", handleScroll);
    
    var elements = {};
    
    this.spyOn = function(domElement) {
        var element = {};
        element['domElement'] = domElement;
        element['isInViewPort'] = true;
        elements[domElement.id] = element;
    }
    
    if(cfg && cfg.spyOn) 
    	this.spyOn(cfg.spyOn);
    
    // 获取元素y方向距离
    function getPositionOfElement(domElement) {
        var pos = 0;
        while (domElement != null) {
            pos += domElement.offsetTop;
            domElement = domElement.offsetParent;
        }
        
        return pos;
    }
}


// Tree-like option control

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
aj.treeLike = {
	methods: {
		// 遍历各个元素，输出
		output: (() => {
			var stack = [];
			
			return function(map, cb) {
				stack.push(map);
				
				for ( var i in map) {
					map[i].level = stack.length;// 层数，也表示缩进多少个字符
					cb(map[i], i);
					
					var c = map[i].children;
					if (c) {
						for (var q = 0, p = c.length; q < p; q++) 
							this.output(c[q], cb);
					}
				}
				
				stack.pop();
			};
		})(),
		// 递归查找父亲节点，根据传入 id
		findParent (map, id) {
			for ( var i in map) {
				if (i == id)
					return map[i];
					
				var c = map[i].children;
				
				if (c) {
					for (var q = 0, p = c.length; q < p; q++) {
						var result = this.findParent(c[q], id);
						if (result != null)
							return result;
					}
				}
			}
		
			return null;
		},
		// 生成树，将扁平化的结构 还原为树状的结构
		// 父id 必须在子id之前，不然下面 findParent() 找不到后面的父节点，故这个数组必须先排序
		toTree(jsonArr) {
			if(!jsonArr)
				return;
				
			var m = {};
			
			for (var i = 0, j = jsonArr.length; i < j; i++) {
				var n = jsonArr[i];
				var parentNode = this.findParent(m, n.pid);
				
				if (parentNode == null) {	// 没有父节点，那就表示这是根节点，保存之
					m[n.id] = n;			// id 是key，value 新建一对象
				} else { 					// 有父亲节点，作为孩子节点保存
					var obj = {};
					obj[n.id] = n;
					
					if (!parentNode.children)
						parentNode.children = [];
	
					parentNode.children.push(obj);
				}
			}
			
			return m;
		},
		/**
		 * 渲染 Option 标签的 DOM
		 */
		rendererOption(json, select, selectedId, cfg) {
			if(cfg && cfg.makeAllOption) {
				var option = document.createElement('option');
				option.value = option.innerHTML = "全部分类";
				select.appendChild(option);
			}
			
			// 生成 option
			var temp = document.createDocumentFragment();
			
			this.output(this.toTree(json), (node, nodeId) => {
				var option = document.createElement('option'); // 节点
				option.value = nodeId;
				
				if(selectedId && selectedId == nodeId) // 选中的
					option.selected = true;
				
				option.dataset['pid'] = node.pid;
				//option.style= "padding-left:" + (node.level - 1) +"rem;";
				option.innerHTML = new Array(node.level * 5).join('&nbsp;') + (node.level == 1 ? '' : '└─') + node.name;
				temp.appendChild(option);
			});
			
			select.appendChild(temp);
		}
	}
};

// 下拉分类选择器，异步请求远端获取分类数据
Vue.component('aj-tree-catelog-select', {
	mixins: [aj.treeLike],
	template: '<select :name="fieldName" @change="onSelected" class="aj-tree-catelog-select aj-select" style="width: 200px;"></select>',
	props: {
		catalogId: { 			// 请求远端的分类 id，必填
			type: Number, required: true
		},
		selectedCatalogId: {	// 已选中的分类 id
			type: Number, required: false
		},
		fieldName: { // 表单 name，字段名
			type: String, default:'catalogId'
		},
		isAutoJump: Boolean // 是否自动跳转 catalogId
	},
	mounted() {
		var fn = j => this.rendererOption(j.result, this.$el, this.selectedCatalogId, {makeAllOption : false});
		aj.xhr.get(this.ajResources.ctx + "/admin/catelog/getListAndSubByParentId", fn, {parentId : this.catalogId});
	},
	
	methods: {	
		onSelected($event) {
			if(this.isAutoJump) {
				var el = e.target, catalogId = el.selectedOptions[0].value;
				location.assign('?' + this.fieldName + '=' + catalogId);
			} else 
				this.BUS.$emit('aj-tree-catelog-select-change', e, this);
		}
	}
});

Vue.component('aj-tree-like-select', {
	mixins: [aj.treeLike],
	template: '<select :name="name" class="aj-select" @change="onSelected"></select>',
	props: {
		catalogId: { 			// 请求远端的分类 id，必填
			type: Number, required: true
		},
		selectedId: {	// 已选中的分类 id
			type: Number, required: false
		},
		name: { // 表单 name，字段名
			type: String, required: false, default:'catalogId'
		},
		api: {
			type: String, default: '/admin/tree-like/'
		}
	},
	mounted() {
		var url = this.ajResources.ctx + this.api + this.catalogId + "/";
		var fn = j => this.rendererOption(j.result, this.$el, this.selectedCatalogId, {makeAllOption : false});
		aj.xhr.get(url, fn);
	},
	methods: {	
		onSelected($event) {
			var el = $event.target, catalogId = el.selectedOptions[0].value;
			this.$emit('selected', Number(catalogId));
		}
	}
});

Vue.component('aj-tree-user-role-select', {
	mixins: [aj.treeLike],
	props: {
		value: { 			// 请求远端的分类 id，必填
			type: Number, required: false
		},
		json: Array,
		noJump: {		// 是否自动跳转
			type: Boolean, defualt: false
		}
	},
	template: '<select name="roleId" class="aj-select"></select>',
	mounted() {
		this.rendererOption(this.json, this.$el, this.value, {makeAllOption : false});
		
		if(!this.noJump)
			this.$el.onchange = () => location.assign("?roleId=" + this.$el.options[this.$el.selectedIndex].value);
	}
});

// 注意递归组件的使用
Vue.component('aj-tree-item', {
    template: '#aj-tree-item',
    props: {
        model: Object,
        allowAddNode: { // 是否允许添加新节点
        	type: Boolean, default: false
        }
    },
    data() {
        return {open: false};
    },
    computed: {
        isFolder() {
            return this.model.children && this.model.children.length;
        }
    },
    methods: {
		// 点击节点时的方法
        toggle() {
            if (this.isFolder)
                this.open = !this.open;
            
			this.BUS.$emit('tree-node-click', this.model);
        },
        // 变为文件夹
        changeType() {
            if (!this.isFolder) {
                Vue.set(this.model, 'children', []);
                this.addChild();
                this.open = true;
            }
        },
        addChild() {
            this.model.children.push({
                name: 'new stuff'
            });
        }
    }
});

Vue.component('aj-tree', {
	template: '<ul class="aj-tree"><aj-tree-item :model="treeData"></aj-tree-item></ul>',
	props: {
		url: String, topNodeName: String // 根节点显示名称
	},
	data() {
		return {
			treeData: { 
				name: this.topNodeName || 'TOP', children: null 
			}
		};
	},
	mounted() {
		aj.xhr.get(this.ajResources.ctx + this.url, j => this.treeData.children = this.makeTree(j.result));
		// 递归组件怎么事件上报呢？通过事件 bus
		// this.BUS.$on('treenodeclick', data => this.$emit('treenodeclick', data));
	},
	methods: {
		makeTree(jsonArray) {
			var arr = [];
			// 父id 必须在子 id 之前，不然下面 findParent() 找不到后面的父节点，故先排序
			for (var i = 0, j = jsonArray.length; i < j; i++) {
				var n = jsonArray[i];
				
				if(n.pid === -1) 
					arr.push(n);
				else {
					var parentNode = this.findParent(arr, n.pid);
					
					if (parentNode) {
						if (!parentNode.children)
							parentNode.children = [];
							
						parentNode.children.push(n);
					} else
						console.log('parent not found!');
				}
			}
			
			return arr;
		},

		// 递归查找父亲节点，根据传入 id
		findParent(jsonArray, id) {
			for (var i = 0, j = jsonArray.length; i < j; i++) {
				var map = jsonArray[i];
				
				if (map.id == id)
					return map;
				
				if (map.children) {
					var result = arguments.callee(map.children, id);
					
					if (result != null)
						return result;
				}
			}

			return null;
		}
	}
});