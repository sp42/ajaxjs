Vue.component('aj-tree-item', {
    template: // 注意递归组件的使用
        '<li>\
              <div :class="{bold: isFolder, node: true}" @click="toggle"><span>········</span>{{ model.name }}\
                    <span v-if="isFolder">[{{ open ? \'-\' : \'+\' }}]</span>\
              </div>\
              <ul v-show="open" v-if="isFolder" :class="{show: open}">\
                <aj-tree-item class="item" v-for="(model, index) in model.children" :key="index" :model="model"></aj-tree-item>\
                <li v-if="allowAddNode" class="add" @click="addChild">+</li>\
              </ul>\
          </li>',
    props: {
        model: Object,
        allowAddNode: { // 是否允许添加新节点
        	type: Boolean,
        	default:false
        }
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
	template : '<ul class="aj-tree"><aj-tree-item :model="treeData"></aj-tree-item></ul>',
	props: {
		url: String, 
		topNodeName : String // 根节点显示名称
	},
	data: function() {
		return {
			treeData: { name : this.topNodeName || 'TOP', children : null }
		};
	},
	mounted : function() {
		aj.xhr.get(this.ajResources.ctx + this.url, function(json) {
			this.treeData.children = this.makeTree(json.result);
		}.bind(this));
	},
	methods: {
		makeTree: function (jsonArray) {
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
		findParent: function (jsonArray, id) {
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



//register the grid component
Vue.component('aj-grid', {
    template:
		'<div><form action="?" method="GET" style="float:right;">\
			<input type="hidden" name="searchField" value="content" />\
			<input type="text" name="searchValue" placeholder="请输入搜索之关键字" class="ajaxjs-inputField" />\
			<button style="margin-top: 0;" class="ajaxjs-btn">搜索</button>\
		</form>\
        <table class="aj-grid ajaxjs-niceTable"><thead><tr>\
          <th v-for="key in columns" @click="sortBy(key)" :class="{ active: sortKey == key }">\
            {{ key | capitalize }}\
            <span class="arrow" :class="sortOrders[key] > 0 ? \'asc\' : \'dsc\'"></span>\
          </th></tr></thead>\
          <tbody>\
                <tr v-for="entry in filteredData">\
                  <td v-for="key in columns">{{entry[key]}}</td>\
            </tr></tbody></table></div>',
    props: {
        data: Array,
        columns: Array,
        filterKey: String
    },
    data: function () {
        var sortOrders = {};
        this.columns.forEach(function (key) {
            sortOrders[key] = 1;
        });
        return {
            sortKey: '',
            sortOrders: sortOrders
        };
    },
    computed: {
        filteredData: function () {
            var sortKey = this.sortKey;
            var filterKey = this.filterKey && this.filterKey.toLowerCase();
            var order = this.sortOrders[sortKey] || 1;
            var data = this.data;

            if (filterKey) {
                data = data.filter(function (row) {
                    return Object.keys(row).some(function (key) {
                        return String(row[key]).toLowerCase().indexOf(filterKey) > -1;
                    })
                });
            }

            if (sortKey) {
                data = data.slice().sort(function (a, b) {
                    a = a[sortKey];
                    b = b[sortKey];
                    return (a === b ? 0 : a > b ? 1 : -1) * order;
                });
            }

            return data;
        }
    },
    filters: {
        capitalize: function (str) {
            return str.charAt(0).toUpperCase() + str.slice(1);
        }
    },
    methods: {
        sortBy: function (key) {
            this.sortKey = key;
            this.sortOrders[key] = this.sortOrders[key] * -1;
        }
    }
});
