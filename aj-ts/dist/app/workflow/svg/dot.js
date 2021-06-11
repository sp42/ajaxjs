"use strict";
var aj;
(function (aj) {
    var svg;
    (function (svg_1) {
        var diff = 3;
        /**
         * 点类型的常量
         */
        var DOT_TYPE;
        (function (DOT_TYPE) {
            /**
             * 起点
             */
            DOT_TYPE[DOT_TYPE["TO"] = 1] = "TO";
            /**
             * 终点
             */
            DOT_TYPE[DOT_TYPE["FROM"] = 2] = "FROM";
            /**
             *
             */
            DOT_TYPE[DOT_TYPE["SMALL"] = 3] = "SMALL";
            /**
             *
             */
            DOT_TYPE[DOT_TYPE["BIG"] = 4] = "BIG";
        })(DOT_TYPE = svg_1.DOT_TYPE || (svg_1.DOT_TYPE = {}));
        /**
         * 表示一个点。点只有坐标，没有大小。
         */
        var Dot = /** @class */ (function () {
            /**
             * 创建一个新的点
             *
             * @param type      点类型
             * @param pos       点的坐标
             * @param path      点所在的路径对象
             * @param leftDot   左边的点
             * @param rightDot  右边的点
             */
            function Dot(type, pos, path, leftDot, rightDot) {
                var _this = this;
                var svg = svg_1.PAPER.rect(pos.x - diff, pos.y - diff); // 形状实例
                svg.addClass('dot');
                this.svg = svg;
                this.type = type;
                this._leftDot = leftDot;
                this._rightDot = rightDot;
                this._pos = pos;
                this.path = path;
                if (type == DOT_TYPE.BIG || type == DOT_TYPE.SMALL) {
                    var _ox_1, _oy_1; // 缓存移动前的位置
                    svg.drag(function (dx, dy) { return _this.moveTo(_ox_1 + dx, _oy_1 + dy); }, function () {
                        _ox_1 = Number(svg.attr("x")) + diff;
                        _oy_1 = Number(svg.attr("y")) + diff;
                    }); // 开始拖动
                }
            }
            /**
             * 获取左边的节点，或者设置左边的点
             *
             * @param dot 左边的点
             */
            Dot.prototype.left = function (dot) {
                if (dot)
                    this._leftDot = dot;
                return this._leftDot;
            };
            /**
             * 获取右边的节点，或者设置右边的点
             *
             * @param dot 右边的点
             */
            Dot.prototype.right = function (dot) {
                if (dot)
                    this._rightDot = dot;
                return this._rightDot;
            };
            /**
             * 返回左边的点。
             * 这个断言必然是有这个点的
             *
             * @returns 左边的点
             */
            Dot.prototype.getLeftDot = function () {
                return this._leftDot;
            };
            /**
             * 返回右边的点。
             * 这个断言必然是有这个点的
             *
             * @returns 右边的点
             */
            Dot.prototype.getRightDot = function () {
                return this._rightDot;
            };
            Dot.prototype.remove = function () {
                this._leftDot = this._rightDot = undefined;
                this.svg.remove();
            };
            /**
             * 定位这个点，或者获取该点的坐标。如果有新的定位送入，则采用新的定位。
             * 这个方法仅仅是改变点的坐标，要同步修改相关联的点，请使用 moveTo() 方法。
             *
             * @param p 新的定位坐标，这是可选的
             * @returns 这个点的坐标
             */
            Dot.prototype.pos = function (p) {
                if (p && !isNaN(p.x) && !isNaN(p.y)) {
                    this._pos = p;
                    this.svg.attr({ x: p.x - diff, y: p.y - diff });
                }
                return this._pos;
            };
            /**
             * 移动到某个点，并同步相关的点。
             *
             * @param x X 坐标
             * @param y y 坐标
             */
            Dot.prototype.moveTo = function (x, y) {
                var _pos = this.pos({ x: x, y: y }), path = this.path, _lt = this.left(), _rt = this.right(), right2x = _rt && _rt.right(), left2x = _lt && _lt.left();
                switch (this.type) {
                    case DOT_TYPE.FROM:
                        if (right2x && right2x.type == DOT_TYPE.TO)
                            right2x.pos(svg_1.Utils.connPoint(path.to.getBBox(), _pos));
                        if (_rt && right2x)
                            _rt.pos(svg_1.Utils.center(_pos, right2x.pos()));
                        break;
                    case DOT_TYPE.BIG:
                        if (right2x && right2x.type == DOT_TYPE.TO)
                            right2x.pos(svg_1.Utils.connPoint(path.to.getBBox(), _pos));
                        if (left2x && left2x.type == DOT_TYPE.FROM)
                            left2x.pos(svg_1.Utils.connPoint(path.from.getBBox(), _pos));
                        if (_rt && right2x)
                            _rt.pos(svg_1.Utils.center(_pos, right2x.pos()));
                        if (_lt && left2x)
                            _lt.pos(svg_1.Utils.center(_pos, left2x.pos()));
                        // 三个大点在一条线上，移除中间的小点
                        if (left2x && _lt && _rt && right2x && svg_1.Utils.isLine(left2x.pos(), _pos, right2x.pos())) {
                            this.type = DOT_TYPE.SMALL;
                            this.svg.attr({ width: 5, height: 5, stroke: "#fff", fill: "#000", cursor: "move", "stroke-width": 3 });
                            var P = _lt;
                            left2x.right(_lt.right());
                            this._leftDot = _lt = left2x;
                            P === null || P === void 0 ? void 0 : P.remove();
                            var R = _rt;
                            right2x.left(_rt.left());
                            this._rightDot = _rt = right2x;
                            R.remove();
                        }
                        break;
                    case DOT_TYPE.SMALL: // 移动小点时，转变为大点，增加俩个小点
                        if (_lt && _rt && !svg_1.Utils.isLine(_lt.pos(), _pos, _rt.pos())) {
                            this.type = DOT_TYPE.BIG; // 变为 BIG 类型之后，只执行一次了
                            this.svg.attr({ width: 5, height: 5, stroke: "#fff", fill: "#000", cursor: "move", "stroke-width": 2 });
                            var P = new Dot(DOT_TYPE.SMALL, svg_1.Utils.center(_lt.pos(), _pos), path, _lt, _lt.right());
                            _lt.right(P);
                            this._leftDot = _lt = P;
                            var R = new Dot(DOT_TYPE.SMALL, svg_1.Utils.center(_rt.pos(), _pos), path, _rt.left(), _rt);
                            _rt.left(R);
                            this._rightDot = _rt = R;
                        }
                        break;
                    case DOT_TYPE.TO:
                        if (left2x && left2x.type == DOT_TYPE.FROM)
                            left2x.pos(svg_1.Utils.connPoint(path.from.getBBox(), _pos));
                        if (_lt && left2x)
                            _lt.pos(svg_1.Utils.center(_pos, left2x.pos()));
                        break;
                }
                path.refreshPath(); // 线的路径, 转换为 path 格式的字串
            };
            Dot.prototype.show = function () {
                this.svg.show();
            };
            Dot.prototype.hide = function () {
                this.svg.hide();
            };
            return Dot;
        }());
        svg_1.Dot = Dot;
    })(svg = aj.svg || (aj.svg = {}));
})(aj || (aj = {}));
