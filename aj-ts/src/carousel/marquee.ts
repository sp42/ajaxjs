namespace aj.carousel {
    interface Marquee extends Vue {
        interval: number;
        canstop: boolean;
        start(): void;
        clearTimer(): void;

        /**
         * 滚动的方法
         */
        scroll(): void;

        /**
         * 定时器 id，内部用
         */
        $timerId: number;
    }

    /**
     * 简单跑马灯 Super Simple Marquee
     */
    export var marqueeBase = {
        props: {
            interval: { type: Number, default: 500 },// 动画时间间隔
            canstop: { type: Boolean, default: true }//  是否可以鼠标移入时候暂停动画
        },
        mounted(this: Marquee): void {
            if (this.canstop) {
                this.$el.onmouseover = this.clearTimer.bind(this);
                this.$el.onmouseout = this.start.bind(this);
            }
        },
        methods: {
            start(this: Marquee): void {
                this.$timerId = window.setInterval(this.scroll, this.interval);
            },
            clearTimer(this: Marquee): void {
                this.$timerId && window.clearInterval(this.$timerId);
            }
        }
    };

    /**
     * 超简单跑马灯
     * 插槽中不能包含 tag
     */
    interface SuperSimpleMarquee extends Marquee {
        $arr: string[]
    }

    Vue.component('aj-super-simple-marquee', {
        mixins: [marqueeBase],
        template: '<div><slot>这是一段滚动的文字；这是一段滚动的文字；这是一段滚动的文字</slot></div>',
        mounted(this: SuperSimpleMarquee): void {
            this.$arr = this.$el.innerHTML.split("");
            this.start();
        },
        methods: {
            scroll(this: SuperSimpleMarquee): void {
                this.$arr.push(<string>this.$arr.shift());
                this.$el.innerHTML = this.$arr.join("");
            }
        }
    });

    /**
     * 上下字幕
     */
    Vue.component('aj-simple-marquee', {
        mixins: [marqueeBase],
        template: '<ol><li>11111111111</li><li>22222222222</li><li>33333333333</li><li>44444444444</li><li>55555555555</li></ol>',
        mounted(this: Marquee): void {
            this.start();
        },
        methods: {
            scroll(this: Marquee): void {
                let lastEl: HTMLElement = <HTMLElement>this.$el.firstChild;

                while (lastEl.nodeType != 1)
                    lastEl = <HTMLElement>lastEl.nextSibling;// 找到最后一个元素

                this.$el.appendChild(this.$el.removeChild(lastEl)); // 把最后一个元素放到前头
            }
        }
    });

    /**
     * TODO 缺少 DEMO
     */
    interface AjMarquee extends Marquee {
        itemHeight: number;
        pauseInterval: number;
    }

    Vue.component('aj-marquee', {
        mixins: [marqueeBase],
        template: `
            <div class="aj-simple-marquee" style="width: 100%; overflow: hidden;">
                <div class="items"><slot></slot></div>
                <div class="clone"></div>
            </div>
        `,
        props: {
            interval: { default: 20 },
            pauseInterval: { type: Number, default: 2000 },// 暂停间隔时间
            itemHeight: { type: Number, default: 20 }// 每一项的高度
        },
        mounted(this: AjMarquee): void {
            let el = this.$el, children: HTMLCollection = (<HTMLElement>el.$('.items')).children, itemHeight: number = this.itemHeight;
            el.style.height = itemHeight + "px";

            let allHeight: number = 0;
            for (let i = 0, j = children.length; i < j; i++) { // 设置每行高度
                let item: HTMLElement = <HTMLElement>children[i];
                item.style.display = 'block';
                item.style.height = itemHeight + "px";

                allHeight += itemHeight;
            }

            (<HTMLElement>el.$('.clone')).style.height = allHeight + 'px';// 相同高度

            // 复制第一个元素
            let clone = children[0].cloneNode(true);
            (<HTMLElement>el.$('.clone')).appendChild(clone);

            setTimeout(this.start.bind(this), 2000);
        },
        methods: {
            scroll(this: AjMarquee): void {
                let el: HTMLElement = this.$el, top: number = el.scrollTop, height: number = (<HTMLElement>el.$('.items')).clientHeight;

                if (top <= height) {
                    el.scrollTop++;

                    if (top != 0 && (top % this.itemHeight) === 0) {
                        this.clearTimer();
                        setTimeout(this.start.bind(this), this.pauseInterval);
                    }
                } else {// 第一个恰好滑完
                    el.scrollTop -= height; //返回至开头处
                }
            }
        }
    });
}