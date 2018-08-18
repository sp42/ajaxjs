/**
 * 列表控件
 */

// 分页
Vue.component('aj-page-list', {
	data : function() {
		return {
			pageSize : this.initPageSize,
			total : 0,
			totalPage :0,
			pageStart: 0,
			currentPage : 0,
			result : [],
			baseParam: {}
		};
	},
	props : {
		apiUrl : {		// JSON 接口地址
			type : String,
			required : true
		},
		initPageSize : {
			type : Number,
			required : false,
			default : 5
		}
	},
	template : 
		'<div class="aj-page-list">\
			<ul><li v-for="(item, index) in result">\
				<slot v-bind="item">\
				<a href="#" @click="show(item.id, index, $event)" :id="item.id">{{item.name}}</a>\
				</slot>\
			</li></ul>\
			<footer>\
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
			</footer>\
		</div>',
	mounted : function() {
		ajaxjs.xhr.get(this.$props.apiUrl, function(json) {
			aj.apply(this, json);
			this.count();
		}.bind(this), {
			limit : this.pageSize
		});
	},
	
	created : function(){
		Vue.BUS.$on('base-param-change', this.onBaseParamChange.bind(this));
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
				aj.apply(this, json);
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
			this.ajaxGet();
		}
	}
});