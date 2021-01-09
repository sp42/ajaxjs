interface Carousel extends Vue {
    isMagic: boolean;

    isUsePx: boolean;

    autoHeight: boolean;

    isGetCurrentHeight: boolean;

    selected: number;

    stepWidth: number;

    tabWidth: string;

    mover: HTMLElement;

    setItemWidth(): number;

    $isStop: boolean;

    /**
     * 跳到指定的那一帧
     * 
     * @param i 
     */
    go(i: number): void;

    /**
     * 跳到下一帧。
     */
    goNext(): void;
}

namespace aj.carousel {
    /**
     * 基础类
     */
    export var base = {
        props: {
            isMagic: { type: Boolean, default: false },					// 是否无缝模式
            /*
                推荐使用 百分比，px 的话要考虑滚动条，比较麻烦，
                要使用 px 推荐指定 stepWidth banner，要使用 px；如果要跟手移动，要使用 px
            */
            isUsePx: { type: Boolean, default: false },
            autoHeight: { type: Boolean, default: false },				// 是否自动高度
            disableTabHeaderJump: { type: Boolean, default: false },	// 是否禁止通过 tab 候选栏来跳转。一般 wizzard 向导式的时候不能跳转
            isGetCurrentHeight: { type: Boolean, default: true },		// 自动当前项最高，忽略其他高度，这个用在 tab 很好，比 autoHeight 的好，可视作autoHeight 2.-
            initItems: Array 											// 输入的数据
        },
        data() {
            return {
                selected: 0
            };
        },
        mounted(this: Carousel): void {
            this.mover = <HTMLElement>this.$el.$('div > div');
            var mover: HTMLElement = this.mover, children: HTMLCollection = mover.children, len: number = children.length;

            setTimeout(() => {
                let stepWidth: number = this.setItemWidth();

                if (this.isMagic)
                    mover.style.width = this.isUsePx ? (stepWidth * 2) + 'px' : '200%';
                else
                    mover.style.width = this.isUsePx ? (stepWidth * len) + 'px' : len + '00%';

                let tabWidth: string = this.isUsePx ? stepWidth + 'px' : (1 / len * 100).toFixed(5) + '%';// 分配  tab 宽度
                this.tabWidth = tabWidth;

                for (var i = 0; i < len; i++)
                    (<HTMLElement>children[i]).style.width = this.isMagic ? '50%' : tabWidth;

                let headerUl = <HTMLElement>this.$el.$('header ul');
                if (headerUl)
                    for (var i = 0; i < len; i++)
                        (<HTMLElement>(headerUl.children[i])).style.width = tabWidth;

                doHeight.call(this, this.selected);
            }, 400);

            // 登记 resize 事件，以便 resize 容器的时候调整大小。
            // 使用 isUsePx = true 的好处是不用登记 resize 事件
            // this.isUsePx && ajaxjs.throttle.init(this.onResize.bind(this));
        },

        watch: {
            selected(this: Carousel, index: number, oldIndex: number): void {
                if (this.$isStop) // 停止，不切换，在做向导式时有用
                    return;

                //@ts-ignore
                let children: HTMLElement[] = this.$el.$('header ul').children;
                //@ts-ignore
                let contentChild: HTMLElement[] = this.$el.$('div > div').children;

                if (children && contentChild && children[oldIndex] && contentChild[oldIndex]) {
                    children[oldIndex].classList.remove('active');
                    contentChild[oldIndex].classList.remove('active');

                    children[index].classList.add('active');
                    contentChild[index].classList.add('active');

                    this.go(index);
                }
            }
        },

        methods: {
            /**
             * 设置 item 宽度
             * 
             * @param this 
             */
            setItemWidth(this: Carousel): number {
                this.stepWidth = this.stepWidth || (<HTMLElement>this.mover.parentNode).clientWidth || window.innerWidth; // 获取容器宽度作为 item 宽度
                return this.stepWidth;
            },

            /**
             * 
             * @param this 
             * @param i 
             */
            changeTab(this: Carousel, i: number): void {
                this.selected = i;
                this.go(i);
            },

            /**
             * 跳到指定的那一帧
             * 
             * @param this 
             * @param i 
             */
            go(this: Carousel, i: number): void {
                this.$emit('before-carousel-item-switch', this, i);
                if (this.$isStop) // 停止，不切换，在做向导式时有用
                    return;

                var mover = this.mover, children = mover.children, len = children.length;
                doHeight.call(this, i);

                if (this.isMagic) {
                    // clear before
                    for (var p = 0; p < len; p++) {
                        if (this.selected == p) // 当前的
                            continue;
                        else if (i == p) // 要显示的
                            children[p].classList.remove('hide');
                        else
                            children[p].classList.add('hide');
                    }

                    var cssText = i > this.selected
                        ? 'translate3d({0}, 0px, 0px)'.replace('{0}', '-50%')
                        : 'translate3d({0}, 0px, 0px)'.replace('{0}', '0%');
                    mover.style.webkitTransition = '-webkit-transform 400ms linear';

                    mover.style.webkitTransform = cssText;
                } else {
                    var isWebkit = navigator.userAgent.toLowerCase().indexOf('webkit') != -1;

                    if (!this.stepWidth)
                        this.setItemWidth();

                    let leftValue = this.isUsePx ? ('-' + (i * this.stepWidth) + 'px') : ('-' + (1 / len * 100 * i).toFixed(2) + '%');
                    mover.style['transform'] = 'translate3d({0}, 0px, 0px)'.replace('{0}', leftValue);

                    // 使用 left 移动！
                    // mover.style.left = this.isUsePx ? ('-' + (i * this.stepWidth) + 'px') : ('-'+ i + '00%');
                }

                this.$emit('carousel-item-switch', this, i, children[i]);
            },

            // 跳到前一帧。
            goPrevious(this: Carousel): void {
                if (this.$isStop) // 停止，不切换，在做向导式时有用
                    return;

                let len = this.mover.children.length;

                this.selected--;
                if (this.selected < 0)
                    this.selected = len - 1;
                this.go(this.selected);
            },

            /**
             * 跳到下一帧。
             */
            goNext(this: Carousel): void {
                if (this.$isStop) // 停止，不切换，在做向导式时有用
                    return;

                this.selected++;
                if (this.selected == this.mover.children.length)
                    this.selected = 0; // 循环
                this.go(this.selected);
            },

            // 重复了
            // onResize(this: Carousel): void {
            //     var stepWidth = this.mover.parentNode.clientWidth; // 获取容器宽度作为
            //     // item 宽度
            //     this.mover.style.width = this.isUsePx ? (stepWidth * this.len) + 'px' : this.len + '00%';

            //     for (var i = 0; i < this.len; i++)
            //         this.children[i].style.width = stepWidth + 'px';
            // },

            autoChangeTab(this: Carousel, e: Event): void {
                if (this.$isStop) // 停止，不切换，在做向导式时有用
                    return;

                let el: HTMLElement = <HTMLElement>e.currentTarget;
                let children: HTMLCollection = (<HTMLElement>el.parentNode).children;

                for (var i = 0, j = children.length; i < j; i++) {
                    if (el == children[i])
                        break;
                }

                this.selected = i;
            }
        }
    };

    /**
     * 控制高度 解决高度问题
     * 
     * @param this 
     * @param i 
     */
    function doHeight(this: Carousel, i: number): void {
        if (this.isGetCurrentHeight) {
            let mover = this.mover, children = mover.children, len: number = children.length;

            for (let p = 0; p < len; p++) {
                let el: HTMLElement = <HTMLElement>children[p];
                if (i == p)
                    el.style.height = 'initial';
                else
                    el.style.height = '1px';
            }
        }
    }

    function doAutoHeight(this: Carousel, nextItem: any): void {
        if (this.autoHeight) {
            var tabHeaderHeight = 0;
            // @ts-ignore
            if (this.tabHeader)
                // @ts-ignore
                tabHeaderHeight = this.tabHeader.scrollHeight;

            this.$el.style.height = (nextItem.scrollHeight + tabHeaderHeight + 50) + 'px';
        }
    }
}
