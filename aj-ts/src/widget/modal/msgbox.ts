/**
 * 消息框、弹窗、对话框组件
 */
namespace aj.widget {
    interface MsgBox extends Vue {
        showText: string;
        afterClose: Function;

        /**
         * 显示
         * 
         * @param this 
         * @param text 
         * @param cfg 
         */
        show(text: string, cfg: MsgBoxConfig): MsgBox;
        onOkClk: Function;
        onNoClk: Function;
        onYesClk: Function;
    }

    interface MsgBoxConfig {
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
                 * @param text 
                 * @param cfg 
                 */
                show(this: MsgBox, text: string, cfg: MsgBoxConfig): MsgBox {
                    this.showText = text;
                    this.$el.classList.remove('hide');
                    aj.apply(this, cfg);

                    return this;
                },
                close(this: MsgBox, e: Event) {
                    if(!e) { // 直接关闭
                        this.$el.classList.add('hide');
                        this.afterClose && this.afterClose(this);
                        
                        return true;
                    }

                    let div: Element = <Element>e.target; // check if in the box

                    if (div && div.className.indexOf('modal') != -1) {
                        this.$el.classList.add('hide');
                        this.afterClose && this.afterClose(div, this);
                        return true;
                    }
                },
                onBtnClk(this: MsgBox, e: Event): void {
                    let el: Element = <Element>e.target;

                    switch (el.className) {
                        case 'ok':
                            this.onOkClk && this.onOkClk(e, this);
                            break;
                        case 'no':
                            this.onNoClk && this.onNoClk(e, this);
                            break;
                        case 'yes':
                            this.onYesClk && this.onYesClk(e, this);
                            break;
                    }
                }
            }
        });
    });

    /**
     * 顯示確定的對話框
     * 
     * @param {String} text 显示的文本
     * @param {Function} callback 回调函数
     */
    aj.alert = (text: string, callback?: Function): void => {
        var alertObj = msgbox.show(text, {
            showYes: false,
            showNo: false,
            showOk: true,
            onOkClk(e: Event) { // 在 box 里面触发关闭，不能直接用 msgbox.close(e);
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
        var alertObj = msgbox.show(text, {
            showYes: true,
            showNo: true,
            showOk: false,
            showSave: showSave,
            onYesClk(e: Event) {
                alertObj.$el.classList.add('hide');
                callback && callback(alertObj.$el, e);
            },
            onNoClk() { // 在box里面触发关闭，不能直接用 msgbox.close(e);
                alertObj.$el.classList.add('hide');
            }
        });
    }

    aj.simpleOk = (text: string, callback?: Function) => {
        var alertObj = msgbox.show(text, {
            showYes: false,
            showNo: false,
            showOk: false,
            onOkClk() { // 在box里面触发关闭，不能直接用 msgbox.close(e);
                alertObj.$el.classList.add('hide');
                callback && callback();
            }
        });
    }
}