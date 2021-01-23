namespace aj.svg {
    /**
     * 桌布
     */
    export let PAPER: any;

    export let BaseRect = {
        data() {
            return {
                vBox: {}, 	// 虚拟的一个控制框
                text: ''	// 显示的文字，居中显示
            };
        },
        methods: {
            show(this: SvgVue): void {
                this.svg.show();
            },

            hide(this: SvgVue): void {
                this.svg.hide();
            },

            pos(this: SvgVue, x: number, y: number): void {
                this.svg.attr({ x: x, y: y });
                this.vBox.x = x;
                this.vBox.y = y;
            },

            init(this: SvgVue): void {
                this.svg.vue = this; // 对 raphael 图形实例保存组件
                this.isDrag && this.svg.drag(onDragMove, onDragStart, onDragEnd);	// 使对象可拖动

                if (this.text) {
                    this.textNode = createTextNode(this.text, 0, 0);
                    this.textNode.setXY_vBox(this.vBox);
                }

                if (this.resize) {
                    this.resizeController = new ResizeControl(this);
                    this.resizeController.renderer();
                }
            },

            remove(this: SvgVue): void {
                this.svg.remove();
                this.updateHandlers = [];
                aj.svg.Mgr.unregister(this.id);
            },

            addUpdateHandler(this: SvgVue, fn): void {
                this.updateHandlers.push(fn);
            },
            removeUpdateHandler(this: SvgVue, fn): void {
                let index;
                for (let i = 0, j = this.updateHandlers.length; i < j; i++) {
                    if (this.updateHandlers[i] == fn)
                        index = i;
                }

                this.updateHandlers.splice(index, 1);
            }
        },
        created(this: SvgVue): void {
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
                handler(this: SvgVue, val): void {
                    this.updateHandlers.forEach(fn => fn.call(this, val));

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
    function onDragStart(this: Raphael): void {
        let x = this.attr('x'), y = this.attr('y');
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
    function onDragMove(this: Raphael, x: number, y: number): void {
        let _x: number = this.movingX + x, _y: number = this.movingY + y;

        this.attr({ x: _x, y: _y });
        let vBox: VBox = this.vue.vBox;
        vBox.x = _x, vBox.y = _y;

        this.vue.onDragMove && this.vue.onDragMove(this.vue, _x, _y);
    }

    /**
     * 拖动完毕
     */
    function onDragEnd(this: Raphael): void {
        this.attr({ opacity: 1 });
        // why more one arg 'this'?
        this.vue.onDragEnd && this.vue.onDragEnd(this.vue, this, this.attr('x'), this.attr('y'));
    }
}