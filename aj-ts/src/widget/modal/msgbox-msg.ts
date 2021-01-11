namespace aj.widget {
    /**
     * 顶部出现，用于后台提示信息多
     */
    interface TopMsg extends Vue {
        showText: string;
        /**
         * 顶部出现，用于后台提示信息多
         * 
         * @param text 显示文字
         * @param cfg  配置项，可选的
         */
        show(text: string, cfg?: TopMsgConfig): void;
    }

    /**
     * TopMsg 配置 
     */
    interface TopMsgConfig extends Vue {
        /**
         * 消失之后触发的事件
         */
        afterClose: Function;
        /**
         * 显示时间，单位是毫秒。默认是三秒
         */
        showTime: number;
    }

    document.addEventListener("DOMContentLoaded", () => {
        let msgEl: HTMLDivElement = document.createElement('div');
        msgEl.className = 'aj-topMsg';
        msgEl.setAttribute('v-html', "showText");
        document.body.appendChild(msgEl);

        aj.msg = new Vue({
            el: msgEl,
            data: {
                showText: '' // 显示的内容
            },
            methods: {
                show(this: TopMsg, text: string, cfg?: TopMsgConfig): void {
                    this.showText = text;
                    var el = this.$el;

                    setTimeout(() => {
                        el.classList.remove('fadeOut');
                        el.classList.add('fadeIn');
                    }, 0);

                    setTimeout(() => { // 自动隐藏，无须 close
                        el.classList.remove('fadeIn');
                        el.classList.add('fadeOut');
                        cfg && cfg.afterClose && cfg.afterClose(this);
                    }, cfg && cfg.showTime || 3000);
                }
            }
        });
    });
}