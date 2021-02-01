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
         * 进度条
         */
        var ProcessLine = /** @class */ (function (_super) {
            __extends(ProcessLine, _super);
            function ProcessLine() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.name = 'aj-process-line';
                _this.template = html(__makeTemplateObject(["\n            <div class=\"aj-process-line\">\n                <div class=\"process-line\">\n                    <div v-for=\"(item, index) in items\" :class=\"{current: index == current, done: index < current}\">\n                        <span>{{index + 1}}</span>\n                        <p>{{item}}</p>\n                    </div>\n                </div>\n            </div>\n        "], ["\n            <div class=\"aj-process-line\">\n                <div class=\"process-line\">\n                    <div v-for=\"(item, index) in items\" :class=\"{current: index == current, done: index < current}\">\n                        <span>{{index + 1}}</span>\n                        <p>{{item}}</p>\n                    </div>\n                </div>\n            </div>\n        "]));
                _this.props = {
                    items: {
                        type: Array,
                        default: function () {
                            return ['Step 1', 'Step 2', 'Step 3'];
                        }
                    }
                };
                _this.items = [];
                _this.current = 0;
                return _this;
            }
            /**
             *
             * @param i
             */
            ProcessLine.prototype.go = function (i) {
                this.current = i;
            };
            /**
             *
             */
            ProcessLine.prototype.perv = function () {
                var perv = this.current - 1;
                if (perv < 0)
                    perv = this.items.length - 1;
                this.go(perv);
            };
            /**
             *
             */
            ProcessLine.prototype.next = function () {
                var next = this.current + 1;
                if (this.items.length == next)
                    next = 0; // 循环
                this.go(next);
            };
            return ProcessLine;
        }(aj.VueComponent));
        widget.ProcessLine = ProcessLine;
        new ProcessLine().register();
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));
