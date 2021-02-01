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
         * 展开闭合器
         */
        var Expander = /** @class */ (function (_super) {
            __extends(Expander, _super);
            function Expander() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.name = "aj-expander";
                _this.template = html(__makeTemplateObject(["\n            <div class=\"aj-expander\" :style=\"'height:' + (expended ? openHeight : closeHeight) + 'px;'\">\n                <div :class=\"expended ? 'closeBtn' : 'openBtn'\" @click=\"expended = !expended;\"></div>\n                <slot></slot>\n            </div>\n        "], ["\n            <div class=\"aj-expander\" :style=\"'height:' + (expended ? openHeight : closeHeight) + 'px;'\">\n                <div :class=\"expended ? 'closeBtn' : 'openBtn'\" @click=\"expended = !expended;\"></div>\n                <slot></slot>\n            </div>\n        "]));
                _this.expended = false;
                _this.openHeight = { type: Number, "default": 200 };
                _this.closeHeight = { type: Number, "default": 50 };
                return _this;
            }
            return Expander;
        }(aj.VueComponent));
        widget.Expander = Expander;
        new Expander().register();
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));
