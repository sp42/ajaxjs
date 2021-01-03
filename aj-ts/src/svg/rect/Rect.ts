namespace aj.svg {
    var mixin = {
        data() {
            return {
                vBox: {}, 	// 虚拟的一个控制框
                text: ''	// 显示的文字，居中显示
            };
        },
        methods: {
            // 显示图型
            show() {
                this.svg.show();
            },

            // 隐藏图型
            hide() {
                this.svg.hide();
            },
            // 移动
            pos(x, y) {
                this.svg.attr({ x: x, y: y });
                this.vBox.x = x;
                this.vBox.y = y;
            },
            init() {
                this.svg.vue = this; // 对 raphael 图形实例保存组件
                this.isDrag && this.svg.drag(onDragMove, onDragStart, onDragEnd);	// 使对象可拖动

                if (this.text) {
                    this.textNode = aj.svg.createTextNode(this.text, 0, 0);
                    this.textNode.setXY_vBox(this.vBox);
                }

                if (this.resize) {
                    this.resizeNode = new aj.svg.ResizeControl(this);
                    this.resizeNode.renderer();
                }
            },
            // 在 DOM 中删除，并注销图形
            remove() {
                this.svg.remove();
                this.updateHandlers = [];
                aj.svg.Mgr.unregister(this.id);
            },

            addUpdateHandler(fn) {
                this.updateHandlers.push(fn);
            },
            removeUpdateHandler(fn) {
                var index;
                for (var i = 0, j = this.updateHandlers.length; i < j; i++) {
                    if (this.updateHandlers[i] == fn)
                        index = i;
                }

                this.updateHandlers.splice(index, 1);
            }
        },
        created() {
            /* this上的数据不一定要在data中定义，如果不想变成响应式数据就没有必要定义，这样反而会性能优化 */
            aj.apply(this, {
                type: String,	// 组件类型
                id: String,		// 每个图形对象赋予一个 id
                name: String,	// 通过 name 保存到 states 的 map 之中，它是 key
                svg: Object,	// raphael 图形实例
                wfData: {},		// 工作流状态信息
                PAPER: Object,	// 图形所在的桌布
                isDrag: true, 	// 是否启用鼠标拖放
                resize: true, 	// 是否启用放大缩小
                resizeNode: null,// resize 对象
                textNode: Object// 文字对象，这是 Vue 实例
            });

            this.updateHandlers = []; // 当大小、位置有变化时候执行的函数列表
        },
        watch: {
            vBox: {
                handler(val) {
                    this.updateHandlers.forEach(fn => fn.call(this, val));

                    if (this.resize) {
                        this.resizeNode.setDotsPosition();
                        this.resizeNode.updateBorder();
                    }

                    if (this.textNode) // 文字伴随着图形拖放
                        this.textNode.setXY_vBox(this.vBox);
                },
                deep: true
            }
        }
    };

    // 开始拖动
    function onDragStart() {
        var x = this.attr('x'), y = this.attr('y');
        this.movingX = x, this.movingY = y;
        this.attr({ opacity: .3 }); // 拖动时半透明效果
        this.vue.onDragStart && this.vue.onDragStart(this.vue, x, y);
    }

    // 拖动中
    function onDragMove(x: number, y: number) {
        var _x: number = this.movingX + x, _y: number = this.movingY + y;

        this.attr({ x: _x, y: _y });
        var vBox: VBox = this.vue.vBox;
        vBox.x = _x, vBox.y = _y;

        this.vue.onDragMove && this.vue.onDragMove(this.vue, _x, _y);
    }

    // 拖动完毕
    function onDragEnd() {
        this.attr({ opacity: 1 });
        // why more one arg 'this'?
        this.vue.onDragEnd && this.vue.onDragEnd(this.vue, this, this.attr('x'), this.attr('y'));
    }

    /**
     * 创建图形基类的工厂函数
     */
    export function createRect(PAPER, attr, type: string) {
        var raphaelObj;

        switch (type) {
            case 'img':
                raphaelObj = PAPER.image().attr(attr).addClass('baseImg');
                break;
            default:
                raphaelObj = PAPER.rect().attr(attr).attr({ fill: "90-#fff-#F6F7FF" }).addClass('rectBaseStyle');
        }

        var vueObj = new Vue({ mixins: [mixin], data: { vBox: attr } });

        vueObj.PAPER = PAPER;
        vueObj.svg = raphaelObj;

        // 登记注册
        aj.svg.Mgr.register(vueObj);

        if (type == 'img')
            vueObj.resize = false; // 图片禁止放大缩小

        if (aj.workflow.isREAD_ONLY)
            vueObj.resize = vueObj.isDrag = false;

        return vueObj;
    }
}