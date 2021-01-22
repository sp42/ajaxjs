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
             * 计算矩形中心到点 p 的连线与矩形的交叉点
             *
             * @param rect 矩形
             * @param p    点 p
             */
            Utils.connPoint = function (rect, p) {
                var start = p, end = { x: rect.x + rect.width / 2, y: rect.y + rect.height / 2 };
                // 计算正切角度
                var tag = (end.y - start.y) / (end.x - start.x);
                tag = isNaN(tag) ? 0 : tag;
                var rectTag = rect.height / rect.width;
                // 计算箭头位置
                var xFlag = start.y < end.y ? -1 : 1, yFlag = start.x < end.x ? -1 : 1, arrowTop = 0, arrowLeft = 0;
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
                return {
                    x: (p1.x - p2.x) / 2 + p2.x,
                    y: (p1.y - p2.y) / 2 + p2.y
                };
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
             * @returns 箭头的三个点
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
        /**
         * 创建文本节点，返回 vue 实例
         *
         * @param _text
         * @param x
         * @param y
         */
        function createTextNode(_text, x, y) {
            var text = document.createElementNS("http://www.w3.org/2000/svg", "text");
            text.textContent = '{{value}}';
            text.setAttributeNS(null, "x", String(x));
            text.setAttributeNS(null, "y", String(y));
            document.body.$('svg').appendChild(text);
            return new Vue({
                el: text,
                data: typeof _text == 'string' ? { value: _text } : _text,
                methods: {
                    /**
                     * 设置文字坐标
                     *
                     * @param this
                     * @param x
                     * @param y
                     */
                    setXY: function (x, y) {
                        this.$el.setAttributeNS(null, "x", String(x));
                        this.$el.setAttributeNS(null, "y", String(y));
                    },
                    /**
                     *
                     * @param this
                     */
                    getXY: function () {
                        return { x: Number(this.$el.getAttribute('x')), y: Number(this.$el.getAttribute('y')) };
                    },
                    /**
                     * 在一个 box 中居中定位文字
                     *
                     * @param vBox
                     */
                    setXY_vBox: function (vBox) {
                        var w = vBox.x + vBox.width / 2, h = vBox.y + vBox.height / 2;
                        var textBox = this.$el.getBoundingClientRect();
                        this.setXY(w - textBox.width / 2, h + 5);
                    }
                }
            });
        }
        svg.createTextNode = createTextNode;
        svg.serialize = {
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
                    //	let str = ['<transition offset="', Math.round(_textPos.x) + "," + Math.round(_textPos.y), '" to="', path.to().getName(), '" '];
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
