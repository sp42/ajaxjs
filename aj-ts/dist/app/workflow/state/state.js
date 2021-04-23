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
var aj;
(function (aj) {
    var wf;
    (function (wf) {
        /**
         * 一个流程节点
         */
        var State = /** @class */ (function (_super) {
            __extends(State, _super);
            function State(PAPER, type, ref, rawData, vBox) {
                var _this = this;
                var svg = PAPER.rect().attr(vBox).attr({ fill: "90-#fff-#F6F7FF" }).addClass('rectBaseStyle');
                _this = _super.call(this, PAPER, type, ref, rawData, vBox, svg) || this;
                return _this;
            }
            return State;
        }(wf.BaseState));
    })(wf = aj.wf || (aj.wf = {}));
})(aj || (aj = {}));
