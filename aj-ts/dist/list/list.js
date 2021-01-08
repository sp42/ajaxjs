"use strict";
/**
 * 列表控件
 */
Vue.component('aj-page-list', {
    mixins: [aj.list.base, aj.list.pager],
    template: "\n\t\t<div class=\"aj-page-list\">\n\t\t\t<ul>\n\t\t\t\t<li v-for=\"(item, index) in result\">\n\t\t\t\t\t<slot v-bind=\"item\">\n\t\t\t\t\t\t<a href=\"#\" @click=\"show(item.id, index, $event)\" :id=\"item.id\">{{item.name}}</a>\n\t\t\t\t\t</slot>\n\t\t\t\t</li>\n\t\t\t</ul>\n\t\t\t<footer v-show=\"isShowFooter\">\n\t\t\t\t<a v-if=\"pageStart > 0\" href=\"#\" @click=\"previousPage()\">\u4E0A\u4E00\u9875</a>\n\t\t\t\t<a v-if=\"(pageStart > 0 ) && (pageStart + pageSize < total)\" style=\"text-decoration: none;\">&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</a>\n\t\t\t\t<a v-if=\"pageStart + pageSize < total\" href=\"#\" @click=\"nextPage()\">\u4E0B\u4E00\u9875</a>\n\t\t\t\t\n\t\t\t\t<input type=\"hidden\" name=\"start\" :value=\"pageStart\" />\n\t\t\t\t\u9875\u6570\uFF1A{{currentPage}}/{{totalPage}} \u8BB0\u5F55\u6570\uFF1A{{pageStart}}/{{total}}\n\t\t\t\t\u6BCF\u9875\u8BB0\u5F55\u6570\uFF1A <input size=\"2\" title=\"\u8F93\u5165\u4E00\u4E2A\u6570\u5B57\u786E\u5B9A\u6BCF\u9875\u8BB0\u5F55\u6570\" type=\"text\" :value=\"pageSize\" @change=\"onPageSizeChange($event)\" />\n\t\t\t\t\u8DF3\u8F6C\uFF1A <select @change=\"jumpPageBySelect($event);\">\n\t\t\t\t\t<option :value=\"n\" v-for=\"n in totalPage\">{{n}}</option>\n\t\t\t\t</select>\n\t\t\t</footer><div v-show=\"!!autoLoadWhenReachedBottom\" class=\"buttom\"></div>\n\t\t</div>\n\t",
    props: {
        isShowFooter: { type: Boolean, default: true },
        autoLoadWhenReachedBottom: { type: String, default: '' },
        isDataAppend: { type: Boolean, default: false }
    },
    mounted: function () {
        this.autoLoad && aj.xhr.get(this.realApiUrl, this.doAjaxGet, { limit: this.pageSize });
        if (!!this.autoLoadWhenReachedBottom) {
            // var scrollSpy = new aj.scrollSpy({ scrollInElement: aj(this.autoLoadWhenReachedBottom), spyOn: thish.$el.$('.buttom') });
            // scrollSpy.onScrollSpyBackInSight = e => this.nextPage();
        }
    },
    created: function () {
        this.BUS.$on('base-param-change', this.onBaseParamChange.bind(this));
    },
    methods: {
        doAjaxGet: function (json) {
            if (this.isPage) {
                this.total = json.total;
                this.result = this.isDataAppend ? this.result.concat(json.result) : json.result;
                this.count();
            }
            else
                this.result = json.result;
        },
        ajaxGet: function () {
            var params = {};
            aj.apply(params, { start: this.pageStart, limit: this.pageSize });
            this.baseParam && aj.apply(params, this.baseParam);
            aj.xhr.get(this.realApiUrl, this.doAjaxGet, params);
        },
        onBaseParamChange: function (params) {
            aj.apply(this.baseParam, params);
            this.pageStart = 0; // 每次 baseParam 被改变，都是从第一笔开始
            this.ajaxGet();
        }
    },
    watch: {
        baseParam: function (index, oldIndex) {
            this.ajaxGet();
        }
    }
});
