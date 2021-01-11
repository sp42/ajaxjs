"use strict";
Vue.component('aj-layer', {
    template: '<div class="aj-modal hide" @click="close"><div><slot></slot></div></div>',
    props: {
        notCloseWhenTap: Boolean,
        cleanAfterClose: Boolean // 关闭是否清除
    },
    methods: {
        /**
         * 显示浮层
         *
         * @param this
         * @param cfg
         */
        show: function (cfg) {
            var _this = this;
            var my = Number(getComputedStyle(this.$el).zIndex); // 保证最后显示的总在最前面
            document.body.$('.aj-modal', function (i) {
                if (i != _this.$el) {
                    var o = Number(getComputedStyle(i).zIndex);
                    if (o >= my)
                        _this.$el.style.zIndex = String(o + 1);
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
        close: function (e) {
            var isClosed = false;
            if (!e) {
                isClosed = aj.widget.msgbox.$options.methods.close.call(this, {
                    target: document.body.$('.aj-modal')
                });
            }
            else {
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
