"use strict";
var aj;
(function (aj) {
    var svg;
    (function (svg) {
        svg.BaseRect = {
            data: function () {
                return {
                    vBox: {},
                    text: '' // 显示的文字，居中显示
                };
            },
            methods: {
                show: function () {
                    this.svg.show();
                },
                hide: function () {
                    this.svg.hide();
                },
                pos: function (x, y) {
                    this.svg.attr({ x: x, y: y });
                    this.vBox.x = x;
                    this.vBox.y = y;
                },
                init: function () {
                    this.svg.vue = this; // 对 raphael 图形实例保存组件
                    this.isDrag && this.svg.drag(onDragMove, onDragStart, onDragEnd); // 使对象可拖动
                    if (this.text) {
                        this.textNode = svg.createTextNode(this.text, 0, 0);
                        this.textNode.setXY_vBox(this.vBox);
                    }
                    if (this.resize) {
                        this.resizeController = new svg.ResizeControl(this);
                        this.resizeController.renderer();
                    }
                },
                remove: function () {
                    this.svg.remove();
                    this.updateHandlers = [];
                    aj.svg.Mgr.unregister(this.id);
                },
                addUpdateHandler: function (fn) {
                    this.updateHandlers.push(fn);
                },
                removeUpdateHandler: function (fn) {
                    var index;
                    for (var i = 0, j = this.updateHandlers.length; i < j; i++) {
                        if (this.updateHandlers[i] == fn)
                            index = i;
                    }
                    this.updateHandlers.splice(index, 1);
                }
            },
            created: function () {
                /* this上的数据不一定要在data中定义，如果不想变成响应式数据就没有必要定义，这样反而会性能优化 */
                aj.apply(this, {
                    type: String,
                    id: String,
                    name: String,
                    svg: Object,
                    wfData: {},
                    PAPER: Object,
                    isDrag: true,
                    resize: true,
                    resizeNode: null,
                    textNode: Object // 文字对象，这是 Vue 实例
                });
                this.updateHandlers = []; // 当大小、位置有变化时候执行的函数列表
            },
            watch: {
                vBox: {
                    handler: function (val) {
                        var _this = this;
                        this.updateHandlers.forEach(function (fn) { return fn.call(_this, val); });
                        if (this.resize) {
                            this.resizeController.setDotsPosition();
                            this.resizeController.updateBorder();
                        }
                        if (this.textNode) // 文字伴随着图形拖放
                            this.textNode.setXY_vBox(this.vBox);
                    },
                    deep: true
                }
            }
        };
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
    })(svg = aj.svg || (aj.svg = {}));
})(aj || (aj = {}));
