"use strict";
var aj;
(function (aj) {
    var carousel;
    (function (carousel) {
        /**
         * 基础类
         */
        carousel.base = {
            props: {
                isMagic: { type: Boolean, default: false },
                /*
                    推荐使用 百分比，px 的话要考虑滚动条，比较麻烦，
                    要使用 px 推荐指定 stepWidth banner，要使用 px；如果要跟手移动，要使用 px
                */
                isUsePx: { type: Boolean, default: false },
                autoHeight: { type: Boolean, default: false },
                disableTabHeaderJump: { type: Boolean, default: false },
                isGetCurrentHeight: { type: Boolean, default: true },
                initItems: Array // 输入的数据
            },
            data: function () {
                return {
                    selected: 0
                };
            },
            mounted: function () {
                var _this = this;
                this.mover = this.$el.$('div > div');
                var mover = this.mover, children = mover.children, len = children.length;
                setTimeout(function () {
                    var stepWidth = _this.setItemWidth();
                    if (_this.isMagic)
                        mover.style.width = _this.isUsePx ? (stepWidth * 2) + 'px' : '200%';
                    else
                        mover.style.width = _this.isUsePx ? (stepWidth * len) + 'px' : len + '00%';
                    var tabWidth = _this.isUsePx ? stepWidth + 'px' : (1 / len * 100).toFixed(5) + '%'; // 分配  tab 宽度
                    _this.tabWidth = tabWidth;
                    for (var i = 0; i < len; i++)
                        children[i].style.width = _this.isMagic ? '50%' : tabWidth;
                    var headerUl = _this.$el.$('header ul');
                    if (headerUl)
                        for (var i = 0; i < len; i++)
                            (headerUl.children[i]).style.width = tabWidth;
                    doHeight.call(_this, _this.selected);
                }, 400);
                // 登记 resize 事件，以便 resize 容器的时候调整大小。
                // 使用 isUsePx = true 的好处是不用登记 resize 事件
                // this.isUsePx && ajaxjs.throttle.init(this.onResize.bind(this));
            },
            watch: {
                selected: function (index, oldIndex) {
                    if (this.$isStop) // 停止，不切换，在做向导式时有用
                        return;
                    var children = this.$el.$('header ul').children;
                    var contentChild = this.$el.$('div > div').children;
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
                setItemWidth: function () {
                    this.stepWidth = this.stepWidth || this.mover.parentNode.clientWidth || window.innerWidth; // 获取容器宽度作为 item 宽度
                    return this.stepWidth;
                },
                /**
                 *
                 * @param this
                 * @param i
                 */
                changeTab: function (i) {
                    this.selected = i;
                    this.go(i);
                },
                /**
                 * 跳到指定的那一帧
                 *
                 * @param this
                 * @param i
                 */
                go: function (i) {
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
                    }
                    else {
                        var isWebkit = navigator.userAgent.toLowerCase().indexOf('webkit') != -1;
                        if (!this.stepWidth)
                            this.setItemWidth();
                        var leftValue = this.isUsePx ? ('-' + (i * this.stepWidth) + 'px') : ('-' + (1 / len * 100 * i).toFixed(2) + '%');
                        mover.style['transform'] = 'translate3d({0}, 0px, 0px)'.replace('{0}', leftValue);
                        // 使用 left 移动！
                        // mover.style.left = this.isUsePx ? ('-' + (i * this.stepWidth) + 'px') : ('-'+ i + '00%');
                    }
                    this.$emit('carousel-item-switch', this, i, children[i]);
                },
                // 跳到前一帧。
                goPrevious: function () {
                    if (this.$isStop) // 停止，不切换，在做向导式时有用
                        return;
                    var len = this.mover.children.length;
                    this.selected--;
                    if (this.selected < 0)
                        this.selected = len - 1;
                    this.go(this.selected);
                },
                /**
                 * 跳到下一帧。
                 */
                goNext: function () {
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
                autoChangeTab: function (e) {
                    if (this.$isStop) // 停止，不切换，在做向导式时有用
                        return;
                    var el = e.currentTarget;
                    var children = el.parentNode.children;
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
        function doHeight(i) {
            if (this.isGetCurrentHeight) {
                var mover = this.mover, children = mover.children, len = children.length;
                for (var p = 0; p < len; p++) {
                    var el = children[p];
                    if (i == p)
                        el.style.height = 'initial';
                    else
                        el.style.height = '1px';
                }
            }
        }
        function doAutoHeight(nextItem) {
            if (this.autoHeight) {
                var tabHeaderHeight = 0;
                if (this.tabHeader)
                    tabHeaderHeight = this.tabHeader.scrollHeight;
                this.$el.style.height = (nextItem.scrollHeight + tabHeaderHeight + 50) + 'px';
            }
        }
    })(carousel = aj.carousel || (aj.carousel = {}));
})(aj || (aj = {}));
