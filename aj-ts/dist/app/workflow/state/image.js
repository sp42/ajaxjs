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
         * 一个流程节点（图片）
         */
        var ImageState = /** @class */ (function (_super) {
            __extends(ImageState, _super);
            function ImageState(PAPER, type, ref, rawData, vBox) {
                var _this = _super.call(this, PAPER, type, ref, rawData, vBox, getAttr(PAPER, type, vBox)) || this;
                _this.resize = false; // 图片禁止放大缩小
                return _this;
            }
            return ImageState;
        }(wf.BaseState));
        function getAttr(PAPER, type, vBox) {
            var attr = {};
            switch (type) { // 生成 box
                case 'start':
                    attr = imgBox('start.png', vBox);
                    break;
                case 'decision':
                    attr = imgBox('decision.png', vBox);
                    break;
                case 'end':
                    attr = imgBox('end.png', vBox);
                    break;
            }
            return PAPER.image().attr({}).addClass('baseImg');
        }
        /**
         * 增加图片 src 属性
         *
         * @param img
         * @param size
         * @returns
         */
        var imgBox = function (img, size) { return aj.apply({ src: "../asset/images/workflow/" + img }, size); };
    })(wf = aj.wf || (aj.wf = {}));
})(aj || (aj = {}));
