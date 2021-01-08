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
                initBaseParam: { type: Object, default: function () { return {}; } }
            },
            data: function () {
                return {
                    api: this.apiUrl,
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
                onPageSizeChange: function (ev) {
                    this.pageSize = Number(ev.target.value);
                    this.count();
                    this.getData();
                },
                count: function () {
                    var totalPage = this.total / this.pageSize, yushu = this.total % this.pageSize;
                    this.totalPage = parseInt(String(yushu == 0 ? totalPage : totalPage + 1));
                    this.currentPage = (this.pageStart / this.pageSize) + 1;
                },
                previousPage: function () {
                    this.pageStart -= this.pageSize;
                    this.currentPage = (this.pageStart / this.pageSize) + 1;
                    this.getData();
                },
                nextPage: function () {
                    this.pageStart += this.pageSize;
                    this.currentPage = (this.pageStart / this.pageSize) + 1;
                    this.getData();
                },
            }
        };
        /**
         * 一般情况下不会单独使用这个组件
         */
        Vue.component('aj-pager', {
            mixins: [list.datastore],
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
                /**
                 * 复位
                 *
                 * @param this
                 */
                reset: function () {
                    this.total = this.totalPage = this.pageStart = this.currentPage = 0;
                    this.pageSize = this.initPageSize;
                }
            }
        });
    })(list = aj.list || (aj.list = {}));
})(aj || (aj = {}));
