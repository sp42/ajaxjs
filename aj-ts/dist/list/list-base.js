"use strict";
var aj;
(function (aj) {
    var list;
    (function (list) {
        list.base = {
            props: {
                apiUrl: { type: String, required: true },
                hrefStr: { type: String, required: false },
                isPage: { type: Boolean, default: true } // 是否分页，false=读取所有数据
            },
            data: function () {
                return {
                    result: [],
                    baseParam: {},
                    extraParam: {},
                    realApiUrl: this.apiUrl // 真实发送的请求，可能包含 QueryString
                };
            }
        };
        list.pager = {
            data: function () {
                return {
                    api: this.apiUrl,
                    result: [],
                    baseParam: this.initBaseParam,
                    extraParam: {},
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
                autoLoad: { type: Boolean, default: true },
                isPage: { type: Boolean, default: true },
                initBaseParam: { type: Object, default: function () { return {}; } }
            },
            methods: {
                // 分页，跳到第几页，下拉控件传入指定的页码
                jumpPageBySelect: function (ev) {
                    var selectEl = ev.target;
                    var currentPage = selectEl.options[selectEl.selectedIndex].value;
                    this.pageStart = (Number(currentPage) - 1) * this.pageSize;
                    this.ajaxGet();
                },
                onPageSizeChange: function ($event) {
                    this.pageSize = Number(e.target.value);
                    this.count();
                    this.ajaxGet();
                },
                count: function () {
                    var totalPage = this.total / this.pageSize, yushu = this.total % this.pageSize;
                    this.totalPage = parseInt(yushu == 0 ? totalPage : totalPage + 1);
                    this.currentPage = (this.pageStart / this.pageSize) + 1;
                },
                previousPage: function () {
                    this.pageStart -= this.pageSize;
                    this.currentPage = (this.pageStart / this.pageSize) + 1;
                    this.ajaxGet();
                },
                nextPage: function () {
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
            mixins: [list.pager],
            template: "\n            <footer class=\"aj-pager\">\n                <a v-if=\"pageStart > 0\" href=\"#\" @click=\"previousPage\">\u4E0A\u4E00\u9875</a>\n                <a v-if=\"(pageStart > 0 ) && (pageStart + pageSize < total)\" style=\"text-decoration: none;\">&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</a>\n                <a v-if=\"pageStart + pageSize < total\" href=\"#\" @click=\"nextPage\">\u4E0B\u4E00\u9875</a>\n                <a href=\"javascript:;\" @click=\"get\"><i class=\"fa fa-refresh\" aria-hidden=\"true\"></i> \u5237\u65B0</a>\n                <input type=\"hidden\" name=\"start\" :value=\"pageStart\" />\n                \u9875\u6570\uFF1A{{currentPage}}/{{totalPage}} \u8BB0\u5F55\u6570\uFF1A{{pageStart}}/{{total}}\n                \u6BCF\u9875\u8BB0\u5F55\u6570\uFF1A <input size=\"2\" title=\"\u8F93\u5165\u4E00\u4E2A\u6570\u5B57\u786E\u5B9A\u6BCF\u9875\u8BB0\u5F55\u6570\" type=\"text\" :value=\"pageSize\" @change=\"onPageSizeChange\" />\n                \u8DF3\u8F6C\uFF1A \n                <select @change=\"jumpPageBySelect;\">\n                    <option :value=\"n\" v-for=\"n in totalPage\">{{n}}</option>\n                </select>\n            </footer>        \n        ",
            methods: {
                get: function () {
                    var _this = this;
                    var param = aj.apply({}, this.baseParam);
                    aj.apply(param, this.extraParam);
                    aj.apply(param, { start: this.pageStart, limit: this.pageSize });
                    aj.xhr.get(this.api, function (j) {
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
                    }, param);
                },
                // 复位
                reset: function () {
                    this.total = this.totalPage = this.pageStart = this.currentPage = 0;
                    this.pageSize = this.initPageSize;
                }
            }
        });
    })(list = aj.list || (aj.list = {}));
})(aj || (aj = {}));
