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

Vue.component('aj-select', {
	props : {
		json : {
			type: Object,
			required: true
		},
		defaultSelected : {	// 已选中的分类 id
			type: String,
			required: false
		},
		fieldName : { // 表单 name，字段名
			type: String,
			required: true
		}
	},
	data : function() {
        return {
            selected : ""
        };
    },
	template : '<select :name="fieldName" @change="onSelected($event);" class="ajaxjs-select" style="min-width: 200px;" v-model="selected">\
        <option v-for="(key, v) in json" :value="v">{{key}}</option>\
		</select>',
		
	created:function(){
		if(this.defaultSelected) {
			this.selected = this.defaultSelected;
		} else {
			for(var i in this.json) {
				this.selected = i;
				break;
			}
		}
	},
	
	methods : {
		onSelected : function(e) {
		},
		
	    getSelected: function(){
	        //获取选中的优惠券
	        console.log(this.selected)
	    }
	}
});

// 适合数组的输入
Vue.component('aj-select-arr', {
	props : {
		url : '',
		fieldName : { // 表单 name，字段名
			type: String,
			required: true
		},
		firstOption:{
			type:String,
			default :'---请选择---'
		},
		defaultSelected : {	// 已选中的分类 id
			type: String,
			default: "0"
		}
	},
	data : function() {
        return {
            json : {}
        };
    },
	template:'<aj-select :json="json" :field-name="fieldName" :default-selected="defaultSelected"></aj-select>',
	mounted : function(){
		var self = this; 
		aj.xhr.get(this.ajResources.ctx + this.url, function(arr) {
			var json = {0: self.firstOption};
			for (var i = 0, j = arr.length; i < j; i++) { // 固定 id、name 字段
				json[arr[i].id] = arr[i].name;
			}
			self.json = json;
		});
	}
});

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
			required: false,
			default:'catelogId'
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
				location.assign('?' + this.fieldName + '=' + catalogId);
			} else {
				this.BUS.$emit('aj-tree-catelog-select-change', e, this);
			}
		}
	}
});

//全国省市区 写死属性
Vue.component('aj-china-area', {
	template : '<div class="aj-china-area"><select v-model="province" class="ajaxjs-select" name="locationProvince">\
		            <option value="">请选择</option>\
		            <option v-for="(v, k) in addressData[86]" :value="k">{{v}}</option>\
		        </select>\
		        <select v-model="city" class="ajaxjs-select" name="locationCity">\
		            <option value="">请选择</option>\
		            <option v-for="(v, k) in citys" :value="k">{{v}}</option>\
		        </select>\
		        <select v-model="district" class="ajaxjs-select" name="locationDistrict">\
		            <option value="">请选择</option>\
		            <option v-for="(v, k) in districts" :value="k">{{v}}</option>\
		        </select>\
		    </div>',
    props:{
        provinceCode:String,
        cityCode: String,
        districtCode: String
    },
   data:function(){
	   if(!China_AREA)throw '中国行政区域数据 脚本没导入';
        return {
        	province: this.provinceCode || '',
        	city: this.cityCode || '',
        	district: this.districtCode || '',
            addressData: China_AREA
        }
    },
    
    watch:{ // 令下一级修改
        province: function(val, oldval) {
//            if(val !== oldval) 
//                this.city = '';
            
        },
//        city: function(val, oldval) {
//            if(val !== oldval)
//                this.district = '';
//        }
    },
   
    computed: {
        citys:function() {
            if(!this.province)
                return;
            return this.addressData[this.province];
        },
        districts :function(){
            if(!this.city)
                return;
            return this.addressData[this.city];
        }
    }
});


Vue.component('aj-tree-item', {
    template: // 注意递归组件的使用
        '<li>\
              <div :class="{bold: isFolder, node: true}" @click="toggle">\
				<span>········</span>{{ model.name }}\
                <span v-if="isFolder">[{{ open ? \'-\' : \'+\' }}]</span>\
              </div>\
              <ul v-show="open" v-if="isFolder" :class="{show: open}">\
                <aj-tree-item :event-bus="eventBus" class="item" v-for="(model, index) in model.children" :key="index" :model="model"></aj-tree-item>\
                <li v-if="allowAddNode" class="add" @click="addChild">+</li>\
              </ul>\
          </li>',
    props: {
        model: Object,
        allowAddNode: { // 是否允许添加新节点
        	type: Boolean,
        	default:false
        },
        eventBus: Object
    },
    data: function () {
        return {
            open: false
        };
    },
    computed: {
        isFolder: function () {
            return this.model.children && this.model.children.length;
        }
    },
    methods: {
        toggle: function () {
            if (this.isFolder)
                this.open = !this.open;
            
            if(this.eventBus) {
            	this.eventBus.$emit('treenodeclick', this.model);
            }
        },
        fireEvent(){
        	//this.$emit('treenodeclick', this.model);
        },
        // 变为文件夹
        changeType: function () {
            if (!this.isFolder) {
                Vue.set(this.model, 'children', []);
                this.addChild();
                this.open = true;
            }
        },
        addChild: function () {
            this.model.children.push({
                name: 'new stuff'
            });
        }
    }
});

Vue.component('aj-tree', {
	template : '<ul class="aj-tree"><aj-tree-item :model="treeData" :event-bus="this.eventBus"></aj-tree-item></ul>',
	props: {
		url: String, 
		topNodeName : String // 根节点显示名称
	},
	data: function() {
		return {
			treeData: { name : this.topNodeName || 'TOP', children : null },
			eventBus: new Vue()
		};
	},
	mounted : function() {
		aj.xhr.get(this.ajResources.ctx + this.url, 
			json => 
				this.treeData.children = this.makeTree(json.result)
		);
		this.eventBus.$on('treenodeclick', data => { // 递归组件怎么事件上报呢？通过事件 bus
			this.$emit('treenodeclick', data);
		});
	},
	methods: {
		makeTree (jsonArray) {
			var arr = [];
			// 父id 必须在子id之前，不然下面 findParent() 找不到后面的父节点，故先排序
			
			for (var i = 0, j = jsonArray.length; i < j; i++) {
				var n = jsonArray[i];
				if(n.pid === -1) {
					arr.push(n);
				} else {
					var parentNode = this.findParent(arr, n.pid);
					if (parentNode) {
						if (!parentNode.children)
							parentNode.children = [];
						parentNode.children.push(n);
					} else{
						console.log('parent not found!');
					}
				}
				
			}
			
			return arr;
		},

		// 递归查找父亲节点，根据传入 id
		findParent (jsonArray, id) {
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
		},
		captureBubble(data) {
			this.$emit('treenodeclick', data);
		}
	}
});
