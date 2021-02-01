"use strict";
var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (Object.prototype.hasOwnProperty.call(b, p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var __makeTemplateObject = (this && this.__makeTemplateObject) || function (cooked, raw) {
    if (Object.defineProperty) { Object.defineProperty(cooked, "raw", { value: raw }); } else { cooked.raw = raw; }
    return cooked;
};
var aj;
(function (aj) {
    var widget;
    (function (widget) {
        /**
         * Baidu 自定义搜索
         */
        var BaiduSearch = /** @class */ (function (_super) {
            __extends(BaiduSearch, _super);
            function BaiduSearch() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.name = 'aj-widget-baidu-search';
                _this.template = html(__makeTemplateObject(["\n            <div class=\"aj-widget-baidu-search\">\n                <form method=\"GET\" action=\"http://www.baidu.com/baidu\" onsubmit=\"//return g(this);\">\n                    <input type=\"text\" name=\"word\" placeholder=\"\u8BF7\u8F93\u5165\u641C\u7D22\u4E4B\u5173\u952E\u5B57\" />\n                    <input name=\"tn\" value=\"bds\" type=\"hidden\" />\n                    <input name=\"cl\" value=\"3\" type=\"hidden\" />\n                    <input name=\"ct\" value=\"2097152\" type=\"hidden\" />\n                    <input name=\"si\" :value=\"getSiteDomainName\" type=\"hidden\" />\n                    <div class=\"searchBtn\" onclick=\"this.parentNode.submit();\"></div>\n                </form>\n            </div>\n        "], ["\n            <div class=\"aj-widget-baidu-search\">\n                <form method=\"GET\" action=\"http://www.baidu.com/baidu\" onsubmit=\"//return g(this);\">\n                    <input type=\"text\" name=\"word\" placeholder=\"\u8BF7\u8F93\u5165\u641C\u7D22\u4E4B\u5173\u952E\u5B57\" />\n                    <input name=\"tn\" value=\"bds\" type=\"hidden\" />\n                    <input name=\"cl\" value=\"3\" type=\"hidden\" />\n                    <input name=\"ct\" value=\"2097152\" type=\"hidden\" />\n                    <input name=\"si\" :value=\"getSiteDomainName\" type=\"hidden\" />\n                    <div class=\"searchBtn\" onclick=\"this.parentNode.submit();\"></div>\n                </form>\n            </div>\n        "]));
                _this.siteDomainName = String;
                _this.computed = {
                    getSiteDomainName: function () {
                        //@ts-ignore
                        return this.siteDomainName || location.host || document.domain;
                    }
                };
                return _this;
            }
            return BaiduSearch;
        }(aj.VueComponent));
        widget.BaiduSearch = BaiduSearch;
        new BaiduSearch().register();
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));
