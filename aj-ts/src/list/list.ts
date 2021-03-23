

namespace aj.list {
	/**
	 * 分页列表专用的结果数据
	 */
	interface PageListRepsonseResult extends RepsonseResult {
		/**
		 * 结果总数
		 */
		total: number;
	}

	/**
	 * 列表数据
	 */
	interface DataStore extends Ajax {
		/**
		 * 是否分页，false=读取所有数据
		 */
		isPage: boolean;

		pageStart: number;

		pageSize: number;

		initPageSize: number;

		total: number;

		/**
		 * 请求结果
		 */
		result: BaseObject[];

		totalPage: number;

		currentPage: number;

		/**
		 * 默认的分页参数其名字
		 */
		pageParamNames: string[];

		/**
		 * 计算分页
		 */
		count(): void;
	}

	/**
	 * 本地数据仓库
	 * 一般情况下不会单独使用这个组件
	 */
	export var datastore = {
		props: {
			apiUrl: { type: String, required: true },                       // JSON 接口地址
			isPage: { type: Boolean, default: true },
			initPageSize: { type: Number, required: false, default: 9 },
			isAutoLoad: { type: Boolean, default: true },
			baseParam: { type: Object, default() { return {}; } },
			pageParamNames: { type: Array, default() { return ['start', 'limit']; } }, 	// 默认的分页参数其名字
			onLoad: Function
		},
		data(this: DataStore) {
			return {
				result: [],
				extraParam: {},	                // 与 baseParam 合并后每次请求可发送的，可以修改的
				pageSize: this.initPageSize,
				total: 0,
				totalPage: 0,
				pageStart: 0,
				currentPage: 0
			};
		},

		methods: {
			/**
			 * 分页，跳到第几页，下拉控件传入指定的页码
			 * 
			 * @param this 
			 * @param ev 
			 */
			jumpPageBySelect(this: DataStore, ev: Event): void {
				let selectEl: HTMLSelectElement = <HTMLSelectElement>ev.target;
				let currentPage: string = selectEl.options[selectEl.selectedIndex].value;

				this.pageStart = (Number(currentPage) - 1) * this.pageSize;
				this.getData();
			},

			/**
			 * PageSize 改变时候重新分页
			 * 
			 * @param this 
			 * @param ev 
			 */
			onPageSizeChange(this: DataStore, ev: Event): void {
				this.pageSize = Number((<HTMLInputElement>ev.target).value);
				this.count();
				this.getData();
			},

			count(this: DataStore): void {
				let totalPage: number = this.total / this.pageSize, yushu: number = this.total % this.pageSize;
				this.totalPage = parseInt(String(yushu == 0 ? totalPage : totalPage + 1));
				//@ts-ignore
				this.currentPage = parseInt((this.pageStart / this.pageSize) + 1);
			},

			/**
			 * 前一页
			 * 
			 * @param this 
			 */
			previousPage(this: DataStore): void {
				this.pageStart -= this.pageSize;
				//@ts-ignore
				this.currentPage = parseInt((this.pageStart / this.pageSize) + 1);

				this.getData();
			},

			/**
			 * 下一页
			 * 
			 * @param this
			 */
			nextPage(this: DataStore): void {
				this.pageStart += this.pageSize;
				this.currentPage = (this.pageStart / this.pageSize) + 1;

				this.getData();
			}
		}
	};

	/**
	 * 列表控件
	 */
	interface List extends DataStore, Vue {
		/**
		 * 数据分页是否追加模式，默认不追加 = false。 App 一般采用追加模式
		 */
		isDataAppend: boolean;

		/**
		 * 到达底部是否自动加载下一页，通常在 移动端使用，这个应该是元素的 CSS Selector
		 */
		autoLoadWhenReachedBottom: boolean;
	}

	Vue.component('aj-list', {
		mixins: [datastore],
		template: html`
			<div class="aj-list">
				<slot name="header" v-if="total != 0"></slot>
				<ul v-if="showDefaultUi && (total != 0)">
					<li v-for="(item, index) in result">
						<slot v-bind="item">
							<a href="#" @click="show(item.id, index, $event)" :id="item.id">{{item.name}}</a>
						</slot>
					</li>
				</ul>
				<div class="no-data" v-show="total == 0">未有任何数据</div>
				<footer v-if="isPage" class="pager">
					<a v-if="pageStart > 0" href="#" @click="previousPage">上一页</a>
					<a v-if="(pageStart > 0 ) && (pageStart + pageSize < total)"
						style="text-decoration: none;">&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</a>
					<a v-if="pageStart + pageSize < total" href="#" @click="nextPage">下一页</a>
					<a href="javascript:;" @click="getData"><i class="fa fa-refresh" aria-hidden="true"></i> 刷新</a>
					<input type="hidden" name="start" :value="pageStart" />
					页数：{{currentPage}}/{{totalPage}} 记录数：{{pageStart}}/{{total}}
					每页记录数： <input size="2" title="输入一个数字确定每页记录数" type="text" :value="pageSize" @change="onPageSizeChange" />
					跳转：
					<select @change="jumpPageBySelect">
						<option :value="n" v-for="n in totalPage">{{n}}</option>
					</select>
				</footer>
				<div v-show="!!autoLoadWhenReachedBottom" class="buttom"></div>
			</div>
		`,
		props: {
			showDefaultUi: { type: Boolean, default: true },		 // 如果只是单纯作为分页组件，那么则不需要 UI
			isShowFooter: { type: Boolean, default: true },			 // 是否显示分页 UI
			hrefStr: { type: String, required: false },
			autoLoadWhenReachedBottom: { type: String, default: '' },
			isDataAppend: { type: Boolean, default: false }
		},
		mounted(this: List): void {
			this.isAutoLoad && this.getData();

			// this.BUS.$on('base-param-change', this.onExtraParamChange.bind(this));
			if (!!this.autoLoadWhenReachedBottom) {
				// var scrollSpy = new aj.scrollSpy({ scrollInElement: aj(this.autoLoadWhenReachedBottom), spyOn: thish.$el.$('.buttom') });
				// scrollSpy.onScrollSpyBackInSight = e => this.nextPage();
			}
		},
		methods: {
			getData(this: List): void {
				this.lastRequestParam = {};
				aj.apply(this.lastRequestParam, this.baseParam);
				aj.apply(this.lastRequestParam, this.extraParam);
				initPageParams.call(this);

				xhr.get(this.apiUrl, this.onLoad || ((j: PageListRepsonseResult) => {
					if (j.result) {
						if (j.total === undefined)
							aj.alert('JSON 缺少 total 字段');

						if (j.total == 0 || j.result.length == 0)
							aj.alert('没有找到任何记录');

						this.result = j.result;

						if (this.isPage) {
							this.total = j.total;
							this.count();
						}
					}

					this.$emit('pager-result', this.result);
				}), this.lastRequestParam);
			},

			/**
			 * 复位
			 * 
			 * @param this 
			 */
			reset(this: List): void {
				this.total = this.totalPage = this.pageStart = this.currentPage = 0;
				this.pageSize = this.initPageSize;
			},

			onExtraParamChange(this: List, params: JsonParam): void {
				aj.apply(this.extraParam, params);

				this.pageStart = 0; // 每次 extraParam 被改变，都是从第一笔开始
				this.getData();
			}
		},
		watch: {
			extraParam(this: List): void {
				this.getData();
			}
		}
	});

	/**
	 * 生成分页参数的名字
	 * 
	 * @param this 
	 */
	function initPageParams(this: List) {
		let params: { [key: string]: number } = {};
		params[this.pageParamNames[0]] = this.pageStart;
		params[this.pageParamNames[1]] = this.pageSize;

		this.isPage && aj.apply(this.lastRequestParam, params);
	}
}