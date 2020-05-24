aj._list = {
	props: {
		apiUrl: {		// JSON 接口地址
			type : String,
			required : true
		},
		
		hrefStr: {
			type : String,
			required : false
		},
		isPage: {
			type : Boolean, 	// 是否分页，false=读取所有数据
			default : true
		}
	},
	
	data() {
		return {
			result : [],		// 展示的数据
			baseParam: {},		// 每次请求都附带的参数
			realApiUrl: this.apiUrl
		};
	}
};

// 简单列表
Vue.component('aj-simple-list', {
	mixins: [aj._list],
	template : '<ul class="aj-simple-list"><li v-for="(item, index) in result">\
				<slot v-bind="item">\
					<a :href="(hrefStr || \'\').replace(\'{id}\', item.id)" @click="show(item.id, index, $event)" :id="item.id">{{item.name}}</a>\
				</slot>\
			</li></ul>',
	mounted() {
		ajaxjs.xhr.get(this.realApiUrl, json => {
			aj.apply(this, json);
		}, this.baseParam);
	}
});


aj._pager = {
	data() {
		return {
			api: null, // 接口地址
			result : [],		// 展示的数据
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
			type : Number,
			required : false,
			default : 5
		}
	},
	methods: {
		// 分页，跳到第几页，下拉控件传入指定的页码
		jumpPageBySelect(e) {
			var selectEl = e.target;
			var currentPage = selectEl.options[selectEl.selectedIndex].value;
			this.pageStart = (Number(currentPage) - 1) * this.pageSize;
			this.ajaxGet();
		},
		onPageSizeChange(e) {
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
	template: '<footer>\
				<a v-if="pageStart > 0" href="#" @click="previousPage()">上一页</a> \
				<a v-if="(pageStart > 0 ) && (pageStart + pageSize < total)" style="text-decoration: none;">&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</a>\
				<a v-if="pageStart + pageSize < total" href="#" @click="nextPage()">下一页</a>\
				<div class="info">\
					<input type="hidden" name="start" :value="pageStart" />\
					页数：{{currentPage}}/{{totalPage}} 记录数：{{pageStart}}/{{total}}\
					每页记录数： <input size="2" title="输入一个数字确定每页记录数" type="text" class="aj-input" :value="pageSize" @change="onPageSizeChange($event)" />\
					跳转： <select @change="jumpPageBySelect($event);" class="aj-select" style="width:50px;">\
						<option :value="n" v-for="n in totalPage">{{n}}</option>\
					</select>\
				</div>\
			</footer>',
	mixins: [aj._pager],
	methods: {
		get() {
			aj.apply(this.baseParam, { start : this.pageStart, limit : this.pageSize });
			
			aj.xhr.get(this.api, json => {
				if(json.result) {
					this.result = json.result;
					this.total = json.total;
					this.count();
				}
				
				this.$emit('onDataLoad', this.result);	
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
			type : Boolean,
			default : true
		},
		autoLoadWhenReachedBottom : {	// 到底部是否自动加载下一页，通常在 移动端使用，这个应该是元素的 CSS Selector
			type : String,
			default: ''
		},
		isDataAppend: {
			type : Boolean, 	// 数据分页是否追加模式，默认不追加 = false。 App 一般采用追加模式
			default : false
		}
	},
	template: 
		'<div class="aj-page-list">\
			<ul><li v-for="(item, index) in result">\
				<slot v-bind="item">\
					<a href="#" @click="show(item.id, index, $event)" :id="item.id">{{item.name}}</a>\
				</slot>\
			</li></ul>\
			<footer v-show="isShowFooter">\
				<a v-if="pageStart > 0" href="#" @click="previousPage()">上一页</a> \
				<a v-if="(pageStart > 0 ) && (pageStart + pageSize < total)" style="text-decoration: none;">&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</a>\
				<a v-if="pageStart + pageSize < total" href="#" @click="nextPage()">下一页</a>\
				<div class="info">\
					<input type="hidden" name="start" :value="pageStart" />\
					页数：{{currentPage}}/{{totalPage}} 记录数：{{pageStart}}/{{total}}\
					每页记录数： <input size="2" title="输入一个数字确定每页记录数" type="text" :value="pageSize" @change="onPageSizeChange($event)" />\
					跳转： <select @change="jumpPageBySelect($event);">\
						<option :value="n" v-for="n in totalPage">{{n}}</option>\
					</select>\
				</div>\
			</footer><div v-show="!!autoLoadWhenReachedBottom" class="buttom"></div>\
		</div>',
	mounted() {
		ajaxjs.xhr.get(this.realApiUrl, this.doAjaxGet, {
			limit : this.pageSize
		});
		
		if(!!this.autoLoadWhenReachedBottom) {
			var scrollSpy = new aj.scrollSpy({
				scrollInElement : aj(this.autoLoadWhenReachedBottom),
				spyOn : thish.$el.$('.buttom')
			});
			
			scrollSpy.onScrollSpyBackInSight = e => {
				this.nextPage();
			};
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
    template:
		'<div><form action="?" method="GET" style="float:right;">\
			<input type="hidden" name="searchField" value="content" />\
			<input type="text" name="searchValue" placeholder="请输入搜索之关键字" class="aj-input" />\
			<button style="margin-top: 0;" class="aj-btn">搜索</button>\
		</form>\
        <table class="aj-grid ajaxjs-borderTable"><thead><tr>\
          <th v-for="key in columns" @click="sortBy(key)" :class="{ active: sortKey == key }">\
            {{ key | capitalize }}\
            <span class="arrow" :class="sortOrders[key] > 0 ? \'asc\' : \'dsc\'"></span>\
          </th></tr></thead>\
          <tbody>\
                <tr v-for="entry in filteredData">\
                  <td v-for="key in columns" v-html="entry[key]"></td>\
            </tr></tbody></table></div>',
    props: {
        data: Array,
        columns: Array,
        filterKey: String
    },
    data() {
        var sortOrders = {};
        this.columns.forEach(key => {
            sortOrders[key] = 1;
        });
        
        return {
            sortKey: '',
            sortOrders: sortOrders
        };
    },
    computed: {
        filteredData() {
            var sortKey = this.sortKey;
            var filterKey = this.filterKey && this.filterKey.toLowerCase();
            var order = this.sortOrders[sortKey] || 1;
            var data = this.data;

            if (filterKey) {
                data = data.filter(row => {
                    return Object.keys(row).some(key => {
                        return String(row[key]).toLowerCase().indexOf(filterKey) > -1;
                    })
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
 * @param {Elment}
 *            如果是在区域内滚动的话，则要传入滚动面板的元素，移动端会适用
 */
aj.scrollSpy = function(cfg) {
    var isScrollInElement = !!(cfg && cfg.scrollInElement);
    
    var handleScroll = function () {
        var currentViewPosition;
        
        if(isScrollInElement) {
        	currentViewPosition = cfg.scrollInElement.scrollTop + window.innerHeight;
        }else {
        	currentViewPosition = document.documentElement.scrollTop ? document.documentElement.scrollTop: document.body.scrollTop;
        }
        
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
    } else if (window.attachEvent) {
        window.attachEvent("onscroll", handleScroll);
    }
    
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


