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
/**
 * 调整正文字体大小
 */
var aj;
(function (aj) {
    var widget;
    (function (widget) {
        var page;
        (function (page) {
            var AdjustFontSize = /** @class */ (function (_super) {
                __extends(AdjustFontSize, _super);
                function AdjustFontSize() {
                    var _this = _super !== null && _super.apply(this, arguments) || this;
                    _this.name = 'aj-adjust-font-size';
                    _this.template = html(__makeTemplateObject(["\n            <div class=\"aj-adjust-font-size\">\n                <span>\u5B57\u4F53\u5927\u5C0F</span>\n                <ul @click=\"onClk\">\n                    <li><label><input type=\"radio\" name=\"fontSize\" /> \u5C0F</label></li>\n                    <li><label><input type=\"radio\" name=\"fontSize\" /> \u4E2D</label></li>\n                    <li><label><input type=\"radio\" name=\"fontSize\" /> \u5927</label></li>\n                </ul>\n            </div>\n        "], ["\n            <div class=\"aj-adjust-font-size\">\n                <span>\u5B57\u4F53\u5927\u5C0F</span>\n                <ul @click=\"onClk\">\n                    <li><label><input type=\"radio\" name=\"fontSize\" /> \u5C0F</label></li>\n                    <li><label><input type=\"radio\" name=\"fontSize\" /> \u4E2D</label></li>\n                    <li><label><input type=\"radio\" name=\"fontSize\" /> \u5927</label></li>\n                </ul>\n            </div>\n        "]));
                    _this.articleTarget = { type: String, default: 'article p' }; // 正文所在的位置，通过 CSS Selector 定位
                    return _this;
                }
                AdjustFontSize.prototype.onClk = function (ev) {
                    var _this = this;
                    var el = ev.target;
                    var setFontSize = function (fontSize) {
                        document.body.$(_this.$props.articleTarget, function (p) { return p.style.fontSize = fontSize; });
                    };
                    if (el.tagName == 'LABEL' || el.tagName == 'input') {
                        if (el.tagName != 'LABEL')
                            el = el.up('label');
                        if (el.innerHTML.indexOf('大') != -1)
                            setFontSize('12pt');
                        else if (el.innerHTML.indexOf('中') != -1)
                            setFontSize('10.5pt');
                        else if (el.innerHTML.indexOf('小') != -1)
                            setFontSize('9pt');
                    }
                };
                return AdjustFontSize;
            }(aj.VueComponent));
            page.AdjustFontSize = AdjustFontSize;
            new AdjustFontSize().register();
        })(page = widget.page || (widget.page = {}));
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));
