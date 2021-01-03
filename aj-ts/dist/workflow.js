"use strict";
/* ----------------------------------------工具函数---------------------------------- */
//使的 SVG 图形可以添加样式类
Raphael.el.addClass = function (className) {
    this.node.setAttribute("class", className);
    return this;
};
var aj;
(function (aj) {
    var svg;
    (function (svg) {
        var Utils = /** @class */ (function () {
            function Utils() {
            }
            /**
             * 计算矩形中心到 p 的连线与矩形的交叉点
             */
            Utils.connPoint = function (rect, p) {
                var start = p, end = { x: rect.x + rect.width / 2, y: rect.y + rect.height / 2 };
                // 计算正切角度
                var tag = (end.y - start.y) / (end.x - start.x);
                tag = isNaN(tag) ? 0 : tag;
                var rectTag = rect.height / rect.width;
                // 计算箭头位置
                var xFlag = start.y < end.y ? -1 : 1, yFlag = start.x < end.x ? -1 : 1, arrowTop, arrowLeft;
                // 按角度判断箭头位置
                if (Math.abs(tag) > rectTag && xFlag == -1) { // top 边
                    arrowTop = end.y - rect.height / 2;
                    arrowLeft = end.x + xFlag * rect.height / 2 / tag;
                }
                else if (Math.abs(tag) > rectTag && xFlag == 1) { // bottom 边
                    arrowTop = end.y + rect.height / 2;
                    arrowLeft = end.x + xFlag * rect.height / 2 / tag;
                }
                else if (Math.abs(tag) < rectTag && yFlag == -1) { // left 边
                    arrowTop = end.y + yFlag * rect.width / 2 * tag;
                    arrowLeft = end.x - rect.width / 2;
                }
                else if (Math.abs(tag) < rectTag && yFlag == 1) { // right 边
                    arrowTop = end.y + rect.width / 2 * tag;
                    arrowLeft = end.x + rect.width / 2;
                }
                return { x: arrowLeft, y: arrowTop };
            };
            /**
             * 求两个点的中间点
             *
             * @param p1
             * @param p2
             */
            Utils.center = function (p1, p2) {
                return { x: (p1.x - p2.x) / 2 + p2.x, y: (p1.y - p2.y) / 2 + p2.y };
            };
            /**
             * 三个点是否在一条直线上
             *
             * @param p1
             * @param p2
             * @param p3
             */
            Utils.isLine = function (p1, p2, p3) {
                var s, p2y;
                if ((p1.x - p3.x) == 0)
                    s = 1;
                else
                    s = (p1.y - p3.y) / (p1.x - p3.x);
                p2y = (p2.x - p3.x) * s + p3.y;
                // $('body').append(p2.y+'-'+p2y+'='+(p2.y-p2y)+', ');
                if ((p2.y - p2y) < 10 && (p2.y - p2y) > -10) {
                    p2.y = p2y;
                    return true;
                }
                return false;
            };
            /**
             * 画箭头
             *
             * @param p1 开始位置
             * @param p2 结束位置
             * @param r  前头的边长
             */
            Utils.arrow = function (p1, p2, r) {
                var atan = Math.atan2(p1.y - p2.y, p2.x - p1.x) * (180 / Math.PI);
                var centerX = p2.x - r * Math.cos(atan * (Math.PI / 180));
                var centerY = p2.y + r * Math.sin(atan * (Math.PI / 180));
                var x2 = centerX + r * Math.cos((atan + 120) * (Math.PI / 180));
                var y2 = centerY - r * Math.sin((atan + 120) * (Math.PI / 180));
                var x3 = centerX + r * Math.cos((atan + 240) * (Math.PI / 180));
                var y3 = centerY - r * Math.sin((atan + 240) * (Math.PI / 180));
                return [p2, { x: x2, y: y2 }, { x: x3, y: y3 }];
            };
            return Utils;
        }());
        svg.Utils = Utils;
        function foo() { }
        // 创建文本节点，返回 vue 实例
        aj.svg.createTextNode = function (_text, x, y) {
            var text = document.createElementNS("http://www.w3.org/2000/svg", "text");
            text.textContent = '{{value}}';
            text.setAttributeNS(null, "x", x);
            text.setAttributeNS(null, "y", y);
            aj('svg').appendChild(text);
            return new Vue({
                el: text,
                data: typeof _text == 'string' ? { value: _text } : _text,
                methods: {
                    // 设置文字坐标
                    setXY: function (x, y) {
                        this.$el.setAttributeNS(null, "x", x);
                        this.$el.setAttributeNS(null, "y", y);
                    },
                    getXY: function () {
                        return { x: Number(this.$el.getAttribute('x')), y: Number(this.$el.getAttribute('y')) };
                    },
                    // 在一个 box 中居中定位文字
                    setXY_vBox: function (vBox) {
                        var w = vBox.x + vBox.width / 2, h = vBox.y + vBox.height / 2;
                        var textBox = this.$el.getBoundingClientRect();
                        this.setXY(w - textBox.width / 2, h + 5);
                    }
                }
            });
        };
        aj.svg.serialize = {
            /**
             * 生成键对值
             *
             * @param strArr
             * @param key
             * @param value
             */
            keyValue: function (strArr, key, value) {
                if (value) {
                    value = value.replace(/>/g, "#5").replace(/</g, "#6").replace(/&/g, "#7");
                    strArr.push(" " + key + '="' + value + '"');
                }
            },
            rect: {
                toJson: function (_o, _text, _rect) {
                    var data = "{type:'" + _o.type + "',text:{text:'" + _text.attr('text') +
                        "'}, attr:{ x:" + Math.round(_rect.attr('x')) + ", y:" + Math.round(_rect.attr('y')) +
                        ", width:" + Math.round(_rect.attr('width')) + ", height:" + Math.round(_rect.attr('height')) + "}, props:{";
                    for (var k in _o.props)
                        data += k + ":{value:'" + _o.props[k].value + "'},";
                    if (data.substring(data.length - 1, data.length) == ',')
                        data = data.substring(0, data.length - 1);
                    data += "}}";
                    return data;
                },
                toBeforeXml: function (comp) {
                    var str = [" <", comp.type, ' layout="' +
                            (Math.round(comp.svg.attr("x")) - 180), ",", Math.round(comp.svg.attr("y")), ",",
                        Math.round(comp.svg.attr("width")), ",", Math.round(comp.svg.attr("height")), '"'];
                    var value, keyValue = aj.svg.serialize.keyValue;
                    for (var i in comp.wfData) {
                        value = comp.wfData[i].value;
                        if (i == "name" && !value) {
                            aj.alert(comp.type + " 名称 不能为空");
                            return "";
                        }
                        if (i === "layout")
                            continue;
                        keyValue(str, i, value);
                    }
                    str.push(">");
                    return str.join('');
                }
            },
            path: {
                toJson: function (_from, _to, _dotList, _text, _textPos) {
                    var r = "{from:'" + _from.getId() + "',to:'" + _to.getId() + "', dots:" + _dotList.toJson() + ",text:{text:'" + _text.attr("text") +
                        "'},textPos:{_dotList:" + Math.round(_textPos.x) + ",_ox:" + Math.round(_textPos.y) + "}, props:{";
                    for (var o in _o.props)
                        r += o + ":{value:'" + _o.props[o].value + "'},";
                    if (r.substring(r.length - 1, r.length) == ",")
                        r = r.substring(0, r.length - 1);
                    r += "}}";
                    return r;
                },
                toXml: function (path, _textPos) {
                    //	var str = ['<transition offset="', Math.round(_textPos.x) + "," + Math.round(_textPos.y), '" to="', path.to().getName(), '" '];
                    var str = ['<transition offset="" to="', path.to().vue.id, '" '];
                    var dots = aj.svg.serialize.dotList.toXml(path.fromDot);
                    if (dots != "")
                        str.push(' g="' + dots + '" ');
                    var value, keyValue = aj.svg.serialize.keyValue;
                    for (var i in path.wfData) {
                        value = path.wfData[i].value;
                        if (i === "name" && value == "") { // name 为空，使用 id 作为 name
                            str.push(i + '="' + _id + '" ');
                            continue;
                        }
                        keyValue(str, i, value);
                    }
                    str.push("/>");
                    return str.join('');
                }
            },
            dotList: {
                toJson: function (_fromDot) {
                    var data = "[", d = _fromDot;
                    while (d) {
                        if (d.type === aj.svg.Dot.BIG)
                            data += "{x:" + Math.round(d.pos().x) + ",y:" + Math.round(d.pos().y) + "},";
                        d = d.right();
                    }
                    if (data.substring(data.length - 1, data.length) == ",")
                        data = data.substring(0, data.length - 1);
                    data += "]";
                    return data;
                },
                toXml: function (_fromDot) {
                    var data = "", d = _fromDot;
                    while (d) {
                        if (d.type === aj.svg.Dot.BIG)
                            data += (Math.round(d.pos().x) - 180) + "," + Math.round(d.pos().y) + ";";
                        d = d.right();
                    }
                    if (data.substring(data.length - 1, data.length) == ";")
                        data = data.substring(0, data.length - 1);
                    return data;
                }
            }
        };
    })(svg = aj.svg || (aj.svg = {}));
})(aj || (aj = {}));

"use strict";
var aj;
(function (aj) {
    var svg;
    (function (svg) {
        /**
         * 基础图形组件
         */
        var BaseComponent = /** @class */ (function () {
            function BaseComponent() {
                this.id = "";
            }
            return BaseComponent;
        }());
        svg.BaseComponent = BaseComponent;
    })(svg = aj.svg || (aj.svg = {}));
})(aj || (aj = {}));

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
    (function (svg_1) {
        var diff = 3;
        /**
         * 表示一个点。点只有坐标，没有大小。
         */
        var Dot = /** @class */ (function (_super) {
            __extends(Dot, _super);
            /**
             * 创建一个新的点
             *
             * @param type      点类型
             * @param _pos      点的坐标
             * @param _lt       左边的点
             * @param _rt       右边的点
             * @param path      点所在的路径对象
             */
            function Dot(type, _pos, _lt, _rt, path) {
                var _this = _super.call(this) || this;
                var svg = PAPER.rect(_pos.x - diff, _pos.y - diff); // 形状实例
                svg.addClass('dot');
                _this.svg = svg;
                _this.type = type;
                _this._leftDot = _lt;
                _this._rightDot = _rt;
                _this._pos = _pos;
                _this.path = path;
                if (type == Dot.BIG || type == Dot.SMALL) {
                    var _ox, _oy; // 缓存移动前的位置
                    svg.drag(function (dx, dy) { return _this.moveTo(_ox + dx, _oy + dy); }, function () { _ox = svg.attr("x") + diff, _oy = svg.attr("y") + diff; }); // 开始拖动
                }
                return _this;
            }
            /**
             * 获取左边的节点，或者设置左边的点
             *
             * @param dot 新的点
             */
            Dot.prototype.left = function (dot) {
                if (dot)
                    this._leftDot = dot;
                return this._leftDot;
            };
            /**
             * 获取右边的节点，或者设置右边的点
             *
             * @param dot 新的点
             */
            Dot.prototype.right = function (dot) {
                if (dot)
                    this._rightDot = dot;
                return this._rightDot;
            };
            Dot.prototype.remove = function () {
                this._leftDot = this._rightDot = null;
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
                var _pos = this.pos({ x: x, y: y });
                var path = this.path;
                var _lt = this.left(), _rt = this.right();
                var right2x = _rt && _rt.right(), left2x = _lt && _lt.left();
                switch (this.type) {
                    case Dot.FROM:
                        if (right2x && right2x.type == Dot.TO)
                            right2x.pos(svg_1.Utils.connPoint(path.to().getBBox(), _pos));
                        if (_rt && right2x)
                            _rt.pos(svg_1.Utils.center(_pos, right2x.pos()));
                        break;
                    case Dot.BIG:
                        if (right2x && right2x.type == Dot.TO)
                            right2x.pos(svg_1.Utils.connPoint(path.to().getBBox(), _pos));
                        if (left2x && left2x.type == Dot.FROM)
                            left2x.pos(svg_1.Utils.connPoint(path.from().getBBox(), _pos));
                        if (right2x)
                            _rt === null || _rt === void 0 ? void 0 : _rt.pos(svg_1.Utils.center(_pos, right2x.pos()));
                        if (left2x)
                            _lt === null || _lt === void 0 ? void 0 : _lt.pos(svg_1.Utils.center(_pos, left2x.pos()));
                        // 三个大点在一条线上，移除中间的小点
                        if (svg_1.Utils.isLine(left2x === null || left2x === void 0 ? void 0 : left2x.pos(), _pos, right2x === null || right2x === void 0 ? void 0 : right2x.pos())) {
                            this.type = Dot.SMALL;
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
                    case Dot.SMALL: // 移动小点时，转变为大点，增加俩个小点
                        if (_lt && _rt && !svg_1.Utils.isLine(_lt.pos(), _pos, _rt.pos())) {
                            this.type = Dot.BIG; // 变为 BIG 类型之后，只执行一次了
                            this.svg.attr({ width: 5, height: 5, stroke: "#fff", fill: "#000", cursor: "move", "stroke-width": 2 });
                            var P_1 = new Dot(Dot.SMALL, svg_1.Utils.center(_lt.pos(), _pos), _lt, _lt.right(), path);
                            _lt.right(P_1);
                            this._leftDot = _lt = P_1;
                            var R_1 = new Dot(Dot.SMALL, svg_1.Utils.center(_rt.pos(), _pos), _rt.left(), _rt, path);
                            _rt.left(R_1);
                            this._rightDot = _rt = R_1;
                        }
                        break;
                    case Dot.TO:
                        if (left2x && left2x.type == Dot.FROM)
                            left2x.pos(svg_1.Utils.connPoint(path.from().getBBox(), _pos));
                        if (left2x)
                            _lt.pos(svg_1.Utils.center(_pos, left2x.pos()));
                        break;
                }
                path.refreshPath(); // 线的路径, 转换为 path 格式的字串
            };
            Dot.prototype.toJson = function () { return ""; };
            Dot.prototype.show = function () { };
            Dot.prototype.hide = function () { };
            return Dot;
        }(svg_1.BaseComponent));
        svg_1.Dot = Dot;
        Dot.TO = 1;
        Dot.FROM = 2;
        Dot.SMALL = 3;
        Dot.BIG = 4;
    })(svg = aj.svg || (aj.svg = {}));
})(aj || (aj = {}));
