/**
 * 消息框、弹窗、对话框组件
 */
namespace aj.widget.modal {

    interface MsgBoxConfig {
        /**
         * 显示时间，单位是毫秒。默认是三秒
         */
        showTime?: number;

        /**
         * 消失之后触发的事件
         */
        afterClose?: (div: Element | null, msgBox: MsgBox) => void;

        showYes?: boolean;
        showNo?: boolean;
        showOk?: boolean;
        showSave?: boolean;

        onOkClk?: Function;
        onNoClk?: Function;
        onYesClk?: Function;
    }

    interface MsgBox extends MsgBoxConfig, Vue {
        /**
         * 显示文字，支持 HTML 标签
         */
        showText: string;

        /**
         * 显示
         * 
         * @param this 
         * @param text  显示的内容
         * @param cfg   配置项，可选的
         */
        show(text: string, cfg?: MsgBoxConfig): MsgBox;
    }

    export var msgbox: MsgBox;

    document.addEventListener("DOMContentLoaded", () => {
        document.body.appendChild(document.createElement('div')).className = 'alertHolder';

        // 全屏幕弹窗，居中显示文字。
        // 不应直接使用该组件，而是执行 aj.showOk
        msgbox = <MsgBox>new Vue({
            el: '.alertHolder',
            template: `
                <div class="aj-modal hide" @click="close">
                    <div>
                        <div v-html="showText"></div>
                        <div class="aj-btnsHolder">
                            <button v-show="showOk"  @click="onBtnClk" class="ok">确定</button>
                            <button v-show="showYes" @click="onBtnClk" class="yes">{{showSave? '保存': '是'}}</button>
                            <button v-show="showNo"  @click="onBtnClk" class="no">{{showSave? '否': '否'}}</button>
                        </div>
                    </div>
                </div>
            `,
            data: {
                showText: '', 		// 显示的内容
                afterClose: null,	// 关闭弹窗后的回调
                showOk: false,
                showYes: false,
                showNo: false,
                showSave: false     // 是否显示“保存”按钮
            },
            methods: {
                /**
                 * 显示
                 * 
                 * @param this 
                 * @param text  显示文字，支持 HTML 标签
                 * @param cfg   配置项，可选的
                 */
                show(this: MsgBox, text: string, cfg?: MsgBoxConfig): MsgBox {
                    this.showText = text;
                    this.$el.classList.remove('hide');
                    cfg && aj.apply(this, cfg);

                    return this;
                },

                /**
                 * 关闭窗体
                 * 
                 * @param this 
                 * @param ev    事件对象，可选的
                 */
                close(this: MsgBox, ev?: Event): boolean {
                    if (!ev) { // 直接关闭
                        this.$el.classList.add('hide');
                        this.afterClose && this.afterClose(null, this);
                        return true;
                    }

                    let div: Element = <Element>ev.target; // check if in the box

                    if (div && div.className.indexOf('modal') != -1) {
                        this.$el.classList.add('hide');
                        this.afterClose && this.afterClose(div, this);
                        return true;
                    }

                    return false;
                },
                onBtnClk(this: MsgBox, ev: Event): void {
                    switch ((<Element>ev.target).className) {
                        case 'ok':
                            this.onOkClk && this.onOkClk(ev, this);
                            break;
                        case 'no':
                            this.onNoClk && this.onNoClk(ev, this);
                            break;
                        case 'yes':
                            this.onYesClk && this.onYesClk(ev, this);
                            break;
                    }
                }
            }
        });
    });

    /**
     * 顯示確定的對話框
     * 
     * @param {String} text         显示的文本
     * @param {Function} callback   回调函数
     */
    aj.alert = (text: string, callback?: Function): void => {
        let alertObj = msgbox.show(text, {
            showYes: false,
            showNo: false,
            showOk: true,
            onOkClk(): void { // 在 box 里面触发关闭，不能直接用 msgbox.close(e);
                alertObj.$el.classList.add('hide');
                callback && callback();
            }
        });
    }

    /**
     * 顯示“是否”選擇的對話框
     * 
     * @param {String} text         显示的文本
     * @param {Function} callback   回调函数
     */
    aj.showConfirm = (text: string, callback?: Function, showSave?: boolean): void => {
        let alertObj = msgbox.show(text, {
            showYes: true,
            showNo: true,
            showOk: false,
            showSave: false,
            onYesClk(ev: Event): void {
                alertObj.$el.classList.add('hide');
                callback && callback(alertObj.$el, ev);
            },
            onNoClk(): void { // 在box里面触发关闭，不能直接用 msgbox.close(e);
                alertObj.$el.classList.add('hide');
            }
        });
    }

    export var popup = (text: string, callback?: Function): void => {
        msgbox.show(text, {
            showYes: false,
            showNo: false,
            showOk: false,
            showSave: false
        });
    }

    //----------------------------------------------------------------------------------------

    /**
    * 顶部出现，用于后台提示信息多
    */
    document.addEventListener("DOMContentLoaded", () => {
        let msgEl: HTMLDivElement = document.createElement('div');
        msgEl.className = 'aj-topMsg';
        msgEl.setAttribute('v-html', "showText");
        document.body.appendChild(msgEl);

        aj.msg = new Vue({
            el: msgEl,
            data: { showText: '' }, // 显示的内容
            methods: {
                show(this: MsgBox, text: string, cfg?: MsgBoxConfig): void {
                    this.showText = text;
                    let el = this.$el;

                    setTimeout(() => {
                        el.classList.remove('fadeOut');
                        el.classList.add('fadeIn');
                    }, 0);

                    setTimeout(() => { // 自动隐藏，无须 close
                        el.classList.remove('fadeIn');
                        el.classList.add('fadeOut');
                        cfg && cfg.afterClose && cfg.afterClose(el, this);
                    }, cfg && cfg.showTime || 3000);
                }
            }
        });
    });
}