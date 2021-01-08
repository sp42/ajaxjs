/**
 * 列表控件
 */
Vue.component('aj-page-list', {
	mixins: [aj.list.base, aj.list.pager],
	template: `
		<div class="aj-page-list">
			<ul>
				<li v-for="(item, index) in result">
					<slot v-bind="item">
						<a href="#" @click="show(item.id, index, $event)" :id="item.id">{{item.name}}</a>
					</slot>
				</li>
			</ul>
			<footer v-show="isShowFooter">
				<a v-if="pageStart > 0" href="#" @click="previousPage()">上一页</a>
				<a v-if="(pageStart > 0 ) && (pageStart + pageSize < total)" style="text-decoration: none;">&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</a>
				<a v-if="pageStart + pageSize < total" href="#" @click="nextPage()">下一页</a>
				
				<input type="hidden" name="start" :value="pageStart" />
				页数：{{currentPage}}/{{totalPage}} 记录数：{{pageStart}}/{{total}}
				每页记录数： <input size="2" title="输入一个数字确定每页记录数" type="text" :value="pageSize" @change="onPageSizeChange($event)" />
				跳转： <select @change="jumpPageBySelect($event);">
					<option :value="n" v-for="n in totalPage">{{n}}</option>
				</select>
			</footer><div v-show="!!autoLoadWhenReachedBottom" class="buttom"></div>
		</div>
	`,
	props: {
		isShowFooter: { type: Boolean, default: true },			 // 到底部是否自动加载下一页，通常在 移动端使用，这个应该是元素的 CSS Selector
		autoLoadWhenReachedBottom: { type: String, default: '' },// 数据分页是否追加模式，默认不追加 = false。 App 一般采用追加模式
		isDataAppend: { type: Boolean, default: false }
	},
	mounted(this: List): void {
		this.autoLoad && aj.xhr.get(this.realApiUrl, this.doAjaxGet, { limit: this.pageSize });

		if (!!this.autoLoadWhenReachedBottom) {
			// var scrollSpy = new aj.scrollSpy({ scrollInElement: aj(this.autoLoadWhenReachedBottom), spyOn: thish.$el.$('.buttom') });
			// scrollSpy.onScrollSpyBackInSight = e => this.nextPage();
		}
	},

	created(): void {
		this.BUS.$on('base-param-change', this.onBaseParamChange.bind(this));
	},

	methods: {
		doAjaxGet(this: List, json): void {
			if (this.isPage) {
				this.total = json.total;
				this.result = this.isDataAppend ? this.result.concat(json.result) : json.result;
				this.count();
			} else
				this.result = json.result;
		},
		ajaxGet(this: List): void {
			var params = {};
			aj.apply(params, { start: this.pageStart, limit: this.pageSize });
			this.baseParam && aj.apply(params, this.baseParam);
			aj.xhr.get(this.realApiUrl, this.doAjaxGet, params);
		},
		onBaseParamChange(params): void {
			aj.apply(this.baseParam, params);

			this.pageStart = 0; // 每次 baseParam 被改变，都是从第一笔开始
			this.ajaxGet();
		}
	},
	watch: {
		baseParam(this: List, index, oldIndex): void {
			this.ajaxGet();
		}
	}
});