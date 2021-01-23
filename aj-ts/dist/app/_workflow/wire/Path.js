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
    var svg;
    (function (svg) {
        /**
         * 连线路径
         */
        var Path = /** @class */ (function (_super) {
            __extends(Path, _super);
            function Path(from, to, text) {
                var _this = 
                // 继承
                _super.call(this, from, to) || this;
                _this._from = from;
                _this._to = to;
                _this.type = 'transition'; // 默认 path 就是 Transition 类型
                _this.svg = PAPER.path().addClass('path');
                _this.arrow = PAPER.path().addClass('arrow');
                // aj.svg.DotList.call(this, from, to);
                // aj.apply(this, aj.svg.DotList.prototype);
                aj.svg.Mgr.register(_this);
                _this.hide();
                _this.moveFn = _this.rectResizeHandler.bind(_this);
                from.vue.addUpdateHandler(_this.moveFn);
                to.vue.addUpdateHandler(_this.moveFn);
                _this.refreshPath();
                return _this;
            }
            // 矩形移动时处理器
            Path.prototype.rectResizeHandler = function () {
                var o, dot;
                //            if (from && from.node.id == event.target.id) {
                dot = this.fromDot.right().right();
                if (dot.type == svg.Dot.TO)
                    o = { x: this._to.getBBox().x + this._to.getBBox().width / 2, y: this._to.getBBox().y + this._to.getBBox().height / 2 };
                else
                    o = dot.pos();
                var r = svg.Utils.connPoint(this._from.getBBox(), o);
                this.fromDot.moveTo(r.x, r.y);
                this.refreshPath();
                //            }
                //            if (to && to.node.id == event.target.id) {
                dot = this.toDot.left().left();
                if (dot.type == svg.Dot.FROM)
                    o = { x: this._from.getBBox().x + this._from.getBBox().width / 2, y: this._from.getBBox().y + this._from.getBBox().height / 2 };
                else
                    o = dot.pos();
                var r = svg.Utils.connPoint(this._to.getBBox(), o);
                this.toDot.moveTo(r.x, r.y);
                this.refreshPath();
                //            }
            };
            Path.prototype.from = function () {
                return this._from;
            };
            Path.prototype.to = function () {
                return this._to;
            };
            Path.prototype.remove = function () {
                aj.svg.DotList.prototype.remove.call(this);
                this.svg.remove();
                this.arrow.remove();
                if (this.textObj) {
                    this.textObj.$el.parentNode.removeChild(this.textObj.$el);
                    this.textObj = null;
                }
                this.from.vue.removeUpdateHandler(this.moveFn); // 卸载事件
                this.to.vue.removeUpdateHandler(this.moveFn);
            };
            ;
            // 刷新路径
            Path.prototype.refreshPath = function () {
                var r = this.toPathString(), mid = this.midDot().pos();
                this.svg.attr({ path: r[0] });
                this.arrow.attr({ path: r[1] });
                if (this.textObj) {
                    var textPos = this.textObj.getXY(); // 定位文字
                    this.textObj.setXY(mid.x + 30, mid.y + 10);
                }
            };
            Path.prototype.attr = function (o) {
                o && o.path && this.svg.attr(o.path);
                o && o.arrow && this.arrow.attr(o.arrow);
            };
            return Path;
        }(svg.DotList));
        svg.Path = Path;
    })(svg = aj.svg || (aj.svg = {}));
})(aj || (aj = {}));
