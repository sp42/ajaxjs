interface List extends Ajax {
    
}

namespace aj.list {
    export var base = {
        props: {
            apiUrl: { type: String, required: true },// JSON 接口地址
            hrefStr: { type: String, required: false },
            isPage: { type: Boolean, default: true }// 是否分页，false=读取所有数据
        },
        data(this: List) {
            return {
                result: [],		// 展示的数据
                baseParam: {},	// 每次请求都附带的参数，不可修改的
                extraParam: {},	// 与 baseParam 合并后每次请求可发送的，可以修改的
                realApiUrl: this.apiUrl // 真实发送的请求，可能包含 QueryString
            };
        }
    };
 

    export var pager = {
        data(this: List) {
            return {
                api: this.apiUrl, 	// 接口地址
                result: [],			// 展示的数据
                baseParam: this.initBaseParam,	// 每次请求都附带的参数，不可修改的
                extraParam: {},		// 与 baseParam 合并后每次请求可发送的，可以修改的
                pageSize: this.initPageSize,
                total: 0,
                totalPage: 0,
                pageStart: 0,
                currentPage: 0
            };
        },
        props: {
            initPageSize: { type: Number, required: false, default: 9 },
            apiUrl: String,
            autoLoad: { type: Boolean, default: true },	// 是否自动加载
            isPage: { type: Boolean, default: true },	// 是否分页，false=读取所有数据
            initBaseParam: { type: Object, default() { return {}; } }
        },
        methods: {
            // 分页，跳到第几页，下拉控件传入指定的页码
            jumpPageBySelect(ev: Event): void {
                var selectEl = ev.target;
                var currentPage = selectEl.options[selectEl.selectedIndex].value;
                this.pageStart = (Number(currentPage) - 1) * this.pageSize;
                this.ajaxGet();
            },
            onPageSizeChange($event: Event): void {
                this.pageSize = Number(e.target.value);
                this.count();
                this.ajaxGet();
            },
            count(): void {
                var totalPage = this.total / this.pageSize, yushu = this.total % this.pageSize;
                this.totalPage = parseInt(yushu == 0 ? totalPage : totalPage + 1);
                this.currentPage = (this.pageStart / this.pageSize) + 1;
            },
            previousPage(): void {
                this.pageStart -= this.pageSize;
                this.currentPage = (this.pageStart / this.pageSize) + 1;

                this.ajaxGet();
            },
            nextPage(): void {
                this.pageStart += this.pageSize;
                this.currentPage = (this.pageStart / this.pageSize) + 1;

                this.ajaxGet();
            },
        }
    };

    /**
     * 一般情况下不会单独使用这个组件
     */
    Vue.component('aj-pager', {
        mixins: [pager],
        template: `
            <footer class="aj-pager">
                <a v-if="pageStart > 0" href="#" @click="previousPage">上一页</a>
                <a v-if="(pageStart > 0 ) && (pageStart + pageSize < total)" style="text-decoration: none;">&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</a>
                <a v-if="pageStart + pageSize < total" href="#" @click="nextPage">下一页</a>
                <a href="javascript:;" @click="get"><i class="fa fa-refresh" aria-hidden="true"></i> 刷新</a>
                <input type="hidden" name="start" :value="pageStart" />
                页数：{{currentPage}}/{{totalPage}} 记录数：{{pageStart}}/{{total}}
                每页记录数： <input size="2" title="输入一个数字确定每页记录数" type="text" :value="pageSize" @change="onPageSizeChange" />
                跳转： 
                <select @change="jumpPageBySelect;">
                    <option :value="n" v-for="n in totalPage">{{n}}</option>
                </select>
            </footer>        
        `,
        methods: {
            get() {
                var param = aj.apply({}, this.baseParam);
                aj.apply(param, this.extraParam);
                aj.apply(param, { start: this.pageStart, limit: this.pageSize });

                aj.xhr.get(this.api, j => {
                    if (j.result) {
                        if (j.total == 0 || j.result.length == 0)
                            aj.alert('没有找到任何记录');

                        this.result = j.result;

                        if (this.isPage) {
                            this.total = j.total;
                            this.count();
                        }
                    }

                    this.$emit('onDataLoad', this.result);
                    this.$emit('pager-result', this.result);
                }, param);
            },
            // 复位
            reset() {
                this.total = this.totalPage = this.pageStart = this.currentPage = 0;
                this.pageSize = this.initPageSize;
            }
        }
    });
}