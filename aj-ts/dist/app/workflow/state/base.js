"use strict";
var aj;
(function (aj) {
    var wf;
    (function (wf) {
        var BaseState = /** @class */ (function () {
            function BaseState(PAPER, type, ref, rawData, vBox, svg) {
                this.updateHandlers = [];
                this.isDrag = true;
                this.resize = true;
                this.id = wf.ComMgr.nextId();
                this.PAPER = PAPER;
                this.type = type;
                this.ref = ref;
                this.rawData = rawData;
                this.vBox = vBox;
                this.svg = svg;
                this.vue = new Vue({
                    mixins: [aj.svg.BaseRect],
                    data: { vBox: vBox }
                });
                this.isDrag && this.svg.drag(onDragMove, onDragStart, onDragEnd); // 使对象可拖动
            }
            BaseState.prototype.init = function () {
                throw new Error("Method not implemented.");
            };
            BaseState.prototype.toJson = function () {
                throw new Error("Method not implemented.");
            };
            BaseState.prototype.show = function () {
                throw new Error("Method not implemented.");
            };
            BaseState.prototype.hide = function () {
                throw new Error("Method not implemented.");
            };
            BaseState.prototype.pos = function (p) {
                throw new Error("Method not implemented.");
            };
            BaseState.prototype.moveTo = function (x, y) {
                throw new Error("Method not implemented.");
            };
            BaseState.prototype.remove = function () {
                throw new Error("Method not implemented.");
            };
            return BaseState;
        }());
        wf.BaseState = BaseState;
        /**
         * 开始拖动
         */
        function onDragStart() {
            var x = Number(this.attr('x')), y = Number(this.attr('y'));
            this.movingX = x, this.movingY = y;
            this.attr({ opacity: .3 }); // 拖动时半透明效果
            this.vue.onDragStart && this.vue.onDragStart(this.vue, x, y);
        }
        /**
         * 拖动中
         *
         * @param x
         * @param y
         */
        function onDragMove(x, y) {
            var _x = this.movingX + x, _y = this.movingY + y;
            this.attr({ x: _x, y: _y });
            this.vue.vBox.x = _x;
            this.vue.vBox.y = _y;
            this.vue.onDragMove && this.vue.onDragMove(this.vue, _x, _y);
        }
        /**
         * 拖动完毕
         */
        function onDragEnd() {
            this.attr({ opacity: 1 });
            // why more one arg 'this'?
            this.vue.onDragEnd && this.vue.onDragEnd(this.vue, this, this.attr('x'), this.attr('y'));
        }
    })(wf = aj.wf || (aj.wf = {}));
})(aj || (aj = {}));
