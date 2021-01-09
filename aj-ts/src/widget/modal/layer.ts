interface LayerConfig {
    /**
     * 关闭层之后触发的事件
     */
    afterClose: Function
}

/**
 * 浮層組件，通常要復用這個組件
 */
interface Layer extends Vue {
    afterClose: Function;
    notCloseWhenTap: boolean;
    cleanAfterClose: boolean;
}

Vue.component('aj-layer', {
    template: '<div class="aj-modal hide" @click="close"><div><slot></slot></div></div>',
    props: {
        notCloseWhenTap: Boolean, // 默认点击窗体关闭，当 notCloseWhenTap = true 时禁止关闭
        cleanAfterClose: Boolean  // 关闭是否清除
    },
    methods: {
        /**
         * 显示浮层
         * 
         * @param this 
         * @param cfg 
         */
        show(this: Layer, cfg?: LayerConfig): void {
            let my: number = Number(getComputedStyle(this.$el).zIndex); // 保证最后显示的总在最前面

            document.body.$('.aj-modal', (i: Element) => {
                if (i != this.$el) {
                    let o: number = Number(getComputedStyle(i).zIndex);
                    if (o >= my)
                        this.$el.style.zIndex = String(o + 1);
                }
            });

            this.$el.classList.remove('hide');
            this.BUS && this.BUS.$emit('aj-layer-closed', this);

            if (cfg && cfg.afterClose)
                this.afterClose = cfg && cfg.afterClose;
        },

        /**
         * 关闭浮层
         * 
         * @param this 
         * @param e 
         */
        close(this: Layer, e: Event): void { // isForceClose = 强制关闭
            let isClosed: boolean = false;

            if (!e) {
                isClosed = aj.widget.msgbox.$options.methods.close.call(this, {
                    target: document.body.$('.aj-modal')
                });
            } else {
                // @ts-ignore
                if (e.isForceClose || !this.notCloseWhenTap)
                    isClosed = aj.widget.msgbox.$options.methods.close.apply(this, arguments);
            }

            if (isClosed && this.cleanAfterClose) {
                this.$el.parentNode && this.$el.parentNode.removeChild(this.$el);
                this.$destroy();
            }
        }
    }
});