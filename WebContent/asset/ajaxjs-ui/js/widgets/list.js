aj._list = {
	props : {
		apiUrl : {		// JSON 接口地址
			type : String,
			required : true
		}
	},
	
	data : function() {
		return {
			result : [],		// 展示的数据
			baseParam: {}		// 每次请求都附带的参数
		};
	}
};

// 简单列表
Vue.component('aj-simple-list', {
	mixins: [aj._list],
	
	template : '<ul class="aj-simple-list"><li v-for="(item, index) in result">\
				<slot v-bind="item">\
					<a href="#" @click="show(item.id, index, $event)" :id="item.id">{{item.name}}</a>\
				</slot>\
			</li></ul>',
	mounted : function() {
		ajaxjs.xhr.get(this.apiUrl, function(json) {
			aj.apply(this, json);
		}.bind(this), this.baseParam);
	}
});

/**
 * 列表控件
 */

// 分页
Vue.component('aj-page-list', {
	mixins: [aj._list],
	
	data : function() {
		return {
			pageSize : this.initPageSize,
			total : 0,
			totalPage :0,
			pageStart: 0,
			currentPage : 0
		};
	},
	props : {
		initPageSize : {
			type : Number,
			required : false,
			default : 5
		},
		isShowFooter : {
			type : Boolean,
			default : true
		},
		autoLoadWhenReachedBottom : {	// 到底部是否自动加载下一页，通常在 移动端使用，这个应该是元素的 CSS Selector
			type : String,
			default : ''
		}
	},
	template : 
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
	mounted : function() {
		ajaxjs.xhr.get(this.$props.apiUrl, function(json) {
			this.total = json.total;
			this.result = this.result.concat(json.result);

			//aj.apply(this, json);
			this.count();
		}.bind(this), {
			limit : this.pageSize
		});
		
		if(!!this.autoLoadWhenReachedBottom) {
			var scrollSpy = new aj.scrollSpy({
				scrollInElement : aj(this.autoLoadWhenReachedBottom),
				spyOn : this.$el.$('.buttom')
			});
			
			scrollSpy.onScrollSpyBackInSight = function (e) {
				this.nextPage();
			}.bind(this);
		}
	},
	
	created : function() {
		this.BUS.$on('base-param-change', this.onBaseParamChange.bind(this));
	},
	
	methods : {
		count: function () {
			var totalPage = this.total / this.pageSize, yushu = this.total % this.pageSize;
			this.totalPage = parseInt(yushu == 0 ? totalPage : totalPage + 1);
			this.currentPage = (this.pageStart / this.pageSize) + 1;
		},
		previousPage : function() {
			this.pageStart -= this.pageSize;
			this.currentPage = (this.pageStart / this.pageSize) + 1;
			
			this.ajaxGet();
		},
		nextPage : function() {
			this.pageStart += this.pageSize;
			this.currentPage = (this.pageStart / this.pageSize) + 1;
			
			this.ajaxGet();
		},
		onPageSizeChange : function(e) {
			this.pageSize = Number(e.target.value);
			this.count();
			this.ajaxGet();
		},
		ajaxGet : function () {
			var params = {};
			
			aj.apply(params, {
				start : this.pageStart, limit : this.pageSize
			});
			
			this.baseParam && aj.apply(params, this.baseParam);
			
			ajaxjs.xhr.get(this.$props.apiUrl, function(json) {
				this.total = json.total;
				this.result = this.result.concat(json.result);
				this.count();
			}.bind(this), params);
		},
		// 分页，跳到第几页，下拉控件传入指定的页码
		jumpPageBySelect : function (e) {
			var selectEl = e.target;
			this.currentPage = selectEl.options[selectEl.selectedIndex].value;
		},
		
		onBaseParamChange : function(params) {
			aj.apply(this.baseParam, params);
			
			this.pageStart = 0; // 每次 baseParam 被改变，都是从第一笔开始
			this.ajaxGet();
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