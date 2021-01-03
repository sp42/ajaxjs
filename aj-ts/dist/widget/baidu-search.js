"use strict";
Vue.component('aj-baidu-search', {
    template: "\n        <div class=\"aj-baidu-search\"><form method=\"GET\" action=\"http://www.baidu.com/baidu\" onsubmit=\"//return g(this);\">\n            <input type=\"text\" name=\"word\" placeholder=\"\u8BF7\u8F93\u5165\u641C\u7D22\u4E4B\u5173\u952E\u5B57\" />\n            <input name=\"tn\" value=\"bds\" type=\"hidden\" />\n            <input name=\"cl\" value=\"3\" type=\"hidden\" />\n            <input name=\"ct\" value=\"2097152\" type=\"hidden\" />\n            <input name=\"si\" :value=\"getSiteDomainName\" type=\"hidden\" />\n            <div class=\"searchBtn\" onclick=\"this.parentNode.submit();\"></div>\n        </form></div>\n    ",
    props: ['siteDomainName'],
    computed: {
        getSiteDomainName: function () {
            return this.siteDomainName || location.host || document.domain;
        }
    }
});
