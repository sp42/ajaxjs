/**
 * 列表控件
 */

// 分页
Vue.component('aj-page-list', {
	data : function() {
		return {
			pageSize : 10,
			total : 0,
			totalPage :0,
			pageStart: 0,
			currentPage : 0,
			result : []
		};
	},
	props : {
		apiUrl : {		// JSON 接口地址
			type : String,
			required : true
		}
	},
	template : 
		'<div class="center">\
			<ul><li v-for="(item, index) in result">\
				<a href="#" @click="show(item.id, index, $event)" :id="item.id">{{item.name}}</a>\
			</li></ul>\
			<footer>\
				<a v-if="pageStart > 0" href="#" @click="previousPage()">上一页</a> \
				<a v-if="(pageStart > 0 ) && (pageStart + pageSize < total)" style="text-decoration: none;">&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</a>\
				<a v-if="pageStart + pageSize < total" href="#" @click="nextPage()">下一页</a>\
				<div class="info">\
					<input type="hidden" name="start" :value="pageStart" />\
					页数：{{currentPage}}/{{totalPage}} 记录数：{{pageStart}}/{{total}}\
					每页记录数： <input size="4" title="输入一个数字确定每页记录数" type="text" :value="pageSize" @change="onPageSizeChange($event)" />\
					跳转： <select onchange="jumpPage(this);" style="text-align: center; width: 40px; height: 22px;" class="ajaxjs-select">\
						<option value="5" selected>2</option>\
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
			var self = this;
			ajaxjs.xhr.get(this.$props.apiUrl, function(json) {
				self.result = json.result;
			}, {
				start : this.pageStart, limit : this.pageSize
			});
		},
		// 分页，跳到第几页，下拉控件传入指定的页码
		jumpPageBySelect : function (selectEl) {
			var start = selectEl.options[selectEl.selectedIndex].value;
			var go2 = location.search;

			if (go2.indexOf('start=') != -1) {
				go2 = go2.replace(/start=\d+/, 'start=' + start);
			} else {
				go2 += go2.indexOf('?') != -1 ? ('&start=' + start) : ('?start=' + start);
			}

			location.assign(go2);
		}
	}
});