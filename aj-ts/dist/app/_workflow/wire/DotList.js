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
        * 先有点再有线
        */
        var DotList = /** @class */ (function (_super) {
            __extends(DotList, _super);
            /**
             *
             * @param from  起始连接的组件
             * @param to    终点连接的组件
             */
            function DotList(from, to) {
                var _this = _super.call(this) || this;
                var fromBB = from.getBBox(), toBB = to.getBBox();
                var fromPos = svg.Utils.connPoint(fromBB, { x: toBB.x + toBB.width / 2, y: toBB.y + toBB.height / 2 }), // 起点是 box 中央，这个是起点坐标
                toPos = svg.Utils.connPoint(toBB, fromPos); // 终点坐标
                /* 先创建中间点，一开始左右点都是 null 的，——这没关系，先空着，下面会计算 */
                var midDot = new svg.Dot(svg.Dot.SMALL, { x: (fromPos.x + toPos.x) / 2, y: (fromPos.y + toPos.y) / 2 }, null, null, _this); // 中间点
                /* 起点没有 left 点，right 点就是 midDot 了 */
                _this.fromDot = new svg.Dot(svg.Dot.FROM, fromPos, null, midDot, _this); // 起点对象
                /* 终点没有 right 点，left 点就是 midDot 了 */
                _this.toDot = new svg.Dot(svg.Dot.TO, toPos, midDot, null, _this); // 终点对象
                /* 补充中间点的左右两点信息 */
                midDot.left(_this.fromDot), midDot.right(_this.toDot);
                return _this;
            }
            /**
             * 转换为 path 格式的字串
             * @returns 数组，其中元素1是线的路径，元素2是箭头的路径
             */
            DotList.prototype.toPathString = function () {
                if (!this.fromDot)
                    return "";
                var d = this.fromDot, p = "M" + d.pos().x + " " + d.pos().y, arr = [];
                // 线的路径
                while (d.right()) {
                    d = d.right();
                    arr.push("L" + d.pos().x + " " + d.pos().y);
                }
                p += arr.join('');
                // 箭头路径
                var arrPos = svg.Utils.arrow(d.left().pos(), d.pos(), 4);
                var _arrow = "M" + arrPos[0].x + " " + arrPos[0].y + "L" + arrPos[1].x + " " + arrPos[1].y + "L" + arrPos[2].x + " " + arrPos[2].y + "z";
                return [p, _arrow];
            };
            /**
             * 遍历链表，查找中间的点
             */
            DotList.prototype.midDot = function () {
                var mid = this.fromDot.right(), end = mid.right();
                while (end.right() && end.right().right()) {
                    end = end.right().right();
                    mid = mid.right();
                }
                return mid;
            };
            DotList.prototype.remove = function () {
                var d = this.fromDot;
                while (d) {
                    if (d.right()) {
                        d = d.right();
                        d.left().remove();
                    }
                    else {
                        d.remove();
                        d = null;
                    }
                }
            };
            DotList.prototype.toJson = function () {
                return aj.svg.serialize.dotList.toJson(this.fromDot);
            };
            /**
             * 输入坐标数据，还原图形
             *
             * @param points 坐标数组
             */
            DotList.prototype.restore = function (points) {
                var d = this.fromDot.right(), point;
                for (var i = 0, j = points.length; i < j; i++) {
                    point = points[i];
                    if (d) {
                        d.moveTo(point.x, point.y);
                        d = d.right();
                    }
                }
            };
            /**
             * 显示
             */
            DotList.prototype.show = function () {
                display.call(this, this.fromDot, true);
            };
            /**
             * 隐藏
             */
            DotList.prototype.hide = function () {
                display.call(this, this.fromDot, false);
            };
            return DotList;
        }(svg.BaseComponent));
        svg.DotList = DotList;
        /**
         * 显示或隐藏
         *
         * @param {Dot}     开始的点 fromDot
         * @param {boolean} true=显示
         */
        function display(dotObj, isShow) {
            while (dotObj) {
                isShow ? dotObj.svg.show() : dotObj.svg.hide();
                dotObj = dotObj.right();
            }
        }
    })(svg = aj.svg || (aj.svg = {}));
})(aj || (aj = {}));
