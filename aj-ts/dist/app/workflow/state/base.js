"use strict";
var aj;
(function (aj) {
    var wf;
    (function (wf) {
        var BaseState = /** @class */ (function () {
            function BaseState(PAPER, ref, data, svg) {
                this.updateHandlers = [];
                this.isDrag = true;
                this.resize = true;
                this.id = wf.ComMgr.nextId();
                this.PAPER = PAPER;
                this.type = data.type;
                this.ref = ref;
                this.rawData = data;
                this.vBox = data.attr;
                this.svg = svg;
                this.svg.comp = this;
                this.vue = this.initVue();
                this.isDrag && this.svg.drag(onDragMove, onDragStart, onDragEnd); // 使对象可拖动
                wf.ComMgr.register(this);
            }
            BaseState.prototype.init = function () {
                throw new Error("Method not implemented.");
            };
            BaseState.prototype.toJson = function () {
                throw new Error("Method not implemented.");
            };
            BaseState.prototype.show = function () {
                this.svg.show();
            };
            BaseState.prototype.hide = function () {
                this.svg.hide();
            };
            BaseState.prototype.pos = function (p) {
                if (p)
                    this.moveTo(p.x, p.y);
                return { x: this.vBox.x, y: this.vBox.y };
            };
            BaseState.prototype.moveTo = function (x, y) {
                this.svg.attr({ x: x, y: y });
                this.vBox.x = x;
                this.vBox.y = y;
            };
            BaseState.prototype.remove = function () {
                this.svg.remove();
                this.updateHandlers = [];
                wf.ComMgr.unregister(this.id);
            };
            /**
             * 初始化 Vue 数据驱动
             *
             * @returns
             */
            BaseState.prototype.initVue = function () {
                var self = this, vue = new Vue({
                    data: { vBox: this.vBox, text: '' },
                    watch: {
                        vBox: {
                            handler: function (val) {
                                self.updateVBox(val);
                            },
                            deep: true
                        }
                    }
                });
                return vue;
            };
            /**
             * 更新 vbox 的事件
             *
             * @param val
             */
            BaseState.prototype.updateVBox = function (val) {
                var _this = this;
                this.updateHandlers.forEach(function (fn) { return fn.call(_this, val); });
                if (this.resize && this.resizeController) {
                    this.resizeController.setDotsPosition();
                    this.resizeController.updateBorder();
                }
                if (this.text) // 文字伴随着图形拖放
                    this.text.setXY_vBox(this.vBox);
            };
            /**
             * 加入 updateVBoxHandler
             *
             * @param fn
             */
            BaseState.prototype.addUpdateHandler = function (fn) {
                this.updateHandlers.push(fn);
            };
            /**
             * 移除 updateVBoxHandler
             *
             * @param fn
             */
            BaseState.prototype.removeUpdateHandler = function (fn) {
                var index = null;
                for (var i = 0, j = this.updateHandlers.length; i < j; i++) {
                    if (this.updateHandlers[i] == fn)
                        index = i;
                }
                if (index != null)
                    this.updateHandlers.splice(index, 1);
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
            this.comp.onDragStart && this.comp.onDragStart(this.comp, x, y);
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
            var s = this.comp;
            s.vBox.x = _x;
            s.vBox.y = _y;
            this.comp.onDragMove && this.comp.onDragMove(this.comp, _x, _y);
        }
        /**
         * 拖动完毕
         */
        function onDragEnd() {
            this.attr({ opacity: 1 });
            // why more one arg 'this'?
            this.comp.onDragEnd && this.comp.onDragEnd(this.comp, this, this.attr('x'), this.attr('y'));
        }
    })(wf = aj.wf || (aj.wf = {}));
})(aj || (aj = {}));
