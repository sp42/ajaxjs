"use strict";
var aj;
(function (aj) {
    var list;
    (function (list) {
        list.datastore = {
            props: {
                apiUrl: { type: String, required: true },
                hrefStr: { type: String, required: false },
                isPage: { type: Boolean, default: true },
                initPageSize: { type: Number, required: false, default: 9 },
                autoLoad: { type: Boolean, default: true },
                initBaseParam: { type: Object, default: function () { return {}; } },
                pageParamNames: { type: Array, default: function () { return ['start', 'limit']; } },
                onLoad: Function
            },
            data: function () {
                return {
                    baseParam: this.initBaseParam,
                    result: [],
                    extraParam: {},
                    realApiUrl: this.apiUrl,
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
                jumpPageBySelect: function (ev) {
                    var selectEl = ev.target;
                    var currentPage = selectEl.options[selectEl.selectedIndex].value;
                    this.pageStart = (Number(currentPage) - 1) * this.pageSize;
                    this.getData();
                },
                /**
                 * PageSize 改变时候重新分页
                 *
                 * @param this
                 * @param ev
                 */
                onPageSizeChange: function (ev) {
                    this.pageSize = Number(ev.target.value);
                    this.count();
                    this.getData();
                },
                count: function () {
                    var totalPage = this.total / this.pageSize, yushu = this.total % this.pageSize;
                    this.totalPage = parseInt(String(yushu == 0 ? totalPage : totalPage + 1));
                    //@ts-ignore
                    this.currentPage = parseInt((this.pageStart / this.pageSize) + 1);
                },
                /**
                 * 前一页
                 *
                 * @param this
                 */
                previousPage: function () {
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
                nextPage: function () {
                    this.pageStart += this.pageSize;
                    this.currentPage = (this.pageStart / this.pageSize) + 1;
                    this.getData();
                },
            }
        };
        var pageParams = {
            start: 'start',
            limit: 'limit'
        };
        /**
         * 一般情况下不会单独使用这个组件
         */
        Vue.component('aj-list', {
            mixins: [list.datastore],
            template: "\n\t\t\t<div class=\"aj-list\">\n\t\t\t\t<ul>\n\t\t\t\t\t<li v-for=\"(item, index) in result\">\n\t\t\t\t\t\t<slot v-bind=\"item\">\n\t\t\t\t\t\t\t<a href=\"#\" @click=\"show(item.id, index, $event)\" :id=\"item.id\">{{item.name}}</a>\n\t\t\t\t\t\t</slot>\n\t\t\t\t\t</li>\n\t\t\t\t</ul>\n\t\t\t\t<footer v-if=\"isPage\" class=\"pager\">\n\t\t\t\t\t<a v-if=\"pageStart > 0\" href=\"#\" @click=\"previousPage\">\u4E0A\u4E00\u9875</a>\n\t\t\t\t\t<a v-if=\"(pageStart > 0 ) && (pageStart + pageSize < total)\" style=\"text-decoration: none;\">&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</a>\n\t\t\t\t\t<a v-if=\"pageStart + pageSize < total\" href=\"#\" @click=\"nextPage\">\u4E0B\u4E00\u9875</a>\n\t\t\t\t\t<a href=\"javascript:;\" @click=\"getData\"><i class=\"fa fa-refresh\" aria-hidden=\"true\"></i> \u5237\u65B0</a>\n\t\t\t\t\t<input type=\"hidden\" name=\"start\" :value=\"pageStart\" />\n\t\t\t\t\t\u9875\u6570\uFF1A{{currentPage}}/{{totalPage}} \u8BB0\u5F55\u6570\uFF1A{{pageStart}}/{{total}}\n\t\t\t\t\t\u6BCF\u9875\u8BB0\u5F55\u6570\uFF1A <input size=\"2\" title=\"\u8F93\u5165\u4E00\u4E2A\u6570\u5B57\u786E\u5B9A\u6BCF\u9875\u8BB0\u5F55\u6570\" type=\"text\" :value=\"pageSize\" @change=\"onPageSizeChange\" />\n\t\t\t\t\t\u8DF3\u8F6C\uFF1A \n\t\t\t\t\t<select @change=\"jumpPageBySelect\">\n\t\t\t\t\t\t<option :value=\"n\" v-for=\"n in totalPage\">{{n}}</option>\n\t\t\t\t\t</select>\n\t\t\t\t</footer> \n\t\t\t\t<div v-show=\"!!autoLoadWhenReachedBottom\" class=\"buttom\"></div>\n\t\t\t</div>\n\t\t",
            props: {
                isShowFooter: { type: Boolean, default: true },
                autoLoadWhenReachedBottom: { type: String, default: '' },
                isDataAppend: { type: Boolean, default: false }
            },
            mounted: function () {
                this.autoLoad && this.getData();
                // this.BUS.$on('base-param-change', this.onBaseParamChange.bind(this));
                if (!!this.autoLoadWhenReachedBottom) {
                    // var scrollSpy = new aj.scrollSpy({ scrollInElement: aj(this.autoLoadWhenReachedBottom), spyOn: thish.$el.$('.buttom') });
                    // scrollSpy.onScrollSpyBackInSight = e => this.nextPage();
                }
            },
            methods: {
                foo: function () {
                    window.alert(9);
                },
                getData: function () {
                    var _this = this;
                    this.lastRequestParam = {};
                    aj.apply(this.lastRequestParam, this.baseParam);
                    aj.apply(this.lastRequestParam, this.extraParam);
                    initPageParams.call(this);
                    aj.xhr.get(this.apiUrl, this.onLoad || (function (j) {
                        if (j.result) {
                            if (j.total == 0 || j.result.length == 0)
                                aj.alert('没有找到任何记录');
                            _this.result = j.result;
                            if (_this.isPage) {
                                _this.total = j.total;
                                _this.count();
                            }
                        }
                        _this.$emit('onDataLoad', _this.result);
                        _this.$emit('pager-result', _this.result);
                    }), this.lastRequestParam);
                },
                /**
                 * 复位
                 *
                 * @param this
                 */
                reset: function () {
                    this.total = this.totalPage = this.pageStart = this.currentPage = 0;
                    this.pageSize = this.initPageSize;
                },
                doAjaxGet: function (j) {
                    if (this.isPage) {
                        this.total = j.total;
                        //@ts-ignore
                        this.result = this.isDataAppend ? this.result.concat(j.result) : j.result;
                        this.count();
                    }
                    else
                        this.result = j.result;
                },
                onBaseParamChange: function (params) {
                    aj.apply(this.baseParam, params);
                    this.pageStart = 0; // 每次 baseParam 被改变，都是从第一笔开始
                    this.getData();
                }
            },
            watch: {
                baseParam: function () {
                    this.getData();
                }
            }
        });
        /**
         * 生成分页参数的名字
         *
         * @param this
         */
        function initPageParams() {
            var params = {};
            params[this.pageParamNames[0]] = this.pageStart;
            params[this.pageParamNames[1]] = this.pageSize;
            this.isPage && aj.apply(this.lastRequestParam, params);
        }
    })(list = aj.list || (aj.list = {}));
})(aj || (aj = {}));
