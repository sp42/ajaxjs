/**
 * 渐显 banner
 * 注意：定时器保存在 DOM 元素的属性上，是否会内存泄漏呢？
 */
; (() => {
    interface OpacityBanner extends Vue {
        active: number;

        timer: number;

        delay: number;

        fps: number;

        /**
         * 各帧
         */
        list: NodeListOf<HTMLLIElement>;

        states: [];

        /**
        * 内容淡出 
        */
        clear(): void;

        animate(params: number): void;

        run(): void;
    }

    Vue.component('aj-opacity-banner', {
        template: '<ul class="aj-opacity-banner"><slot></slot></ul>',
        props: {
            delay: { default: 3000 },   // 延时
            fps: { default: 25 }        // 帧速
        },
        data() {
            return {
                active: 0,  // 当前索引
            }
        },
        mounted(this: OpacityBanner): void {
            this.list = this.$el.querySelectorAll('li');
            this.list[0].style.opacity = "1";
            console.log(this.list.length);
            this.run();
        },
        methods: {
            /**
             * 播放动画
             * 
             * @param this 
             */
            run(this: OpacityBanner): void {
                this.timer = window.setInterval(() => {
                    let active: number = this.active;
                    this.clear();
                    active += 1;
                    this.active = active % this.list.length;
                    this.animate(100);
                }, this.delay);
            },

            /**
             * 下一帧
             * 
             * @param this 
             */
            per(this: OpacityBanner): void {
                let active: number = this.active;
                this.clear();
                active -= 1;
                active = active % this.list.length;

                if (active < 0)
                    active = this.list.length - 1;

                this.active = active;
                this.animate(100);
            },

            /**
             * 内容淡出 
             */
            clear(this: OpacityBanner): void {
                this.animate(0);
            },

            /**
             * 
             * @param this 
             * @param params 
             */
            animate(this: OpacityBanner, params: number): void {
                var el: HTMLLIElement = this.list[this.active], fps: number = 1000 / this.fps;
                // @ts-ignore
                window.clearTimeout(el.timer);

                window.setTimeout(function loop() {
                    var i: number = getOpacity(el);
                    var speed: number = (params - i) / 8, speed = speed > 0 ? Math.ceil(speed) : Math.floor(speed);
                    // console.log("i=" + i + "; speed="+ speed+"; s="+s+"; k="+k);
                    i += speed;
                    el.style.opacity = String(i / 100);
                    // @ts-ignore
                    window.clearTimeout(el.timer);
                    // params.complete && params.complete.call(elem);
                    // @ts-ignore
                    el.timer = window.setTimeout(loop, fps);
                }, fps);
            }
        }
    });

    /**
     * 获取元首的透明度
     * 
     * @param el 
     */
    function getOpacity(el: Element): number {
        var v: number = Number(getComputedStyle(el)["opacity"]);
        v *= 100;

        return parseFloat(v + "") || 0;
    }
})();